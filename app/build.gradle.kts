plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "com.alfabank.homework.courseproject"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.alfabank.homework.courseproject"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    val paging_version = "3.4.1"

    implementation("androidx.browser:browser:1.7.0")

    implementation("androidx.paging:paging-runtime:$paging_version")

    implementation("androidx.room:room-paging:2.8.2")

    // optional - Jetpack Compose integration
    implementation("androidx.paging:paging-compose:3.4.1")

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Networking - Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.serialization)

    // JSON
    implementation(libs.kotlinx.serialization.json)

    // HTTP Client
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    //Hilt
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Image Loading
    implementation(libs.coil.compose)
    implementation("com.google.accompanist:accompanist-pager:0.35.0-alpha")
    implementation("androidx.compose.material3:material3:1.3.0")


    //Room
    implementation(libs.room)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Яндекс MapKit
    implementation(libs.yandex.mapskit)
}