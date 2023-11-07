plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.emergencyguardian"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.emergencyguardian"
        minSdk = 26
        targetSdk = 33
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
}

dependencies {

    implementation ("androidx.appcompat:appcompat:1.4.0")
    implementation ("com.android.support.constraint:constraint-layout:1.1.3")
    implementation ("com.google.android.material:material:1.4.0")
    implementation("com.android.support:design:34.0.0")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    testImplementation ("junit:junit:4.12")
    androidTestImplementation ("com.android.support.test:runner:1.0.2")
    androidTestImplementation ("com.android.support.test.espresso:espresso-core:3.0.2")
}