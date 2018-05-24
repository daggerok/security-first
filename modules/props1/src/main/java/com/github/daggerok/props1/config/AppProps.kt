package com.github.daggerok.props1.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

data class User1(var username: String? = null, var password: String? = null)

@Component
@ConfigurationProperties(prefix = "app")
data class AppProps1(
    var users: Map<String, User1>? = mutableMapOf()
)

fun AppProps1.parseUsers() = this.users.orEmpty().map { it.value }.map { User1(it.username, it.password) }
