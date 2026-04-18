import java.net.URI
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

val localProperties = Properties().also { props ->
    val file = rootProject.file("local.properties")
    if (file.exists()) props.load(file.inputStream())
}

fun getProp(name: String, required: Boolean = true): String? {
    val value = (System.getenv(name) ?: localProperties.getProperty(name))?.trim()
    if (required && value.isNullOrBlank()) {
        throw GradleException(
            "\n\n❌ Propriedade obrigatória ausente: '$name'" +
            "\n   → Defina como variável de ambiente ou em local.properties\n"
        )
    }
    return value?.ifBlank { null }
}

fun validateBaseUrl(url: String, propName: String) {
    val uri = runCatching { URI(url) }.getOrElse {
        throw GradleException("❌ '$propName' não é uma URL válida: $url")
    }
    if (uri.scheme !in listOf("http", "https")) {
        throw GradleException("❌ '$propName' deve começar com http:// ou https:// — valor atual: $url")
    }
    if (!url.endsWith("/")) {
        throw GradleException("❌ '$propName' deve terminar com '/' — valor atual: $url")
    }
}

android {
    namespace = "br.com.claus.sellvia"
    compileSdk = 36

    buildFeatures {
        buildConfig = true
        compose = true
    }

    defaultConfig {
        applicationId = "br.com.claus.sellvia"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.2"
    }

    signingConfigs {
        create("release") {
            storeFile = rootProject.file("sellvia_app_key.jks")
            storePassword = getProp("KEYSTORE_PASSWORD")
            keyAlias = getProp("KEY_ALIAS")
            keyPassword = getProp("KEY_PASSWORD")
        }
    }

    buildTypes {
        debug {
            val baseUrl = getProp("BASE_URL", required = false) ?: "http://10.0.2.2:8080/"
            validateBaseUrl(baseUrl, "BASE_URL")
            buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
        }

        release {
            val baseUrl = getProp("BASE_URL")!!
            validateBaseUrl(baseUrl, "BASE_URL")
            buildConfigField("String", "BASE_URL", "\"$baseUrl\"")

            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("androidx.compose.material:material-icons-extended")
    implementation("io.insert-koin:koin-android:3.5.3")
    implementation("io.insert-koin:koin-androidx-compose:3.5.3")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.4")
}
