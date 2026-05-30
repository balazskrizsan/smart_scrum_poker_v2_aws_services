package com.kbalazsworks.aws_services.modules.ses.unit.domain.services

import com.kbalazsworks.AbstractTest
import com.kbalazsworks.aws_services.modules.ses.domain.services.TemplatedMailService
import com.kbalazsworks.aws_services.modules.ses.test_fake_builders.IdTemplateMailFakeBuilder
import com.kbalazsworks.aws_services.modules.ses.test_fake_builders.RawMailFakeBuilder
import com.kbalazsworks.aws_services.modules.ses.test_mock_builders.MustacheTemplateServiceMockBuilder
import com.kbalazsworks.aws_services.modules.ses.test_mock_builders.ResourceLoaderMockBuilder
import com.kbalazsworks.aws_services.modules.ses.test_mock_builders.SendServiceFakeMockBuilder
import org.junit.jupiter.api.Test

class TemplatedMailService_SendTemplatedMailByIdTest : AbstractTest() {
    @Test
    fun `Sending templated email by id -- Should send rendered email from file templates`() {
        // Arrange
        val testedMail = IdTemplateMailFakeBuilder().build()

        val htmlTemplatePath = "templates/${testedMail.templateId}/index.html"
        val textTemplatePath = "templates/${testedMail.templateId}/index.txt"
        val htmlTemplateContent = resourceToString(htmlTemplatePath)
        val textTemplateContent = resourceToString(textTemplatePath)

        val expectedMail = RawMailFakeBuilder().build()

        val resourceLoader = ResourceLoaderMockBuilder()
            .loadTemplate(htmlTemplatePath, htmlTemplateContent)
            .loadTemplate(textTemplatePath, textTemplateContent)
            .build()

        val mustacheTemplateService = MustacheTemplateServiceMockBuilder()
            .verifyRender(htmlTemplateContent, testedMail.templateVariables, expectedMail.html)
            .verifyRender(textTemplateContent, testedMail.templateVariables, expectedMail.text)
            .build()

        val sendServiceMockBuilder = SendServiceFakeMockBuilder().verifySend(expectedMail)
        val sendServiceMock = sendServiceMockBuilder.build()

        // Act
        val mocks = listOf(resourceLoader, mustacheTemplateService, sendServiceMock)
        createInstance(TemplatedMailService::class.java, mocks).sendTemplatedMailById(testedMail)

        // Assert
        sendServiceMockBuilder.verifySend()
    }
}
