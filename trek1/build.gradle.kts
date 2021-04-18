plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinKapt)
    id(BuildPlugins.hiltAndroid)
    id(BuildPlugins.tripletPlay)
}

val keystore = Keystore(rootProject)

android {
    compileSdkVersion(AndroidSdk.compile)

    defaultConfig {
        applicationId = "com.hatfat.trek1"

        minSdkVersion(AndroidSdk.min)
        targetSdkVersion(AndroidSdk.target)
        versionCode = Config.versionCode
        versionName = Config.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        register("release").configure {
            storeFile = file("trek1_keystore.jks")
            storePassword = keystore.properties["storePassword"] as String
            keyAlias = keystore.properties["trek1KeyAlias"] as String
            keyPassword = keystore.properties["keyPassword"] as String
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            signingConfig = signingConfigs.getByName("release")
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

play {
    serviceAccountCredentials.set(file("../cards-play-console-api-access.json"))
    track.set("beta")
    promoteTrack.set("production")
}

dependencies {
    implementation(project(":cards"))

    implementation(Libraries.appCompat)
    implementation(Libraries.constraintLayout)
    implementation(Libraries.glide)
    implementation(Libraries.gson)
    implementation(Libraries.hiltAndroid)
    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.ktxCore)
    implementation(Libraries.lifecycleViewModel)
    implementation(Libraries.okhttpInterceptor)
    implementation(Libraries.recyclerview)
    implementation(Libraries.retrofit)
    implementation(Libraries.retrofitGson)

    kapt(Libraries.glideCompiler)
    kapt(Libraries.hiltCompiler)

    testImplementation(TestLibraries.junit4)
    androidTestImplementation(TestLibraries.testRunner)
    androidTestImplementation(TestLibraries.espresso)
}