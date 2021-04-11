buildscript {
    repositories {
        google()
        jcenter()
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
        jcenter()
    }
}

tasks.register("clear").configure {
    delete("build")
}
