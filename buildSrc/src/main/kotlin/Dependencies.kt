@file:Suppress("MemberVisibilityCanBePrivate", "SpellCheckingInspection", "unused")

const val kotlinVersion = "1.4.32" //can't upgrade to kotlin 1.5 until hilt/dagger issue is fixed?
const val navigationVersion = "2.3.5"
const val hiltVersion = "2.37"
const val tripletPlayVersion = "3.5.0"

object BuildPlugins {
    object Versions {
        const val buildToolsVersion = "4.2.1"
    }

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.buildToolsVersion}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}"
    const val navigationSafeArgsGradlePlugin = "androidx.navigation:navigation-safe-args-gradle-plugin:${navigationVersion}"
    const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${hiltVersion}"
    const val tripletPlayPlugin = "com.github.triplet.gradle:play-publisher:${tripletPlayVersion}"

    const val androidApplication = "com.android.application"
    const val androidLibrary = "com.android.library"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinKapt = "kotlin-kapt"
    const val navigationSafeArgs = "androidx.navigation.safeargs.kotlin"
    const val hiltAndroid = "dagger.hilt.android.plugin"
    const val tripletPlay = "com.github.triplet.play"
}

object AndroidSdk {
    const val min = 22
    const val compile = 30
    const val target = compile
}

object Libraries {
    private object Versions {
        const val appCompat = "1.3.0"
        const val constraintLayout = "2.0.4"
        const val ktx = "1.5.0"
        const val lifecycle = "2.3.1"
        const val coroutines = "1.3.9"
        const val gson = "2.8.6"
        const val glide = "4.12.0"
        const val material = "1.3.0"
        const val okhttp = "4.9.1"
        const val recyclerviewVersion = "1.2.1"
        const val retrofit = "2.9.0"
        const val viewPager2 = "1.1.0-alpha01"
    }

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:${kotlinVersion}"
    const val ktxCore = "androidx.core:core-ktx:${Versions.ktx}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycleLiveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${navigationVersion}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${navigationVersion}"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val gson = "com.google.code.gson:gson:${Versions.gson}"
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val okhttpInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
    const val hiltAndroid = "com.google.dagger:hilt-android:${hiltVersion}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${hiltVersion}"
    const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerviewVersion}"

    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitGson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val viewPager2 = "androidx.viewpager2:viewpager2:${Versions.viewPager2}"
}

object TestLibraries {
    private object Versions {
        const val junit4 = "4.12"
        const val junitExt = "1.1.2"
        const val espresso = "3.3.0"
    }

    const val junit4 = "junit:junit:${Versions.junit4}"
    const val testRunner = "androidx.test.ext:junit:${Versions.junitExt}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
}
