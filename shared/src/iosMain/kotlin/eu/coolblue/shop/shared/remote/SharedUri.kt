package eu.coolblue.shop.shared.remote

import eu.coolblue.shop.shared.remote.model.SharedUriModel
import platform.Foundation.NSURL

fun getUri(uri: SharedUriModel): NSURL {
    return  NSURL(string = uri.value)
}
