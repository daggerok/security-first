package com.github.daggerok.thymeleaf.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

data class User(var username: String? = null, var password: String? = null)

@Component
@ConfigurationProperties(prefix = "app")
data class AppProps(
    var users: Map<String, User>? = mutableMapOf()
)

fun AppProps.parseUsers() = this.users.orEmpty().map { it.value }.map { User(it.username, it.password) }
