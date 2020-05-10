object Dependencies{
    object AndroidJetpack{
        const val appcompat = "androidx.appcompat:appcompat:${Versions.AndroidJetpack.appCompat}"
        const val appcompat_ressource ="androidx.appcompat:appcompat-resources:${Versions.AndroidJetpack.appCompat}"

        const val fragment = "androidx.fragment:fragment:${Versions.AndroidJetpack.fragment}"
        const val fragment_ktx=  "androidx.fragment:fragment-ktx:${Versions.AndroidJetpack.fragment}"
        const val fragment_testing ="androidx.fragment:fragment-testing:${Versions.AndroidJetpack.fragment}"

        const val activity  = "androidx.activity:activity:${Versions.AndroidJetpack.activity}"
        const val activity_ktx = "androidx.activity:activity-ktx:${Versions.AndroidJetpack.activity}"

        const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.AndroidJetpack.recyclerView}"
        const val recyclerViewSelection  = "androidx.recyclerview:recyclerview-selection:${Versions.AndroidJetpack.recyclerView}"

        const val contraintLayout  = "androidx.constraintlayout:constraintlayout:${Versions.AndroidJetpack.constraintLayout}"

        const val lifecycle = "androidx.lifecycle:lifecycle-extensions:${Versions.AndroidJetpack.lifecycle}"

        const val preference = "androidx.preference:preference:${Versions.AndroidJetpack.preferences}"
        const val preference_ktx = "androidx.preference:preference-ktx:${Versions.AndroidJetpack.preferences}"

        const val paging = "androidx.paging:paging-runtime:${Versions.AndroidJetpack.paging}"
        const val viewPager2 = "androidx.viewpager2:viewpager2:${Versions.AndroidJetpack.viewPager2}"

    }

    const val googleDesign  = "com.google.android.material:material:${Versions.design}"
    const val picasso = "com.squareup.picasso:picasso:${Versions.picasso}"
    const val lottie  = "com.airbnb.android:lottie:${Versions.lottieVersion}"
    const val imageCropper = "com.theartofdev.edmodo:android-image-cropper:${Versions.imageCropper}"
    const val circleImage = "de.hdodenhof:circleimageview:${Versions.circleImage}"
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val numberPicker = "com.github.sephiroth74:NumberSlidingPicker:${Versions.numberPicker}"
    const val multidex = "com.android.support:multidex:${Versions.multiDex}"
    const val arcView = "com.github.florent37:shapeofview:1.4.7"
    const val tagView = "co.lujun:androidtagview:1.1.7"
    const val filepicker = "com.nbsp:materialfilepicker:1.9.1"
    const val locationPicker = "com.schibstedspain.android:leku:6.4.0"

    const val scalLayout = "com.github.iammert:ScalingLayout:1.2.1"


    const val SwipeRefreshLayout= "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshLayout}"

    object Room{
        const val runtime ="androidx.room:room-runtime:${Versions.AndroidJetpack.roomVersion}"
        const val compiler = "androidx.room:room-compiler:${Versions.AndroidJetpack.roomVersion}"
        const val rxJava  = "androidx.room:room-rxjava2:${Versions.AndroidJetpack.roomVersion}"
        const val room_ktx = "androidx.room:room-ktx:${Versions.AndroidJetpack.roomVersion}"
        const val testing  = "androidx.room:room-testing:${Versions.AndroidJetpack.roomVersion}"

    }
    object Jstarter{
        const val core  ="com.roacult.team7:Jstarter:${Versions.jstarterVersion}"
        const val domain = "com.roacult.team7:Jstarter_domain:${Versions.jstarterVersion}"
        const val presentation = "com.roacult.team7:Jstarter_presentation:${Versions.jstarterVersion}"
    }


    object Koin{
        // Koin core
        const val core = "org.koin:koin-android:${Versions.koin}"
        // Koin AndroidX ViewModel feature
        const val viewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
        // Koin AndroidX Scope feature
        const val scope = "org.koin:koin-androidx-scope:${Versions.koin}"
        // koin test
        const val test = "org.koin:koin-test:${Versions.koin}"
    }

    object Dagger{
        const val core  = "com.google.dagger:dagger-android:${Versions.daggerVersion}"
        const val javax_inject  = "javax.inject:javax.inject:1"
        const val support = "com.google.dagger:dagger-android-support:${Versions.daggerVersion}"
        const val kaptCompiler = "com.google.dagger:dagger-compiler:${Versions.daggerVersion}"
        const val kaptPorcessor = "com.google.dagger:dagger-android-processor:${Versions.daggerVersion}"

    }

    object Epoxy{
        const val core = "com.airbnb.android:epoxy:${Versions.epoxy}"
        const val processor  = "com.airbnb.android:epoxy-processor:${Versions.epoxy}"
        const val databinding = "com.airbnb.android:epoxy-databinding:${Versions.epoxy}"
        const val paging = "com.airbnb.android:epoxy-paging:${Versions.epoxy}"
    }
    object Retrofit {
        const val core = "com.squareup.retrofit2:retrofit:${Versions.Retrofit.core}"

        const val gson = "com.google.code.gson:gson:${Versions.Retrofit.gson}"
        const val rxJava = "com.squareup.retrofit2:adapter-rxjava2:${Versions.Retrofit.core}"
        const val gson_converter = "com.squareup.retrofit2:converter-gson:${Versions.Retrofit.core}"
        const val logging = "com.squareup.okhttp3:logging-interceptor:${Versions.Retrofit.okhttp}"

        const val okhttp3  = "com.squareup.okhttp3:okhttp:${Versions.Retrofit.okhttp}"
        const val mockWebServer = "com.squareup.okhttp3:mockwebserver:${Versions.Retrofit.okhttp}"
        const val stetho = "com.facebook.stetho:stetho-okhttp3:${Versions.Retrofit.stetho}"
        const val chuck_release = "com.readystatesoftware.chuck:library-no-op:${Versions.Retrofit.chuck}"
        const val chuck_debug = "com.readystatesoftware.chuck:library:${Versions.Retrofit.chuck}"
        const val scalarConverter = "com.squareup.retrofit2:converter-scalars:${Versions.Retrofit.core}"

    }
    object Test {
        const val junit = "junit:junit:${Versions.Test.junit}"
        const val hamcrest = "org.hamcrest:hamcrest-library:${Versions.Test.hamcrest}"
        const val kotlin_junit = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.Kotlin.kotlinVersion}"

        const val mockito= "org.mockito:mockito-core:${Versions.Test.mockito}"
        const val kotlin_mockito = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.Test.mockitoKotlin}"
        const val mockk= "io.mockk:mockk:${Versions.Test.mockk}"

        const val kotlinCoroutineTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesVersion}"
        const val kotlinCoroutineRule = "com.github.marcinOz:TestCoroutineRule:1.0.1"

        const val roboelectric = "org.robolectric:robolectric:${Versions.Test.roboelectric}"

        const val androidCore ="androidx.test:core:${Versions.Test.test}"
        const val androidArchCore = "androidx.arch.core:core-testing:2.1.0"
        const val androidRunner = "androidx.test:runner:${Versions.Test.runner}"
        const val androidRules = "androidx.test:rules:${Versions.Test.runner}"
        const val androidJunit = "androidx.test.ext:junit:${Versions.Test.test}"
        const val androidTruth = "com.google.truth:truth:${Versions.Test.truth}"
        const val androidTruthExt  = "androidx.test.ext:truth:${Versions.Test.test}"

        object Espresso{
            const val core = "androidx.test.espresso:espresso-core:${Versions.Test.espresso}"
            const val contrib = "androidx.test.espresso:espresso-contrib:${Versions.Test.espresso}"
            const val intents= "androidx.test.espresso:espresso-intents:${Versions.Test.espresso}"
            const val accessibility = "androidx.test.espresso:espresso-accessibility:${Versions.Test.espresso}"
            const val idlingRes = "androidx.test.espresso:espresso-idling-resource:${Versions.Test.espresso}"
            const val idlingConcurent = "androidx.test.espresso.idling:idling-concurrent:${Versions.Test.espresso}"

        }


    }


    object Kotlin{
        const val jre ="org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.Kotlin.kotlinVersion}"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.Kotlin.kotlinVersion}"
        object Coroutine{
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesVersion}"
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesVersion}"
            const val rx2 = "org.jetbrains.kotlinx:kotlinx-coroutines-rx2:${Versions.coroutinesVersion}"
        }
    }
    object RxJava{
        const val core ="io.reactivex.rxjava2:rxjava:${Versions.Rx.rxJava}"
        const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.Rx.rxKotlin}"
        const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.Rx.rxAndroid}"
        const val rxPreference ="com.f2prateek.rx.preferences2:rx-preferences:${Versions.rxpreference}"
        const val reactiveNetwork = "com.github.pwittchen:reactivenetwork-rx2:${Versions.reactiveNetwork}"

    }
}