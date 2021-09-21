package com.cruftbusters.ktor_baseurl_util

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.request.*
import io.ktor.http.*

class SetBaseUrlExtensionKtTest : FunSpec({
  test("set the protocol") {
    HttpRequestBuilder().apply {
      setBaseUrl("https://localhost")
      url.protocol shouldBe URLProtocol.HTTPS
    }
  }

  test("set the hostname") {
    HttpRequestBuilder().apply {
      setBaseUrl("http://wasd-asaurus123.com")
      url.host shouldBe "wasd-asaurus123.com"
    }
  }

  test("set the port") {
    HttpRequestBuilder().apply {
      setBaseUrl("http://localhost:993")
      url.port shouldBe 993
    }
  }

  test("default port from protocol") {
    HttpRequestBuilder().apply {
      setBaseUrl("http://localhost")
      url.port shouldBe 80
      setBaseUrl("https://localhost")
      url.port shouldBe 443
    }
  }

  test("set the base path") {
    HttpRequestBuilder().apply {
      setBaseUrl("http://localhost:1/the/base/path")
      url.encodedPath shouldBe "/the/base/path"
    }
  }

  test("prefix the base path") {
    HttpRequestBuilder().apply {
      url.encodedPath = "/but/wait/theres/more"
      setBaseUrl("http://localhost:1/the/base/path")
      url.encodedPath shouldBe "/the/base/path/but/wait/theres/more"
    }
  }
})
