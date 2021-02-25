plugins {
    id(BuildPlugins.androidLibrary)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinKapt)
    id(BuildPlugins.navigationSafeArgs)
    id(BuildPlugins.hiltAndroid)
}

android {
    compileSdkVersion(AndroidSdk.compile)

    defaultConfig {
        minSdkVersion(AndroidSdk.min)
        targetSdkVersion(AndroidSdk.target)
        versionCode = Config.versionCode
        versionName = Config.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isDebuggable = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName("release") {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.ktxCore)
    implementation(Libraries.appCompat)
    implementation(Libraries.constraintLayout)
    implementation(Libraries.lifecycleLiveData)
    implementation(Libraries.lifecycleViewModel)
    implementation(Libraries.navigationFragment)
    implementation(Libraries.navigationUi)
    implementation(Libraries.coroutinesCore)
    implementation(Libraries.coroutinesAndroid)
    implementation(Libraries.gson)
    implementation(Libraries.material)
    implementation(Libraries.hiltAndroid)
    implementation(Libraries.viewPager)
    implementation(Libraries.okhttpInterceptor)
    implementation(Libraries.recyclerview)
    implementation(Libraries.retrofit)
    implementation(Libraries.retrofitGson)

    kapt(Libraries.hiltCompiler)

    testImplementation(TestLibraries.junit4)
    androidTestImplementation(TestLibraries.testRunner)
    androidTestImplementation(TestLibraries.espresso)
}
