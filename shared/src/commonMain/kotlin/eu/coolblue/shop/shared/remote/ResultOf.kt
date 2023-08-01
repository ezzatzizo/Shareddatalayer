package eu.coolblue.shop.shared.remote


sealed class ResultOf<out T> {

    data class Success<out T>(val result: T) : ResultOf<T>()
    data class Error<T>(val error: ErrorEntity = ErrorEntity.UnknownError()) : ResultOf<T>()
}

inline fun <reified T> ResultOf<T>.doIfFailure(
    callback: (error: ErrorEntity) -> Unit
): ResultOf<T> {
    if (this is ResultOf.Error) {
        callback(error)
    }
    return this
}

inline fun <reified T> ResultOf<T>.doIfSuccess(callback: (value: T) -> Unit): ResultOf<T> {
    if (this is ResultOf.Success<T>) {
        callback(result)
    }
    return this
}

inline fun <reified T> ResultOf<T>.letIfSuccess(callback: (value: T) -> ResultOf<T>): ResultOf<T> {
    return if (this is ResultOf.Success<T>) {
        callback(result)
    } else {
        this
    }
}

abstract class ErrorEntity : Throwable() {
    // TODO Revisit why are we handling this in the postal code checker. Presentation shouldn't know
    // about an specific http error
    object HttpBadRequestError : ErrorEntity()
    object OutdatedApiError : ErrorEntity()

    /** Represents an error caused by trying to use a logged in feature while being logged out */
    object UnauthenticatedUserError : ErrorEntity()
    object NetworkError : ErrorEntity()
    object NullDataError : ErrorEntity()
    data class UnknownError(override val cause: Throwable? = null) : ErrorEntity()
}
