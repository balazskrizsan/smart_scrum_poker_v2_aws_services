package com.kbalazsworks.aws_services.modules.ses.api.filters

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.web.filter.OncePerRequestFilter

class RequestLoggingFilter : OncePerRequestFilter() {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val method = request.method
        val requestUri = request.requestURI
        val queryString = request.queryString

        var logMessage = "API Call: [$method] $requestUri"
        if (queryString != null && queryString.isNotEmpty()) {
            logMessage += "?$queryString"
        }

        log.info(logMessage)

        filterChain.doFilter(request, response)
    }
}
