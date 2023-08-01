plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.library").version("7.4.2").apply(false)
    kotlin("multiplatform").version("1.8.10").apply(false)
    id("maven-publish")
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

afterEvaluate {
    publishing {
        publications{
            create<MavenPublication>("Maven") {
                groupId = "com.github.ezzatzizo"
                artifactId = "shopping-app-data-layer"
                version = "1.0.0"
            }
        }
    }
}


buildscript {
    repositories {
        mavenCentral() // Add the mavenCentral repository
        mavenLocal()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
        classpath("com.apollographql.apollo3:apollo-gradle-plugin:3.8.2")
    }
}



