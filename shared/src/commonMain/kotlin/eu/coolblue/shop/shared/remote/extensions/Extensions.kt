package eu.coolblue.shop.shared.remote.extensions

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.api.Subscription
import kotlinx.coroutines.flow.Flow

suspend fun <D : Query.Data> ApolloClient.suspendQuery(query: Query<D>): ApolloResponse<D> = query(
    query
).execute()

suspend fun <D : Mutation.Data> ApolloClient.suspendMutate(
    mutation: Mutation<D>
): ApolloResponse<D> = mutation(mutation).execute()

suspend fun <D : Subscription.Data> ApolloClient.subscribeFlow(
    subscription: Subscription<D>
): Flow<ApolloResponse<D>> = subscription(subscription).toFlow()
