plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.21"
}

dependencies {
    val versionKotlinCoroutines = "1.6.4"

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$versionKotlinCoroutines")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:$versionKotlinCoroutines")
}