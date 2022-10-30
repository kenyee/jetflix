plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.yasinkacmaz.jetflix.testing.androidtest"
    compileSdk = Config.compileSdk

    defaultConfig {
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
        debug {
            isMinifyEnabled = false
        }
        // NOTE: you must have matching variants w/ the app module or this module doesn't get loaded
        create("mockserver") {
            initWith(buildTypes.getByName("debug"))
            isDefault = false
        }
    }
    compileOptions {
        sourceCompatibility = Config.javaVersion
        targetCompatibility = Config.javaVersion
    }
    kotlinOptions {
        jvmTarget = Config.javaVersion.toString()
        allWarningsAsErrors = true
    }
}

dependencies {
    implementation(libs.okhttp.mockserver)
    implementation(libs.androidX.testrunner)
    implementation(libs.compose.uiTest)
    implementation(libs.compose.uiTestJunit)
}
