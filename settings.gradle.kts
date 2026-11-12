plugins {
    // Permette a Gradle di scaricare il JDK richiesto dalla toolchain (Java 25)
    // quando non è installato, così "./gradlew build" funziona su qualsiasi computer.
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "senza-ritorno"
