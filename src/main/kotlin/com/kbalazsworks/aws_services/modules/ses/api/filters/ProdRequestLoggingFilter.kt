package com.kbalazsworks.aws_services.modules.ses.api.filters

import com.kbalazsworks.aws_services.common.services.ApplicationPropertiesService.Companion.SPRING_APPLICATION_IS_PROD
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.time.Duration
import java.time.Instant

@Component
@Order(HIGHEST_PRECEDENCE)
@ConditionalOnProperty(name = [SPRING_APPLICATION_IS_PROD], havingValue = "true")
class ProdRequestLoggingFilter : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val wrappedRequest = ContentCachingRequestWrapper(request, 1024 * 1024)
        val wrappedResponse = ContentCachingResponseWrapper(response)

        val start = Instant.now()

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse)
        } finally {
            val duration = Duration.between(start, Instant.now()).toMillis()

            logRequest(wrappedRequest, duration)

            wrappedResponse.copyBodyToResponse()
        }
    }

    private fun logRequest(request: ContentCachingRequestWrapper, durationMs: Long) {
        val message = "HTTP Request: ({} ms) [{}] {}{}"

        log.info(message, durationMs, request.method, request.requestURI, request.queryString)
    }
}
