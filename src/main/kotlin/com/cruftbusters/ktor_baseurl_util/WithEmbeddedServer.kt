package com.cruftbusters.ktor_baseurl_util

import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.net.ServerSocket

fun withEmbeddedServer(
  module: Application.() -> Unit,
  block: (HttpClient) -> Unit,
) {
  val port = ServerSocket(0).run {
    val port = localPort
    close()
    port
  }

  embeddedServer(Netty, port = port, module = module).start()

  HttpClient {
    defaultRequest { setBaseUrl("http://localhost:$port") }
    install(JsonFeature)
  }.apply(block)
}