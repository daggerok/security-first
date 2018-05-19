package com.github.daggerok.props.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
data class AppProps(
    var hello: String? = "world"
)
