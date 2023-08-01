package eu.coolblue.shop.shared.remote

class GraphQlService {
    private val platformApolloClient: PlatformApolloClient = getPlatformApolloClient()

    private val apolloClient = platformApolloClient.apolloClient

    suspend fun getProduct(
        id: String,
        postalCode: String?,
        shouldAddProductCollection: Boolean,
        isUserLoggedIn: Boolean
    ){

    }

}
