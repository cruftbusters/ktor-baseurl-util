import java.net.URI

plugins {
  kotlin("jvm") version "1.5.21"
  `java-library`
  `maven-publish`
}

group = "com.cruftbusters"

repositories {
  mavenCentral()
}

dependencies {
  implementation("io.ktor:ktor-client-core:+")
  testImplementation("io.kotest:kotest-runner-junit5-jvm:+")
}

tasks.test {
  useJUnitPlatform()
}

version = ProcessBuilder("sh", "-c", "git rev-list --count HEAD")
  .start()
  .apply { waitFor() }
  .inputStream.bufferedReader().readText().trim()

publishing {
  publications {
    create<MavenPublication>(rootProject.name) {
      from(components["java"])
    }
  }
  repositories {
    maven {
      url = URI("s3://maven.cruftbusters.com")
      credentials(AwsCredentials::class) {
        File("${System.getProperty("user.home")}/.aws/credentials")
          .readLines()
          .filter { it.contains(" = ") }
          .associate {
            it.split(" = ").run {
              assert(size == 2)
              Pair(get(0), get(1))
            }
          }
          .apply {
            accessKey = get("aws_access_key_id")
            secretKey = get("aws_secret_access_key")
          }
      }
    }
  }
}