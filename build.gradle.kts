buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(BuildPlugins.androidGradlePlugin)
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
