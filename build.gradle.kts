plugins {
  id("java")
  id("co.uzzu.dotenv.gradle") version "4.0.0"
}

/** Package data. */
group = "technology.idlab"
version = "0.0.1"

/** Use JUnit for testing. */
tasks.test { useJUnitPlatform() }

repositories {
  mavenCentral()

  maven {
    url = uri("https://maven.pkg.github.com/rdf-connect/jvm-runner")
    credentials {
      username = env.GITHUB_ACTOR.value
      password = env.GITHUB_TOKEN.value
    }
  }
}

dependencies {
  // JVM Runner
  implementation("technology.idlab:jvm-runner:0.0.2")

  // JUnit
  testImplementation(platform("org.junit:junit-bom:5.10.0"))
  testImplementation("org.junit.jupiter:junit-jupiter")
}
