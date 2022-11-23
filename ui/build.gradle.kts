plugins {
    id("application")
    id("org.jetbrains.kotlin.jvm") version "1.7.21"
    id("org.openjfx.javafxplugin") version "0.0.13"
}

application {
    mainClass.set("org.mrlem.zikey.MainKt")
}

javafx {
    version = "19"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(project(":core"))
}