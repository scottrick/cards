buildscript {
    val kotlin_version by extra("1.4.30")
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath(BuildPlugins.androidGradlePlugin)
        classpath(BuildPlugins.kotlinGradlePlugin)
        classpath(BuildPlugins.navigationSafeArgsGradlePlugin)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
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
