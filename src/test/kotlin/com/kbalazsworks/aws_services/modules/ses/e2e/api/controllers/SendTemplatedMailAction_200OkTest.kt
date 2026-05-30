package com.kbalazsworks.aws_services.modules.ses.e2e.api.controllers

import com.kbalazsworks.AbstractE2eTest
import com.kbalazsworks.aws_services.modules.ses.api.controllers.SendTemplatedMailAction
import com.kbalazsworks.aws_services.modules.ses.domain.factories.AmazonSESFactory
import com.kbalazsworks.aws_services.modules.ses.test_fake_builders.StringTemplateMailFakeBuilder
import com.kbalazsworks.aws_services.modules.ses.test_mock_builders.AmazonSESFactoryMockBuilder
import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import software.amazon.awssdk.services.ses.SesClient

@SpringBootTest
@AutoConfigureMockMvc
class SendTemplatedMailAction_200OkTest : AbstractE2eTest() {
    @MockkBean
    lateinit var amazonSESFactory: AmazonSESFactory

    @MockkBean
    lateinit var sesClientMock: SesClient

    @Test
    fun `Sending templated email via controller -- Should call AWS SES sendEmail`() {
        // Arrange
        val testedMail = StringTemplateMailFakeBuilder().build()

        val amazonSESFactoryMockBuilder = AmazonSESFactoryMockBuilder(sesClientMock, amazonSESFactory)
            .verifySendStringTemplatedEmail(testedMail)

        val request = SendTemplatedMailAction.SendTemplatedMailRequest(
            to = testedMail.to,
            subject = testedMail.subject,
            htmlTemplate = testedMail.htmlTemplate,
            textTemplate = testedMail.textTemplate,
            templateVariables = testedMail.templateVariables as Map<String, String>
        )

        // Act
        getMockMvc().perform(
            post("/api/v1/ses/send-templated")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)

        // Assert
        amazonSESFactoryMockBuilder.verify()
    }
}
