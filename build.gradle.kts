plugins {
  id("java")
  id("co.uzzu.dotenv.gradle") version "4.0.0"
  kotlin("jvm") version "1.9.22"
}

/** Package data. */
group = "technology.idlab"
version = "0.0.1"

/** Use JUnit for testing. */
tasks.test { useJUnitPlatform() }

/** Set the Kotlin JVM version to 17. */
kotlin { jvmToolchain(17) }

/**
 * A list of all the repositories we use in the project. This includes the maven central repository
 * and the GitHub package repository.
 */
repositories {
  mavenCentral()

  maven {
    url = uri("https://maven.pkg.github.com/rdf-connect/jvm-runner")
    credentials {
      username = env.fetchOrNull("GITHUB_ACTOR") ?: System.getenv("GITHUB_ACTOR")
      password = env.fetchOrNull("GITHUB_TOKEN") ?: System.getenv("GITHUB_TOKEN")
    }
  }
}

dependencies {
  // JVM Runner.
  implementation("technology.idlab:jvm-runner:0.0.4")

  // Initialize testing.
  testImplementation("org.jetbrains.kotlin:kotlin-test")
}
