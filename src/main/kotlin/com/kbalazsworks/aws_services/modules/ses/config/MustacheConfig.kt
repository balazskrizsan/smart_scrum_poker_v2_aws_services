package com.kbalazsworks.aws_services.modules.ses.config

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.MustacheFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MustacheConfig {
    @Bean
    fun mustacheFactory(): MustacheFactory = DefaultMustacheFactory()
}
