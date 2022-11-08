
import java.nio.charset.Charset
import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlinx-serialization")
    id("dagger.hilt.android.plugin")
}

android {

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    signingConfigs {
        val propertiesFile = File("signing.properties")
        if (propertiesFile.exists()) {
            val properties = Properties()
            properties.load(propertiesFile.reader(Charset.forName("UTF-8")))
            create("release") {
                storeFile = file(properties.getProperty("KEYSTORE_FILE"))
                storePassword = properties.getProperty("KEYSTORE_PASSWORD")
                keyAlias = properties.getProperty("KEY_ALIAS")
                keyPassword = properties.getProperty("KEY_PASSWORD")
            }
        } else {
            create("release")
        }
    }

    buildTypes {
        getByName("debug") {
            isDefault = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
            buildConfigField("int", "MOCKSERVER_PORT", "8080")
            buildConfigField("String", "API_BASE_URL", "\"https://api.themoviedb.org/3/\"")
        }
        getByName("release") {
            isShrinkResources = true
            isMinifyEnabled = true
            isDebuggable = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("int", "MOCKSERVER_PORT", "8080")
            buildConfigField("String", "API_BASE_URL", "\"https://api.themoviedb.org/3/\"")
        }
        create("mockserver") {
            initWith(buildTypes.getByName("debug"))
            isDefault = false
            buildConfigField("String", "API_BASE_URL", "\"http://127.0.0.1:8080/\"")
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
        }
    }

    testOptions {
        unitTests.apply {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
        animationsDisabled = true
        emulatorSnapshots {
            // NOTE: enableTestFailures causes connectedAndroidTest CLI to hang
            // enableForTestFailures = true
            maxSnapshotsForTestFailures = 2
            compressSnapshots = false
        }
    }

    packagingOptions.apply {
        resources.excludes.addAll(
            listOf(
                "**/attach_hotspot_windows.dll",
                "META-INF/licenses/**",
                "META-INF/AL2.0",
                "META-INF/LGPL2.1"
            )
        )
    }
    namespace = "com.yasinkacmaz.jetflix"

    testBuildType = getTestBuildTypeOrDefault()
}

fun getTestBuildTypeOrDefault(): String =
    if (project.hasProperty("testBuildType")) {
        project.properties["testBuildType"].toString()
    } else "mockserver"

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(libs.bundles.androidX)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.accompanist)
    implementation(libs.bundles.io)
    implementation(libs.coil)
    implementation(libs.hilt)
    kapt(libs.hiltCompiler)

    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.bundles.androidTest)
    kaptAndroidTest(libs.hiltCompiler)
    implementation(libs.compose.uiTest.manifest)
    testImplementation(project(":lib:testing:unittest"))
    androidTestImplementation(project(":lib:testing:androidtest"))
}
