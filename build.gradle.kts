plugins {
    id("application")
    id("org.jetbrains.kotlin.jvm") version "1.7.21"
    id("org.openjfx.javafxplugin") version "0.0.13"
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("org.mrlem.zikey.MainKt")
}

javafx {
    version = "19"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    val versionKotlinCoroutines = "1.6.4"

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$versionKotlinCoroutines")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:$versionKotlinCoroutines")
}