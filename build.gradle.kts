plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "it.unicam.cs.mpgc"
version = "1.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

// I sorgenti contengono lettere accentate: si compila sempre in UTF-8,
// qualunque sia la codifica predefinita del sistema.
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

repositories {
    mavenCentral()
}

javafx {
    version = "25"
    modules = listOf("javafx.controls")
}

dependencies {
    implementation("com.google.code.gson:gson:2.14.0")

    testImplementation(platform("org.junit:junit-bom:6.1.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass = "it.unicam.cs.mpgc.rpg122629.senzaritorno.App"
    // JavaFX carica librerie native: senza questa opzione, dal JDK 24 in poi
    // all'avvio compare un avviso di "restricted method" (e in futuro un errore).
    applicationDefaultJvmArgs = listOf("--enable-native-access=javafx.graphics")
}

tasks.test {
    useJUnitPlatform()
}
