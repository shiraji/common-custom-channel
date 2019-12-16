package com.github.shiraji.ccch.domain

enum class CommonCustomChannel(val url: String) {
    EAP("https://plugins.jetbrains.com/plugins/eap/list"),
    ALPHA("https://plugins.jetbrains.com/plugins/alpha/list"),
    BETA("https://plugins.jetbrains.com/plugins/beta/list"),
    NIGHTLY("https://plugins.jetbrains.com/plugins/nightly/list")
}