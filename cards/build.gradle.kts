plugins {
    id(BuildPlugins.androidLibrary)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinKapt)
    id(BuildPlugins.navigationSafeArgs)
    id(BuildPlugins.hiltAndroid)
}

android {
    namespace = "com.hatfat.cards"

    compileSdk = AndroidSdk.compile

    defaultConfig {
        minSdk = AndroidSdk.min

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    implementation(Libraries.glide)
    implementation(Libraries.gson)
    implementation(Libraries.material)
    implementation(Libraries.hiltAndroid)
    implementation(Libraries.viewPager2)
    implementation(Libraries.okhttpInterceptor)
    implementation(Libraries.recyclerview)
    implementation(Libraries.retrofit)
    implementation(Libraries.retrofitGson)
    implementation(Libraries.hiltNavigation)

    kapt(Libraries.hiltCompiler)

    testImplementation(TestLibraries.junit4)
    androidTestImplementation(TestLibraries.testRunner)
    androidTestImplementation(TestLibraries.espresso)
}
