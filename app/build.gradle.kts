plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.carcrashproject_v20_10112024"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.carcrashproject_v20_10112024"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.navigation.ui.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.play.services.location)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.annotation)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}