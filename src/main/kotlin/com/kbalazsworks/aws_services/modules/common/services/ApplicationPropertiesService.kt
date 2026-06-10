package com.kbalazsworks.aws_services.modules.common.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ApplicationPropertiesService
{
    public static final String SPRING_APPLICATION_IS_PROD = "spring.application.isprod";

    @Value("${spring.application.name}")
    public String springApplicationName;

    @Value("${" + SPRING_APPLICATION_IS_PROD + "}")
    public boolean springApplicationIsProd;

    @Value("${server.ssl.enabled}")
    public boolean serverSslEnabled;

    @Value("${server.ssl.key-store-type}")
    public String serverSslKeyStoreType;

    @Value("${server.ssl.key-store}")
    public String serverSslKeyStore;

    @Value("${server.ssl.key-store-password}")
    public String serverSslKeyStorePassword;

    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-uri}")
    public String oauth2IntrospectionUri;

    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-id}")
    public String oauth2IntrospectionClientId;

    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-secret}")
    public String oauth2IntrospectionClientSecret;
}
