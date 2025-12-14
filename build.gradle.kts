// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.kover)
}

subprojects {
    pluginManager.withPlugin("org.jlleitschuh.gradle.ktlint") {
        configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
            ignoreFailures.set(true)
        }
    }

    plugins.withId("com.android.application") { apply(plugin = "org.jetbrains.kotlinx.kover") }
    plugins.withId("com.android.library")     { apply(plugin = "org.jetbrains.kotlinx.kover") }
    plugins.withId("org.jetbrains.kotlin.jvm"){ apply(plugin = "org.jetbrains.kotlinx.kover") }
}

// run to run code coverage report: ./gradlew :koverHtmlReport
dependencies {
    kover(project(":core:domain"))
    kover(project(":core:data"))
    kover(project(":feature:game"))
    kover(project(":feature:setboard"))
    kover(project(":feature:shared"))
    kover(project(":uicomponents"))
    kover(project(":uinavigation"))
}