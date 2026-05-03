package com.kbalazsworks.aws_services.modules.ses.domain.factories

import org.springframework.stereotype.Service
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.ses.SesClient

@Service
class AmazonSESFactory {
    fun create(): SesClient {
        return SesClient.builder()
            .region(Region.EU_CENTRAL_1)
            .build()
    }
}