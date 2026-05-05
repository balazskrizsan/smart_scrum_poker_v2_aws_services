package com.kbalazsworks.aws_services.modules.ses.api.configs

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SpringWebSecurityConfig {
    @Value("\${spring.security.oauth2.resourceserver.opaque.introspection-uri}")
    var introspectionUri: String? = null

    @Value("\${spring.security.oauth2.resourceserver.opaque.introspection-client-id}")
    var clientId: String? = null

    @Value("\${spring.security.oauth2.resourceserver.opaque.introspection-client-secret}")
    var clientSecret: String? = null

    companion object {
        private val logger = LoggerFactory.getLogger(SpringWebSecurityConfig::class.java)
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests { authz -> authz.anyRequest().authenticated() }
            .exceptionHandling { e -> e.authenticationEntryPoint(loggingAuthenticationEntry401Point()) }
            .oauth2ResourceServer { oauth2 ->
                oauth2
                    .opaqueToken { token ->
                        token
                            .introspectionUri(introspectionUri)
                            .introspectionClientCredentials(clientId, clientSecret)
                    }
            }
            .build()
    }

    private fun loggingAuthenticationEntry401Point(): AuthenticationEntryPoint {
        return AuthenticationEntryPoint { request: HttpServletRequest,
                                          response: HttpServletResponse,
                                          ex: AuthenticationException ->
            logger.warn(
                "401 Unauthorized – endpoint [{} {}], reason: {}",
                request.method,
                request.requestURI,
                ex.message
            )

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
        }
    }

//    private fun loggingAccessDenied403Handler(): AccessDeniedHandler {
//        return AccessDeniedHandler { request: HttpServletRequest, response: HttpServletResponse, accessDeniedException: AccessDeniedException ->
////            log.warn(
////                "Access denied for request [{} {}], reason: {}, user: {}",
////                request.getMethod(),
////                request.getRequestURI(),
////                accessDeniedException.getMessage(),
////                if (request.getUserPrincipal() != null) request.getUserPrincipal().getName() else "anonymous"
////            )
//            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden")
//        }
}
