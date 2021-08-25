plugins {
  kotlin("jvm") version "1.5.21"
  `java-library`
}

repositories {
  mavenCentral()
}

dependencies {
  api("org.apache.commons:commons-math3:3.6.1")
  implementation("com.google.guava:guava:30.1.1-jre")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
  testImplementation("org.jetbrains.kotlin:kotlin-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}
