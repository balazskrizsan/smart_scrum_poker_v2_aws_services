package com.kbalazsworks.aws_services.modules.ses.api.filters

import com.kbalazsworks.aws_services.modules.common.services.ApplicationPropertiesService.SPRING_APPLICATION_IS_PROD
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
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant

@Component
@Order(HIGHEST_PRECEDENCE)
@ConditionalOnProperty(name = [SPRING_APPLICATION_IS_PROD], havingValue = "false", matchIfMissing = false)
class NonProdRequestLoggingFilter : OncePerRequestFilter() {
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

            logRequest(wrappedRequest, wrappedResponse, duration)

            wrappedResponse.copyBodyToResponse()
        }
    }

    private fun logRequest(
        request: ContentCachingRequestWrapper,
        response: ContentCachingResponseWrapper,
        durationMs: Long
    ) {
        val requestBody = extractRequestBody(request)
        val responseBody = extractResponseBody(response)

        val headers = request.headerNames.toList()
            .associateWith { request.getHeader(it) }

        log.info(
            """
            HTTP REQUEST
            ==================================================
            Method      : {}
            URI         : {}
            Query       : {}
            Remote Addr : {}
            Duration    : {} ms

            Headers     : {}

            RequestBody :
            {}

            ResponseCode: {}

            ResponseBody:
            {}
            ==================================================
            """.trimIndent(),
            request.method,
            request.requestURI,
            request.queryString,
            request.remoteAddr,
            durationMs,
            headers,
            requestBody,
            response.status,
            responseBody
        )
    }

    private fun extractRequestBody(request: ContentCachingRequestWrapper): String {
        val content = request.contentAsByteArray

        if (content.isEmpty()) {
            return "<empty>"
        }

        return try {
            String(content, StandardCharsets.UTF_8)
        } catch (_: Exception) {
            "<failed to read request body>"
        }
    }

    private fun extractResponseBody(response: ContentCachingResponseWrapper): String {
        val content = response.contentAsByteArray

        if (content.isEmpty()) {
            return "<empty>"
        }

        return try {
            String(content, StandardCharsets.UTF_8)
        } catch (_: Exception) {
            "<failed to read response body>"
        }
    }
}
