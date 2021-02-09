@file:Suppress("MemberVisibilityCanBePrivate", "SpellCheckingInspection", "unused")

const val kotlinVersion = "1.3.40"
const val navigationVersion = "2.3.3"

object BuildPlugins {

    object Versions {
        const val buildToolsVersion = "4.1.2"
    }

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.buildToolsVersion}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val navigationSafeArgsGradlePlugin = "androidx.navigation:navigation-safe-args-gradle-plugin:${navigationVersion}"

    const val androidApplication = "com.android.application"
    const val androidLibrary = "com.android.library"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinAndroidExtensions = "kotlin-android-extensions"
    const val kotlinKapt = "kotlin-kapt"
    const val navigationSafeArgs = "androidx.navigation.safeargs.kotlin"
}

object AndroidSdk {
    const val min = 25
    const val compile = 30
    const val target = compile
}

object Libraries {
    private object Versions {
        const val jetpack = "1.2.0"
        const val constraintLayout = "2.0.4"
        const val ktx = "1.3.2"
        const val lifecycle = "2.2.0"
        const val coroutines = "1.3.9"
        const val gson = "2.8.6"
        const val glide = "4.11.0"
        const val material = "1.3.0"
    }

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
    const val ktxCore = "androidx.core:core-ktx:${Versions.ktx}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.jetpack}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"

    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${navigationVersion}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${navigationVersion}"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val gson = "com.google.code.gson:gson:${Versions.gson}"
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    const val material = "com.google.android.material:material:${Versions.material}"
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
