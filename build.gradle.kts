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

group = "com.github.shiraji"
version = "1.0-SNAPSHOT"

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "2019.2.3"
}

val patchPluginXml: PatchPluginXmlTask by tasks
patchPluginXml {
    changeNotes("""
      Add change notes here.<br>
      <em>most HTML tags may be used</em>""")
}

inline operator fun <T : Task> T.invoke(a: T.() -> Unit): T = apply(a)