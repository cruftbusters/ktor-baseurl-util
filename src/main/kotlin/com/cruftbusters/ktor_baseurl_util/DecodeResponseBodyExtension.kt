package com.cruftbusters.ktor_baseurl_util

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.server.testing.*

val mapper =  jsonMapper { addModule(kotlinModule()) }

inline fun <reified T> TestApplicationCall.decodeResponseBody(): T =
  mapper.readValue(response.content!!)