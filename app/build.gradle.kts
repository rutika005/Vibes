plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
}
android {
    namespace = "com.example.vibes"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.vibes"
        minSdk = 28
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}
dependencies {

    implementation(libs.play.services.auth)

    implementation(libs.material.v160)
    implementation(libs.car.ui.lib.v250)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.car.ui.lib)

    //firebase authentication
    implementation(libs.firebase.auth)
    implementation(libs.firebase.auth.ktx)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.database.v2005)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.dynamic.links)

    //facebook auth
    implementation(libs.facebook)
    implementation(libs.google.firebase.auth)


    //retrofit
    implementation(libs.retrofit)

    //gson converter
    implementation(libs.converter.gson)

    //picasso for converting image link to API
    implementation(libs.picasso)

    //Add Firebase Authentication
    implementation(libs.google.firebase.auth.ktx)
    //Add Google Sign-In
    implementation(libs.play.services.auth.v2060)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    //convert images url to images
    androidTestImplementation(libs.androidx.espresso.core)
}