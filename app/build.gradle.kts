import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android") // Hilt plugin
    kotlin("kapt")
    id("com.google.gms.google-services")
}

// --- Auto-versioning -------------------------------------------------------
// versionCode is stored in version.properties and bumped only when a release
// task (assembleRelease / bundleRelease / installRelease) is in the build.
// versionName is derived as "1.0.<versionCode>".
val versionPropsFile = file("version.properties")
val versionProps = Properties().apply {
    versionPropsFile.inputStream().use { load(it) }
}
val appVersionCode = run {
    val baseCode = versionProps.getProperty("versionCode").trim().toInt()
    val ciRunNumber = System.getenv("GITHUB_RUN_NUMBER")?.toIntOrNull()
    when {
        // CI (GitHub Actions): monotonic code derived from the run number,
        // always above the file baseline. Nothing is written back to the repo.
        ciRunNumber != null -> baseCode + ciRunNumber
        // Local release build: bump version.properties and use the new value.
        gradle.startParameter.taskNames.any { it.contains("Release") } -> {
            val bumped = baseCode + 1
            versionProps.setProperty("versionCode", bumped.toString())
            versionPropsFile.outputStream().use { versionProps.store(it, null) }
            bumped
        }
        // Debug / non-release local build: use the file value as-is.
        else -> baseCode
    }
}

android {
    namespace = "com.dhethi.jntuhconnect"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.dhethi.jntuhconnect"
        minSdk = 24
        targetSdk = 36
        versionCode = appVersionCode
        versionName = "1.0.$appVersionCode"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            // Populated from environment variables in CI (GitHub Actions).
            // Left unconfigured for local builds unless KEYSTORE_FILE is set.
            System.getenv("KEYSTORE_FILE")?.let { keystorePath ->
                storeFile = file(keystorePath)
                storePassword = System.getenv("KEYSTORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true

            // Only sign with the release key when a keystore is provided (CI).
            if (System.getenv("KEYSTORE_FILE") != null) {
                signingConfig = signingConfigs.getByName("release")
            }

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            ndk {
                debugSymbolLevel = "FULL" // options: NONE, SYMBOL_TABLE, FULL
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.animation.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("androidx.constraintlayout:constraintlayout-compose-android:1.1.1")
    implementation("androidx.compose.material:material-icons-extended-android:1.7.8")
    implementation("androidx.navigation:navigation-compose-android:2.9.5")
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("androidx.datastore:datastore-preferences:1.1.7")
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("androidx.browser:browser:1.9.0")
    implementation("com.google.dagger:hilt-android:2.57.2")
    kapt("com.google.dagger:hilt-android-compiler:2.57.2")

// (optional) For Hilt with instrumentation tests
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.57.2")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.57.2")

// (optional) For local unit tests
    testImplementation("com.google.dagger:hilt-android-testing:2.57.2")
    kaptTest("com.google.dagger:hilt-android-compiler:2.57.2")

    implementation("androidx.hilt:hilt-navigation-compose:1.3.0")
    implementation("androidx.room:room-runtime:2.8.1")
    kapt("androidx.room:room-compiler:2.8.1")       // annotation processor
    implementation("androidx.room:room-ktx:2.8.1")


    implementation("com.google.firebase:firebase-messaging:24.0.0")

    // Google Play In-App Updates
    implementation(libs.app.update)

}