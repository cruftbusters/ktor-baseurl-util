import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
  kotlin("jvm") version "1.5.21"
  `java-library`
  `maven-publish`
  signing
  id("nebula.maven-resolved-dependencies") version "17.0.0"
}

group = "com.cruftbusters"

repositories {
  mavenCentral()
}

dependencies {
  api("io.ktor:ktor-client-core:+")
  api("io.ktor:ktor-client-jackson:+")
  api("io.ktor:ktor-server-netty:+")
  api("io.ktor:ktor-server-tests:+")
  testImplementation("io.kotest:kotest-runner-junit5-jvm:+")
}

tasks.withType<KotlinCompile> { kotlinOptions.jvmTarget = "11" }
tasks.test { useJUnitPlatform() }

version = ProcessBuilder("sh", "-c", "git rev-list --count HEAD")
  .start()
  .apply { waitFor() }
  .inputStream.bufferedReader().readText().trim()

java {
  withJavadocJar()
  withSourcesJar()
}

publishing {
  publications {
    create<MavenPublication>(rootProject.name) {
      from(components["java"])
      pom {
        name.set(rootProject.name)
        withXml {
          asNode().appendNode("description", "Ktor utilities")
        }
        url.set("https://github.com/cruftbusters/ktor-baseurl-util")
        licenses {
          license {
            name.set("The Apache License, Version 2.0")
            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
          }
        }
        scm {
          url.set("https://github.com/cruftbusters/ktor-baseurl-util")
        }
        developers {
          developer {
            id.set("Tyler")
            name.set("Tyler Johnson")
            email.set("tyler@cruftbusters.com")
          }
        }
      }
    }
  }
  repositories {
    maven {
      url = URI("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")
      credentials {
        username = project.properties["ossrhUsername"] as String?
        password = project.properties["ossrhPassword"] as String?
      }
    }
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

signing {
  useGpgCmd()
  sign(publishing.publications[rootProject.name])
}
