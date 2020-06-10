import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.maven

object ClassPaths{
    const val googleServices = "com.google.gms:google-services:${Versions.Gradle.googleServices}"
    const val gradleBuild = "com.android.tools.build:gradle:${Versions.Gradle.gradleBuildVersion}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Kotlin.kotlinVersion}"
}
fun RepositoryHandler.addRepositories(){
    google()
    jcenter()
    mavenLocal()
    mavenCentral()
    maven(url = "https://jitpack.io" )
    maven( url ="https://dl.bintray.com/kotlin/kotlinx" )
}