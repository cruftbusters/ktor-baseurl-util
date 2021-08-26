package com.cruftbusters.ktor_baseurl_util

import io.ktor.client.request.*
import io.ktor.http.*

fun HttpRequestBuilder.setBaseUrl(baseUrl: String) {
  val (protocolString, host, portString, basePath) = Regex("(\\w+)://([\\w.-]+)(:(\\d+))?(.*)")
    .matchEntire(baseUrl)!!
    .groupValues
    .slice(listOf(1, 2, 4, 5))
  url.protocol = URLProtocol.createOrDefault(protocolString)
  url.host = host
  url.port = portString.toIntOrNull() ?: url.port
  url.encodedPath = if (url.encodedPath.isNotEmpty()) "$basePath${url.encodedPath.trimEnd('/')}" else basePath
}