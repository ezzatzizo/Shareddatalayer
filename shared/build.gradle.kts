import com.android.build.gradle.internal.scope.publishArtifactToConfiguration

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("com.apollographql.apollo3")
}

kotlin {

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        framework {
            baseName = "shareddatalayer"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.apollographql.apollo3:apollo-api:3.8.2")
                implementation("com.apollographql.apollo3:apollo-runtime:3.8.2")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                api( "io.insert-koin:koin-core:3.4.2")
            }

        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}


android {
    namespace = "eu.coolblue.shop.shared"
    compileSdk = 33
    defaultConfig {
        minSdk = 23
    }
}

apollo{
    packageName.set("eu.coolblue.shop.sharedatalayer.shared.commanMain.graphql")
    generateApolloMetadata.set(true)
    mapScalar("Uri", "eu.coolblue.shop.shared.remote.model.SharedUriModel")
    mapScalar("DateTime", "kotlinx.datetime.LocalDateTime")
    mapScalarToUpload("Upload")
}
