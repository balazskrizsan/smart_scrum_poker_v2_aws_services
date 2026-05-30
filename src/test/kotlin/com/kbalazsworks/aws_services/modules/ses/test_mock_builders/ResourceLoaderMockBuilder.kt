package com.kbalazsworks.aws_services.modules.ses.test_mock_builders

import io.mockk.every
import io.mockk.mockk
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import java.io.ByteArrayInputStream

class ResourceLoaderMockBuilder(private val mock: ResourceLoader = mockk()) {

    fun loadTemplate(templatePath: String, content: String): ResourceLoaderMockBuilder {
        val resource = mockk<Resource>()
        every { resource.inputStream } returns ByteArrayInputStream(content.toByteArray())
        every { mock.getResource("classpath:$templatePath") } returns resource

        return this
    }

    fun build() = mock
}
