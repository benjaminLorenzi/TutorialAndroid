plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.example.tutorialandroid"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.tutorialandroid"
        minSdk = 24
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
            signingConfig = signingConfigs.getByName("debug")
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
        // Active la creation d'un classe BuildConfig
        buildConfig = true
    }

    flavorDimensions += "environment"

    productFlavors {
        // Flavor de développement (Localhost)
        create("dev") {
            dimension = "environment"
            // Le package de l'app devient com.example.app.dev
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:3010/\"")
        }
        // Flavor de développement (Vrai serveur)
        create("prod") {
            dimension = "environment"
            buildConfigField("String", "API_BASE_URL", "\"https://jsonplaceholder.typicode.com/\"")
        }
    }
}


dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.navigation:navigation-compose:2.8.0")

    // RetroFit
    val retrofitVersion = "2.11.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")

    // Moshi
    var moshiKotlinVersion = "1.15.1"
    implementation("com.squareup.moshi:moshi-kotlin:$moshiKotlinVersion")
    implementation("com.squareup.moshi:moshi-kotlin:$moshiKotlinVersion")

    // Http (client + logs réseau)
    var httpVersion = "4.12.0"
    implementation("com.squareup.okhttp3:okhttp:$httpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$httpVersion")

    val roomVersion = "2.8.3"

    implementation("androidx.room:room-runtime:$roomVersion")
// Extensions Kotlin + coroutines (suspend, Flow, etc.)
    implementation("androidx.room:room-ktx:$roomVersion")
// Compilateur d'annotations (génère le code Room)
    kapt("androidx.room:room-compiler:$roomVersion")
}