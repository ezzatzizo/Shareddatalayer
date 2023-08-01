package eu.coolblue.shop.shared.remote

import com.apollographql.apollo3.ApolloClient

interface PlatformApolloClient {
    var apolloClient: ApolloClient?
}

expect fun getPlatformApolloClient(): PlatformApolloClient
