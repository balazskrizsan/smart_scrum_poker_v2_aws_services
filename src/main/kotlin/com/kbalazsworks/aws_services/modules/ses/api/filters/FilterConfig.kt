package com.kbalazsworks.aws_services.modules.ses.api.filters

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {
    @Bean
    fun requestLoggingFilter(): FilterRegistrationBean<RequestLoggingFilter> {
        val registrationBean = FilterRegistrationBean(RequestLoggingFilter())
        registrationBean.addUrlPatterns("/*")
        registrationBean.order = 1

        return registrationBean
    }
}
