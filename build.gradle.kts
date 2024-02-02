buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        // Don't put this in Dependencies, otherwise Android Studio has trouble automatic upgrades.
        classpath("com.android.tools.build:gradle:8.2.2")
        classpath(BuildPlugins.kotlinGradlePlugin)
        classpath(BuildPlugins.navigationSafeArgsGradlePlugin)
        classpath(BuildPlugins.hiltGradlePlugin)
        classpath(BuildPlugins.tripletPlayPlugin)
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
