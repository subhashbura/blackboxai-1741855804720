plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.provizit.qrscanner"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.provizit.qrscanner"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    packaging {
        resources {
            excludes +=  "/META-INF/DEPENDENCIES"
            excludes +=  "/META-INF/LICENSE.txt"
            excludes +=  "/META-INF/LICENSE"
            excludes +=  "/META-INF/NOTICE.txt"
            excludes +=  "/META-INF/NOTICE"




         }
    }
}

dependencies {
    implementation (libs.zxing.android.embedded)
    implementation(libs.appcompat)
    implementation (libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation (libs.okhttp)
    implementation (libs.jopendocument)
    implementation (libs.glide)
    implementation (libs.httpclient)
    implementation (libs.httpcore)
    implementation (libs.jcifs.ng)


    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}