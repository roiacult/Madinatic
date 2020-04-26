plugins{
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android{
    compileSdkVersion(Versions.AndroidConfigs.compileSdk)

    defaultConfig {
        minSdkVersion(Versions.AndroidConfigs.minSdkVersion)
        targetSdkVersion(Versions.AndroidConfigs.targetSdk)
        versionCode = Versions.AndroidConfigs.versionCode
        versionName  = Versions.AndroidConfigs.appVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes{
        getByName("release"){
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility =JavaVersion.VERSION_1_8
    }

    configurations.all{
        resolutionStrategy.eachDependency {
            if(this.requested.group == "androidx.arch.core" &&
                    !this.requested.name.contains("core-runtime")
            ){
                this.useVersion("2.0.1")
            }
        }
    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":domain"))
    implementation(Dependencies.Kotlin.jre)
    implementation(Dependencies.Kotlin.Coroutine.core)
    implementation(Dependencies.Kotlin.Coroutine.android)
    implementation(Dependencies.Kotlin.Coroutine.rx2)
    implementation(Dependencies.AndroidJetpack.paging)
    implementation(Dependencies.timber)

    implementation(Dependencies.Jstarter.domain)

    implementation(Dependencies.Koin.core)
    implementation(Dependencies.Koin.scope)
    implementation(Dependencies.Koin.viewModel)
    implementation(Dependencies.Kotlin.reflect)


    implementation(Dependencies.Retrofit.core)
    implementation(Dependencies.Retrofit.gson)
    implementation(Dependencies.Retrofit.gson_converter)
    implementation(Dependencies.Retrofit.logging)
    implementation(Dependencies.Retrofit.mockWebServer)
    implementation(Dependencies.Retrofit.okhttp3)
    implementation(Dependencies.Retrofit.rxJava)
    implementation(Dependencies.Retrofit.scalarConverter)
    implementation(Dependencies.Retrofit.stetho)

    implementation(Dependencies.Room.runtime)
    implementation(Dependencies.Room.room_ktx)
    implementation(Dependencies.Room.rxJava)
    kapt(Dependencies.Room.compiler)

    implementation(Dependencies.RxJava.core)
    implementation(Dependencies.RxJava.reactiveNetwork)
    implementation(Dependencies.RxJava.rxAndroid)
    implementation(Dependencies.RxJava.rxKotlin)
    implementation(Dependencies.RxJava.rxPreference)

    testImplementation(Dependencies.Test.kotlin_junit)
    testImplementation(Dependencies.Test.junit)
    testImplementation(Dependencies.Test.mockk)
    testImplementation(Dependencies.Test.roboelectric)
    testImplementation(Dependencies.Test.hamcrest)
    testImplementation(Dependencies.Test.kotlinCoroutineTest)
    testImplementation(Dependencies.Test.kotlinCoroutineRule)
    testImplementation(Dependencies.Test.kotlin_junit)
    testImplementation(Dependencies.Test.kotlin_mockito)
    testImplementation(Dependencies.Test.androidArchCore)
    testImplementation(Dependencies.Test.kotlinCoroutineTest)
    testImplementation(Dependencies.Test.kotlinCoroutineRule)

    androidTestImplementation(Dependencies.Test.androidTruthExt)
    androidTestImplementation(Dependencies.Test.androidRunner)
    androidTestImplementation(Dependencies.Test.androidJunit)
    androidTestImplementation(Dependencies.Test.androidCore)
    androidTestImplementation(Dependencies.Test.androidRules)
    androidTestImplementation(Dependencies.Test.androidArchCore)
    androidTestImplementation(Dependencies.Test.androidRunner)
}