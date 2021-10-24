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

configurations.all {
  resolutionStrategy {
    activateDependencyLocking()
    componentSelection
      .all(object : Action<ComponentSelection> {
        @Mutate
        override fun execute(selection: ComponentSelection) {
          val version = selection.candidate.version
          when {
            version.matches(
              Regex(
                ".*[-.]rc\\d*$",
                RegexOption.IGNORE_CASE
              )
            ) -> selection.reject("Release candidates are excluded")
            version.matches(Regex(".*[-.]M\\d+$")) -> selection.reject("Milestones are excluded")
            version.matches(Regex(".*-alpha\\d+$")) -> selection.reject("Alphas are excluded")
            version.matches(Regex(".*-beta\\d+$")) -> selection.reject("Betas are excluded")
          }
        }
      })
  }
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
  }
}

signing {
  useGpgCmd()
  sign(publishing.publications[rootProject.name])
}
