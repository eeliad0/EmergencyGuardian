plugins {
    id("com.android.application") // Apply the Android application plugin
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") // Apply the google maps plugin
    id("com.google.gms.google-services") // Apply the Google services Gradle plugin
}

android {
    namespace = "com.example.emergencyguardian" // Define the package namespace
    // Configure Android SDK and build settings
    compileSdk = 33 // Compile against Android SDK version 33

    /**
     * WARNING: In general, do not attempt to change the versions of either the SDK or packages without
     * first confirming their compatibility. Not all versions are compatible with every package
     * or SDK, and simply using the newest version does not guarantee compatibility.
     */
    defaultConfig {
        applicationId = "com.example.emergencyguardian" // Package name of the application
        minSdk = 26 // Minimum supported Android version (API level)
        targetSdk = 33 // Target Android version
        versionCode = 1 // Version code for the app
        versionName = "1.0" // Version name for the app

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // Test runner for unit tests
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Disable code minification for release builds
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            ) // Configuration for ProGuard, a code shrinker and obfuscator
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8 // Source code compatibility (Java 8)
        targetCompatibility = JavaVersion.VERSION_1_8 // Target compatibility (Java 8)
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    packagingOptions {
        exclude ("META-INF/DEPENDENCIES");
    }
}

dependencies {

    // AndroidX AppCompat library
    implementation("androidx.appcompat:appcompat:1.6.1")
    // Material Design components
    implementation("com.google.android.material:material:1.9.0")
    // Google Play services for location [This wasn't added by default]
    implementation("com.google.android.gms:play-services-location:21.0.1")
    // Google Play services for maps [This wasn't added by default]
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    // ConstraintLayout
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    // Navigation fragment
    implementation("androidx.navigation:navigation-fragment:2.5.3")
    // Navigation UI components
    implementation("androidx.navigation:navigation-ui:2.5.3")

    // LiveData and Kotlin Extensions for data observability
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.0")
    // ViewModel and Kotlin Extensions for managing UI-related data
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0")

    // Camera components
    implementation("androidx.camera:camera-camera2:1.3.0-alpha04")
    implementation("androidx.camera:camera-lifecycle:1.3.0-alpha04")
    implementation("androidx.camera:camera-view:1.3.0-alpha04")
    implementation("androidx.camera:camera-extensions:1.3.0-alpha04")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))

    // Call API Package
    implementation("com.google.android.gms:play-services-cronet:18.0.1")
    // Display image package
    implementation("com.squareup.picasso:picasso:2.8")

    // Firebase Packages
    // Cloud Firebase Packages
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-firestore")

    implementation("com.google.firebase:firebase-analytics:21.3.0")
    implementation("com.google.firebase:firebase-messaging:23.2.1")
    implementation("com.google.firebase:firebase-storage:20.2.1")
    implementation ("com.google.firebase:firebase-database:20.0.3")
    implementation ("com.google.firebase:firebase-auth:21.0.1")
    implementation("androidx.cardview:cardview:1.0.0")

    implementation("com.google.apis:google-api-services-youtube:v3-rev222-1.25.0")
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")

    // JUnit for unit testing
    testImplementation("junit:junit:4.13.2")
    // AndroidX JUnit extensions
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    // Espresso for UI testing
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}