package com.cruftbusters.ktor_baseurl_util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.server.testing.*

inline fun <reified T> TestApplicationCall.decodeResponseBody(): T =
  ObjectMapper().readValue(response.content!!)