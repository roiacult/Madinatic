// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        addRepositories()
    }
    dependencies {
        classpath(ClassPaths.googleServices)
        classpath(ClassPaths.gradleBuild)
        classpath(ClassPaths.kotlinGradlePlugin)

    }
}

allprojects {
    repositories {
        addRepositories()
    }

}

tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}