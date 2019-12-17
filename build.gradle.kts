import org.jetbrains.intellij.tasks.PatchPluginXmlTask
import org.jetbrains.intellij.tasks.PublishTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        jcenter()
        maven { setUrl("http://dl.bintray.com/jetbrains/intellij-plugin-service") }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

repositories {
    mavenCentral()
    jcenter()
}

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.intellij") version "0.4.15"
}

group = "com.github.shiraji.ccch"
version = System.getProperty("VERSION") ?: "0.0.1"

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "2019.2"
}

val patchPluginXml: PatchPluginXmlTask by tasks
patchPluginXml {
    changeNotes(project.file("LATEST.txt").readText())
    sinceBuild("192.5728")
    untilBuild("193")
}

val publishPlugin: PublishTask by tasks
publishPlugin {
    token(System.getenv("HUB_TOKEN"))
    channels(System.getProperty("CHANNELS") ?: "beta")
}

configurations {
    create("ktlint")

    dependencies {
        add("ktlint", "com.github.shyiko:ktlint:0.30.0")
    }
}

tasks.register("ktlintCheck", JavaExec::class) {
    description = "Check Kotlin code style."
    classpath = configurations["ktlint"]
    main = "com.github.shyiko.ktlint.Main"
    args("src/**/*.kt")
}

tasks.register("ktlintFormat", JavaExec::class) {
    description = "Fix Kotlin code style deviations."
    classpath = configurations["ktlint"]
    main = "com.github.shyiko.ktlint.Main"
    args("-F", "src/**/*.kt")
}

tasks.register("resolveDependencies") {
    doLast {
        project.rootProject.allprojects.forEach {subProject ->
            subProject.buildscript.configurations.forEach {configuration ->
                if (configuration.isCanBeResolved) {
                    configuration.resolve()
                }
            }
            subProject.configurations.forEach {configuration ->
                if (configuration.isCanBeResolved) {
                    configuration.resolve()
                }
            }
        }
    }
}

inline operator fun <T : Task> T.invoke(a: T.() -> Unit): T = apply(a)