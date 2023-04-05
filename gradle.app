plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-kapt'
    id 'com.google.gms.google-services'
}

android {
    namespace 'com.bluest.secureestate'
    compileSdk 33

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    defaultConfig {
        applicationId "com.bluest.secureestate"
        minSdk 27
        targetSdk 33
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {
        release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = '1.8'
    }
}

dependencies {
    implementation 'androidx.core:core-ktx:1.9.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.firebase:firebase-auth:21.2.0'
    implementation 'com.google.android.gms:play-services-maps:18.1.0'
    implementation 'com.google.firebase:firebase-analytics-ktx'
    kapt "com.android.databinding:compiler:3.2.0-alpha10"
    implementation 'com.google.android.material:material:1.8.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    implementation 'com.google.android.gms:play-services-auth:20.4.1'
    implementation 'com.github.bumptech.glide:glide:4.14.2'
    kapt 'com.github.bumptech.glide:compiler:4.14.2'
    implementation platform('com.google.firebase:firebase-bom:31.3.0')
    implementation 'pl.droidsonroids.gif:android-gif-drawable:1.2.19'
    implementation 'com.airbnb.android:lottie:3.4.4'
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1'
    implementation 'nl.joery.animatedbottombar:library:1.1.0'
}