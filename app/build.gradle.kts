import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.servly_app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.servly_app"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        
        buildConfigField(type = "String", name = "DEFAULT_WEB_CLIENT_ID", value = "\"${properties.getProperty("DEFAULT_WEB_CLIENT_ID")}\"")
        buildConfigField(type = "String", name = "SERVER_URL", value = "\"${properties.getProperty("SERVER_URL")}\"")
        buildConfigField(type = "String", name = "PLACES_API_KEY", value = "\"${properties.getProperty("PLACES_API_KEY")}\"")
        buildConfigField(type = "String", name = "STRIPE_PUBLIC_KEY", value = "\"${properties.getProperty("STRIPE_PUBLIC_KEY")}\"")

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
//    BASE
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

//    COMMUNICATION RETROFIT
    implementation(libs.retrofit)

//    GSON CONVERTER
    implementation(libs.gson)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.okhttp)

//    NAVIGATION
    implementation(libs.androidx.navigation.compose)

//    FIREBASE
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging.ktx)

//    AUTH AND CREDENTIALS
    implementation(libs.firebase.auth)
    implementation(libs.play.services.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

//    GOOGLE
    implementation(libs.places.v410)

//    PAYMENTS
    implementation(libs.stripe.android)

//    DAGGER HILT
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

kapt {
    correctErrorTypes = true
}
