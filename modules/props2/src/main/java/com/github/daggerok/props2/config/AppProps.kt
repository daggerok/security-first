package com.github.daggerok.props2.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

data class User2(var username: String? = null, var password: String? = null)

@Component
@ConfigurationProperties(prefix = "app")
data class AppProps2(
    var users: Map<String, User2>? = mutableMapOf()
)

fun AppProps2.parseUsers() = this.users.orEmpty().map { it.value }.map { User2(it.username, it.password) }
