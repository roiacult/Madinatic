plugins{
    id("com.android.library")
    id("kotlin-android")
}
android{
    compileSdkVersion(Versions.AndroidConfigs.compileSdk)

    defaultConfig {
        minSdkVersion(Versions.AndroidConfigs.minSdkVersion)
        targetSdkVersion(Versions.AndroidConfigs.targetSdk)
        versionCode = Versions.AndroidConfigs.versionCode
        versionName  = Versions.AndroidConfigs.appVersion
        multiDexEnabled = true
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

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Dependencies.Kotlin.jre)
    implementation(Dependencies.Kotlin.Coroutine.core)
    implementation(Dependencies.Kotlin.reflect)

    implementation(Dependencies.Kotlin.Coroutine.android)
    implementation(Dependencies.Kotlin.Coroutine.rx2)
    implementation(Dependencies.AndroidJetpack.paging)
    implementation(Dependencies.Jstarter.domain)
    implementation(Dependencies.timber)

    implementation(Dependencies.Koin.core)
    implementation(Dependencies.Koin.scope)
    implementation(Dependencies.Koin.viewModel)

    testImplementation(Dependencies.Test.kotlin_junit)
    testImplementation(Dependencies.Test.junit)
    testImplementation(Dependencies.Test.hamcrest)
    testImplementation(Dependencies.Test.kotlinCoroutineTest)
    testImplementation(Dependencies.Test.kotlinCoroutineRule)
    testImplementation(Dependencies.Test.mockk)
    testImplementation(Dependencies.Test.roboelectric)

    androidTestImplementation(Dependencies.Test.androidTruthExt)
    androidTestImplementation(Dependencies.Test.androidRunner)
}