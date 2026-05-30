package com.kbalazsworks.aws_services.modules.ses.unit.domain.services

import com.kbalazsworks.AbstractTest
import com.kbalazsworks.aws_services.modules.ses.domain.services.SendService
import com.kbalazsworks.aws_services.modules.ses.test_fake_builders.RawMailFakeBuilder
import com.kbalazsworks.aws_services.modules.ses.test_mock_builders.AmazonSESFactoryMockBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SendService_SendTest : AbstractTest() {
    @Test
    fun `Sending email -- Should send email through AWS SES`() {
        // Arrange
        val testedMail = RawMailFakeBuilder().build()
        val expectedMail = RawMailFakeBuilder().build()

        val amazonSESFactoryMockBuilder = AmazonSESFactoryMockBuilder()
            .create()
            .verifySendEmail(expectedMail)
        val amazonSESFactoryMock = amazonSESFactoryMockBuilder.build()

        // Act
        val mocks = listOf(amazonSESFactoryMock)
        createInstance(SendService::class.java, mocks).send(testedMail)

        // Assert
        amazonSESFactoryMockBuilder.verify()
    }
}
