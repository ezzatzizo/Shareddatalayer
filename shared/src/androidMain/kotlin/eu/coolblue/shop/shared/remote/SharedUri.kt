package eu.coolblue.shop.shared.remote

import eu.coolblue.shop.shared.remote.model.SharedUriModel
import java.net.URI

fun getUri(uri: SharedUriModel): URI {
      return URI(uri.value)
}

