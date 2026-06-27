import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("maven-publish")
    id("signing")
}

group = "dev.ishant"
version = "1.0.0"

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

android {
    namespace = "dev.ishant.easyniddle"
    compileSdk = 37

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    // Navigation
    implementation(libs.navigation.compose)

    // Navigation 3
    implementation(libs.navigation3.runtime)
    implementation(libs.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "dev.ishant"
                artifactId = "easyniddle"
                version = "1.0.0"

                pom {
                    name.set("EasyNiddle")
                    description.set("A lightweight DSL-based navigation library for Jetpack Compose")
                    url.set("https://github.com/Kratos1996/EasyNiddle")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("ishant")
                            name.set("Ishant")
                            email.set("ishant.sharma1947@gmail.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:github.com/Kratos1996/EasyNiddle.git")
                        developerConnection.set("scm:git:ssh://github.com/Kratos1996/EasyNiddle.git")
                        url.set("https://github.com/Kratos1996/EasyNiddle")
                    }
                }
            }
        }
        repositories {
            maven {
                name = "OSSRH"
                val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
                credentials {
                    username = localProperties.getProperty("mavenCentralUsername")
                    password = localProperties.getProperty("mavenCentralPassword")
                }
            }
        }
    }

    signing {
        val keyId = localProperties.getProperty("signing.keyId")
        val password = localProperties.getProperty("signing.password")
        val secretKey = localProperties.getProperty("signing.secretKey") ?: ""
        
        if (keyId != null && password != null && secretKey.isNotEmpty()) {
            useInMemoryPgpKeys(keyId, secretKey, password)
            sign(publishing.publications["release"])
        }
    }
}
