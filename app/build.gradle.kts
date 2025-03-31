plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.sellapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.sellapp"
        minSdk = 27
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
    buildFeatures{
        viewBinding = true
        dataBinding = true
    }
}



dependencies {
    androidTestImplementation (libs.junit.v121) // Bản mới nhất
    androidTestImplementation (libs.espresso.core.v361)
    configurations.all {
        resolutionStrategy {
            force ("androidx.annotation:annotation:1.6.0")
        }
    }
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.support.annotations)
    implementation(libs.annotation.jvm)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    //RxJava
    implementation (libs.rxandroid)
    implementation (libs.rxjava)
    // Retrofit
    implementation (libs.squareup.adapter.rxjava3)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.rxjava3.retrofit.adapter)
    implementation("com.github.bumptech.glide:glide:4.16.0@aar"){
        isTransitive = true
    }
    //brade
    implementation (libs.notification.badge)
    implementation(libs.eventbus)
    implementation (libs.paperdb)
}