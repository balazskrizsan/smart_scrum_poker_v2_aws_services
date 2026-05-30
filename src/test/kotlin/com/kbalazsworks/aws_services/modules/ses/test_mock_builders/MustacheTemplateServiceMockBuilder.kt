package com.kbalazsworks.aws_services.modules.ses.test_mock_builders

import com.kbalazsworks.aws_services.modules.ses.domain.services.MustacheTemplateService
import io.mockk.every
import io.mockk.mockk

class MustacheTemplateServiceMockBuilder(private val mock: MustacheTemplateService = mockk()) {

    fun verifyRender(
        templateContent: String,
        variables: Map<String, Any>,
        returnResult: String,
    ): MustacheTemplateServiceMockBuilder {
        every { mock.render(templateContent, variables) } returns returnResult

        return this
    }

    fun build() = mock
}
