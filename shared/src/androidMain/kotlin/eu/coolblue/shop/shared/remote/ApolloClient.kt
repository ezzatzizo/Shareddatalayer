package eu.coolblue.shop.shared.remote

import com.apollographql.apollo3.ApolloClient

class PlatformApolloClientAndroid : PlatformApolloClient {
    override var apolloClient: ApolloClient? = null
        set(value) {
            field = value as ApolloClient
        }
}

actual fun getPlatformApolloClient(): PlatformApolloClient =  PlatformApolloClientAndroid()
