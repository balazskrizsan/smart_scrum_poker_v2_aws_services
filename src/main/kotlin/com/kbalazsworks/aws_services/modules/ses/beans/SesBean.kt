package com.kbalazsworks.aws_services.modules.ses.beans

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.ses.SesClient

@Configuration
class SesBean {
    @Bean
    fun sesClient(): SesClient {
        return SesClient.builder()
            .region(Region.EU_CENTRAL_1)
            .build()
    }
}
