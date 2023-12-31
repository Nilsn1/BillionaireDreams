plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.nilscreation.billionairedreams"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.nilscreation.billionairedreams"
        minSdk = 24
        targetSdk = 33
        versionCode = 2
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-analytics-ktx:21.3.0")
    implementation("com.google.android.gms:play-services-ads:22.3.0")
    implementation("com.google.firebase:firebase-messaging-ktx:23.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
//    implementation("com.github.daoibrahim:AdmobAdvancedNativeRecyclerview:1.0.0")
//    implementation("com.github.chrisbanes:PhotoView:2.3.0")

}