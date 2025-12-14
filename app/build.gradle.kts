import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    kotlin("kapt")
}

// local.properties 파일에서 민감한 키 읽기
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

// local.properties에서 키 가져오기 (없으면 빈 문자열)
val nativeAppKey = localProperties.getProperty("NATIVE_APP_KEY", "")
val kakaoLatestVersion = localProperties.getProperty("KAKAO_LATEST_VERSION", "2.23.1")

android {
    namespace = "com.example.devmart"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.example.devmart"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "BASE_URL", "\"http://coda-dev-env.eba-pdmadfde.ap-northeast-2.elasticbeanstalk.com\"")
        
        // BuildConfig에 키 추가 (코드에서 사용 가능)
        buildConfigField("String", "NATIVE_APP_KEY", "\"$nativeAppKey\"")
        
        // AndroidManifest.xml에서 사용할 수 있도록 manifestPlaceholders 설정
        manifestPlaceholders["NATIVE_APP_KEY"] = nativeAppKey
    }
    buildFeatures { compose = true; buildConfig = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // ✅ Kotlin 17로 통일
    kotlinOptions {
        jvmTarget = "17"
    }
    packaging { resources.excludes += "/META-INF/{AL2.0,LGPL2.1}" }
}

// AndroidTest 빌드 타입에서 KAPT 태스크 비활성화 (Hilt를 사용하지 않으므로)
afterEvaluate {
    tasks.findByName("kaptDebugAndroidTestKotlin")?.apply {
        enabled = false
    }
    tasks.findByName("kaptReleaseAndroidTestKotlin")?.apply {
        enabled = false
    }
    // 모든 AndroidTest 관련 kapt 태스크 비활성화
    tasks.matching { it.name.startsWith("kapt") && it.name.contains("AndroidTest") }.configureEach {
        enabled = false
    }
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.navigation)
    implementation(libs.activity.compose)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.hilt.navigation.compose)

    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.datastore)
    
    // Material Icons
    implementation(libs.material.icons.extended)
    
    // Coil for image loading
    implementation(libs.coil.compose)
    
    // AndroidTest dependencies
    androidTestImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    //kakao login
    implementation("com.kakao.sdk:v2-user:$kakaoLatestVersion")
}
