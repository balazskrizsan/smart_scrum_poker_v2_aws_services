package com.kbalazsworks.aws_services.common.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ApplicationPropertiesService {
    @Value("\${spring.application.name}")
    var springApplicationName: String? = null

    @Value("\${spring.application.env}")
    var springApplicationEnv: String? = null

    var logbackLogstashEnabled: Boolean = false
    var logbackLogstashFullHost: String = ""

    @Value("\${" + SPRING_APPLICATION_IS_PROD + "}")
    var springApplicationIsProd: Boolean = false

    @Value("\${server.ssl.enabled}")
    var serverSslEnabled: Boolean = false

    @Value("\${server.ssl.key-store-type}")
    var serverSslKeyStoreType: String? = null

    @Value("\${server.ssl.key-store}")
    var serverSslKeyStore: String? = null

    @Value("\${server.ssl.key-store-password}")
    var serverSslKeyStorePassword: String? = null

    @Value("\${spring.security.oauth2.resourceserver.opaque.introspection-uri}")
    var oauth2IntrospectionUri: String? = null

    @Value("\${spring.security.oauth2.resourceserver.opaque.introspection-client-id}")
    var oauth2IntrospectionClientId: String? = null

    @Value("\${spring.security.oauth2.resourceserver.opaque.introspection-client-secret}")
    var oauth2IntrospectionClientSecret: String? = null

    @Value("\${native.reflection-configuration-generator.enabled}")
    private lateinit var nativeReflectionConfigurationGeneratorEnabledEnabled: String
    val isNativeReflectionConfigurationGeneratorEnabledEnabled: Boolean by lazy {
        nativeReflectionConfigurationGeneratorEnabledEnabled.toBoolean()
    }

    companion object {
        const val SPRING_APPLICATION_IS_PROD: String = "spring.application.isprod"
    }
}
