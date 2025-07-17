buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        // Don't put this in Dependencies, otherwise Android Studio has trouble automatic upgrades.
        classpath("com.android.tools.build:gradle:8.11.1")
        classpath(BuildPlugins.kotlinGradlePlugin)
        classpath(BuildPlugins.navigationSafeArgsGradlePlugin)
        classpath(BuildPlugins.hiltGradlePlugin)
        classpath(BuildPlugins.tripletPlayPlugin)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clear").configure {
    delete("build")
}
