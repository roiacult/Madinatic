plugins{
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

kapt{
    correctErrorTypes = true
}

android{
    compileSdkVersion(Versions.AndroidConfigs.compileSdk)

    defaultConfig {
        applicationId = Versions.AndroidConfigs.applicationId
        minSdkVersion(Versions.AndroidConfigs.minSdkVersion)
        targetSdkVersion(Versions.AndroidConfigs.targetSdk)
        versionCode = Versions.AndroidConfigs.versionCode
        versionName  = Versions.AndroidConfigs.appVersion
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables{
            useSupportLibrary = true
        }

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
    dataBinding {
        isEnabled = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(Dependencies.AndroidJetpack.activity)
    implementation(Dependencies.AndroidJetpack.viewPager2)
    implementation(Dependencies.AndroidJetpack.activity_ktx)
    implementation(Dependencies.AndroidJetpack.appcompat)
    implementation(Dependencies.AndroidJetpack.appcompat_ressource)
    implementation(Dependencies.AndroidJetpack.contraintLayout)
    implementation(Dependencies.AndroidJetpack.fragment)
    implementation(Dependencies.AndroidJetpack.fragment_ktx)
    implementation(Dependencies.AndroidJetpack.lifecycle)
    implementation(Dependencies.AndroidJetpack.paging)
    implementation(Dependencies.AndroidJetpack.preference)
    implementation(Dependencies.AndroidJetpack.preference_ktx)
    implementation(Dependencies.AndroidJetpack.recyclerView)
    implementation(Dependencies.SwipeRefreshLayout)

    implementation(Dependencies.googleDesign)

    implementation(Dependencies.timber)
    implementation(Dependencies.circleImage)
    implementation(Dependencies.arcView)
    implementation(Dependencies.tagView)
    implementation(Dependencies.scalLayout)
    implementation(Dependencies.filepicker)
    implementation(Dependencies.locationPicker)
    implementation(Dependencies.indicator)

    implementation(Dependencies.Jstarter.domain)


    implementation(Dependencies.imageCropper)
    implementation(Dependencies.leakCanary)
    implementation(Dependencies.lottie)
    implementation(Dependencies.picasso)

    implementation(Dependencies.Koin.core)
    implementation(Dependencies.Koin.scope)
    implementation(Dependencies.Koin.viewModel)
    testImplementation(Dependencies.Koin.test)

    implementation(Dependencies.Epoxy.core)
    implementation(Dependencies.Epoxy.databinding)
    implementation(Dependencies.Epoxy.paging)
    kapt(Dependencies.Epoxy.processor)

    implementation(Dependencies.Retrofit.core)
    implementation(Dependencies.Retrofit.gson)
    implementation(Dependencies.Retrofit.gson_converter)
    implementation(Dependencies.Retrofit.logging)
    implementation(Dependencies.Retrofit.mockWebServer)
    implementation(Dependencies.Retrofit.okhttp3)
    implementation(Dependencies.Retrofit.rxJava)
    implementation(Dependencies.Retrofit.scalarConverter)
    implementation(Dependencies.Retrofit.stetho)

    implementation(Dependencies.Kotlin.jre)
    implementation(Dependencies.Kotlin.Coroutine.core)
    implementation(Dependencies.Kotlin.Coroutine.android)
    implementation(Dependencies.Kotlin.Coroutine.rx2)

    implementation(Dependencies.Room.runtime)
    implementation(Dependencies.Room.room_ktx)
    implementation(Dependencies.Room.rxJava)
    kapt(Dependencies.Room.compiler)

    implementation(Dependencies.RxJava.core)
    implementation(Dependencies.RxJava.reactiveNetwork)
    implementation(Dependencies.RxJava.rxAndroid)
    implementation(Dependencies.RxJava.rxKotlin)
    implementation(Dependencies.RxJava.rxPreference)
    implementation(Dependencies.multidex)

    testImplementation(Dependencies.Test.mockk)
    testImplementation(Dependencies.Test.roboelectric)
    testImplementation(Dependencies.Test.hamcrest)
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation(Dependencies.Test.kotlin_junit)
    testImplementation(Dependencies.Test.androidArchCore)
    testImplementation(Dependencies.Test.kotlinCoroutineTest)
    testImplementation(Dependencies.Test.kotlinCoroutineRule)

    androidTestImplementation(Dependencies.Test.androidJunit)
    androidTestImplementation(Dependencies.Test.androidCore)
    androidTestImplementation(Dependencies.Test.androidRules)
    androidTestImplementation(Dependencies.Test.androidRunner)
    androidTestImplementation(Dependencies.Test.Espresso.core)
}
