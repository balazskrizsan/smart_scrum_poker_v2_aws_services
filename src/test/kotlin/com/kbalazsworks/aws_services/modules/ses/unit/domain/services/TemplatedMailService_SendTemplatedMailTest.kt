package com.kbalazsworks.aws_services.modules.ses.unit.domain.services

import com.kbalazsworks.AbstractTest
import com.kbalazsworks.aws_services.modules.ses.domain.services.TemplatedMailService
import com.kbalazsworks.aws_services.modules.ses.test_fake_builders.RawMailFakeBuilder
import com.kbalazsworks.aws_services.modules.ses.test_fake_builders.StringTemplateMailFakeBuilder
import com.kbalazsworks.aws_services.modules.ses.test_mock_builders.MustacheTemplateServiceMockBuilder
import com.kbalazsworks.aws_services.modules.ses.test_mock_builders.SendServiceFakeMockBuilder
import org.junit.jupiter.api.Test

class TemplatedMailService_SendTemplatedMailTest : AbstractTest() {
    @Test
    fun `Sending text templated email -- Should send rendered email from text templates`() {
        // Arrange
        val testedMail = StringTemplateMailFakeBuilder().build()
        val expectedMail = RawMailFakeBuilder().build()

        val mustacheTemplateService = MustacheTemplateServiceMockBuilder()
            .verifyRender(testedMail.htmlTemplate, testedMail.templateVariables, expectedMail.html)
            .verifyRender(testedMail.textTemplate, testedMail.templateVariables, expectedMail.text)
            .build()

        val sendServiceMockBuilder = SendServiceFakeMockBuilder().verifySend(expectedMail)
        val sendServiceMock = sendServiceMockBuilder.build()

        // Act
        val mocks = listOf(mustacheTemplateService, sendServiceMock)
        createInstance(TemplatedMailService::class.java, mocks).sendTemplatedMail(testedMail)

        // Assert
        sendServiceMockBuilder.verifySend()
    }
}
