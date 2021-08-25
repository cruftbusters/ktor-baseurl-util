package com.cruftbusters.ktor_baseurl_util

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class LibraryTest : FunSpec({
  test("someLibraryMethodReturnsTrue") {
    val classUnderTest = Library()
    classUnderTest.someLibraryMethod() shouldBe true
  }
})
