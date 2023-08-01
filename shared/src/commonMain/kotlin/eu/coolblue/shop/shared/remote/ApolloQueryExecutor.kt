package eu.coolblue.shop.shared.remote

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Error
import com.apollographql.apollo3.api.Operation
import eu.coolblue.shop.shared.remote.model.ApolloErrorCode
import eu.coolblue.shop.shared.remote.model.ApolloErrorProperties

import kotlinx.coroutines.delay


class ApolloQueryExecutor {
    suspend fun <T : Operation.Data, R> execute(
        query: suspend () -> ApolloResponse<T>,
        mapper: (T) -> R?,
        retryCount: Int = 0,
        currentDelay: Long = 100L,
        maxDelay: Long = 1000L,
        factor: Int = 2
    ): ResultOf<R> {
        return try {
            val result = query.invoke()
            result.handleResponse(
                dataMapper = mapper,
                errorMapper = this::mapErrorsToErrorEntity,
                onSuccess = { ResultOf.Success(it) },
                onError = { errorEntity ->
                    retryIfNeeded(
                        retryCount = retryCount,
                        currentDelay = currentDelay,
                        maxDelay = maxDelay,
                        factor = factor,
                        query = query,
                        mapper = mapper,
                        errorEntity = errorEntity
                    )
                }
            )
        } catch (exception: Throwable) {
            retryIfNeeded(
                retryCount = retryCount,
                currentDelay = currentDelay,
                maxDelay = maxDelay,
                factor = factor,
                query = query,
                mapper = mapper,
                errorEntity = ErrorEntity.UnknownError(exception)
            )
        }
    }

    private inline fun <T : Operation.Data, R> ApolloResponse<T>.handleResponse(
        dataMapper: (T) -> R?,
        errorMapper: (List<Error>) -> ErrorEntity,
        onSuccess: (R) -> ResultOf.Success<R>,
        onError: (ErrorEntity) -> ResultOf<R>
    ): ResultOf<R> {
        return errors?.let { onError(errorMapper(it)) }
            ?: data?.let { dataMapper(it) }?.let { onSuccess(it) }
            ?: onError(ErrorEntity.NullDataError)
    }

    private suspend fun <T : Operation.Data, R> retryIfNeeded(
        retryCount: Int,
        currentDelay: Long,
        maxDelay: Long,
        factor: Int,
        query: suspend () -> ApolloResponse<T>,
        mapper: (T) -> R?,
        errorEntity: ErrorEntity
    ): ResultOf<R> {
        return if (retryCount != 0) {
            delay(currentDelay)
            execute(
                query = query,
                mapper = mapper,
                currentDelay = calculateExpBackoffDelay(currentDelay, maxDelay, factor),
                maxDelay = maxDelay,
                factor = factor,
                retryCount = retryCount - 1
            )
        } else {
            ResultOf.Error(errorEntity)
        }
    }

    private fun calculateExpBackoffDelay(
        currentDelay: Long,
        maxDelay: Long,
        factor: Int
    ): Long {
        return (currentDelay * factor).coerceAtMost(maxDelay)
    }

    private fun mapErrorsToErrorEntity(errorList: List<Error>) =
        errorList
            .map { error ->
                val errorCode = error.extensions?.get(ApolloErrorProperties.CODE.value) as? String
                errorCode?.let { ApolloErrorCode.valueOf(errorCode) }
            }
            .run { first()?.toErrorEntity() ?: ErrorEntity.UnknownError() }

    private fun ApolloErrorCode.toErrorEntity() = when (this) {
        ApolloErrorCode.UNAUTHENTICATED -> ErrorEntity.UnauthenticatedUserError
    }
}
