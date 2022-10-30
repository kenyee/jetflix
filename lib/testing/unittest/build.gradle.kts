plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = Config.javaVersion
    targetCompatibility = Config.javaVersion
}

dependencies {
    implementation(libs.junit)
    implementation(libs.jsonSerialization)
    implementation(libs.coroutinesTest)
}
