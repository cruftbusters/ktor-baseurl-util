plugins {
  kotlin("jvm") version "1.5.21"
  `java-library`
}

repositories {
  mavenCentral()
}

dependencies {
  testImplementation("io.kotest:kotest-runner-junit5-jvm:+")
}

tasks.withType<Test> {
  useJUnitPlatform()
}