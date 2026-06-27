import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.maven.publish)
}

group = "io.github.kratos1996"
version = "1.0.1"

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates("io.github.kratos1996", "easyniddle", "1.0.1")

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

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "consumer-rules.pro"
            )
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
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

    api(libs.navigation3.runtime)
    api(libs.navigation3.ui)
    api(libs.androidx.lifecycle.viewmodel.navigation3)
    api(libs.kotlinx.serialization.json)
}
