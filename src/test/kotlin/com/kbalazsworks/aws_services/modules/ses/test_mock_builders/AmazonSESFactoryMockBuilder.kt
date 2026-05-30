package com.kbalazsworks.aws_services.modules.ses.test_mock_builders

import com.kbalazsworks.aws_services.modules.ses.domain.factories.AmazonSESFactory
import com.kbalazsworks.aws_services.modules.ses.domain.value_objects.IdTemplateMail
import com.kbalazsworks.aws_services.modules.ses.domain.value_objects.RawMail
import com.kbalazsworks.aws_services.modules.ses.domain.value_objects.StringTemplateMail
import io.mockk.*
import software.amazon.awssdk.services.ses.SesClient
import software.amazon.awssdk.services.ses.model.SendEmailRequest
import software.amazon.awssdk.services.ses.model.SendEmailResponse

class AmazonSESFactoryMockBuilder(
    private val sesClientMock: SesClient? = null,
    private val amazonSESFactory: AmazonSESFactory? = null
) {
    companion object {
        const val MOCKED_AWS_RESPONSE_ID = "test-message-id"
    }

    private val mock = amazonSESFactory ?: mockk<AmazonSESFactory>()
    private val internalSesClientMock = sesClientMock ?: mockk<SesClient>(relaxed = true)
    private var expectedRawMail: RawMail? = null
    private var expectedStringTemplatedMail: StringTemplateMail? = null
    private var expectedIdTemplatedMail: IdTemplateMail? = null

    fun create(): AmazonSESFactoryMockBuilder {
        every { mock.create() } returns internalSesClientMock
        return this
    }

    fun verifySendEmail(email: RawMail): AmazonSESFactoryMockBuilder {
        expectedRawMail = email
        return setupSendEmailMock()
    }

    fun verifySendStringTemplatedEmail(email: StringTemplateMail): AmazonSESFactoryMockBuilder {
        expectedStringTemplatedMail = email
        setupSendEmailMock()

        if (amazonSESFactory != null) {
            every { amazonSESFactory.create() } answers { internalSesClientMock }
        }

        return this
    }

    fun verifySendIdTemplatedEmail(email: IdTemplateMail): AmazonSESFactoryMockBuilder {
        expectedIdTemplatedMail = email
        setupSendEmailMock()

        if (amazonSESFactory != null) {
            every { amazonSESFactory.create() } answers { internalSesClientMock }
        }

        return this
    }

    private fun setupSendEmailMock(): AmazonSESFactoryMockBuilder {
        val response = mockk<SendEmailResponse>()
        every { response.messageId() } returns MOCKED_AWS_RESPONSE_ID
        every { internalSesClientMock.sendEmail(any<SendEmailRequest>()) } returns response

        return this
    }

    private fun checkMailSendingValid(email: RawMail, slot: CapturingSlot<SendEmailRequest>) {
        val (actualEmail, actualSub, actualHtml, actualText) = extractEmailFields(slot)

        if (actualHtml != email.html
            || actualText != email.text
            || actualEmail != email.to
            || actualSub != email.subject
        ) {
            throw AssertionError(buildErrorMessage(email.to, email.subject, email.html, email.text, actualEmail, actualSub, actualHtml, actualText))
        }
    }

    private fun checkStringTemplatedMailSendingValid(email: StringTemplateMail, slot: CapturingSlot<SendEmailRequest>) {
        val (actualEmail, actualSub, actualHtml, actualText) = extractEmailFields(slot)

        if (!actualHtml.contains("Test Value")
            || !actualText.contains("Test Value")
            || actualEmail != email.to
            || actualSub != email.subject
        ) {
            throw AssertionError(
                """
                    Email content does not match:
                    To Expected:   ${email.to}
                    To Actual:     $actualEmail
                    Subject Expected: ${email.subject}
                    Subject Actual:   $actualSub
                    HTML Expected to contain: Test Value
                    HTML Actual:   $actualHtml
                    TEXT Expected to contain: Test Value
                    TEXT Actual:   $actualText
                    """.trimIndent()
            )
        }
    }

    private fun checkIdTemplatedMailSendingValid(email: IdTemplateMail, slot: CapturingSlot<SendEmailRequest>) {
        val (actualEmail, actualSub, actualHtml, actualText) = extractEmailFields(slot)

        if (!actualHtml.contains("Test Value")
            || !actualText.contains("Test Value")
            || actualEmail != email.to
            || actualSub != email.subject
        ) {
            throw AssertionError(
                """
                    Email content does not match:
                    To Expected:   ${email.to}
                    To Actual:     $actualEmail
                    Subject Expected: ${email.subject}
                    Subject Actual:   $actualSub
                    HTML Expected to contain: Test Value
                    HTML Actual:   $actualHtml
                    TEXT Expected to contain: Test Value
                    TEXT Actual:   $actualText
                    """.trimIndent()
            )
        }
    }

    private fun extractEmailFields(slot: CapturingSlot<SendEmailRequest>): EmailFields {
        val captured = slot.captured
        val message = captured.message()
        val body = message.body()

        return EmailFields(
            captured.destination().toAddresses().first(),
            message.subject().data(),
            body.html().data(),
            body.text().data()
        )
    }

    private fun buildErrorMessage(
        expectedTo: String,
        expectedSubject: String,
        expectedHtml: String,
        expectedText: String,
        actualTo: String,
        actualSubject: String,
        actualHtml: String,
        actualText: String
    ) = """
        Email content does not match:
        To Expected:   $expectedTo
        To Actual:     $actualTo
        Subject Expected: $expectedSubject
        Subject Actual:   $actualSubject
        HTML Expected: $expectedHtml
        HTML Actual:   $actualHtml
        TEXT Expected: $expectedText
        TEXT Actual:   $actualText
        """.trimIndent()

    fun verify() {
        val slot = slot<SendEmailRequest>()
        verify { internalSesClientMock.sendEmail(capture(slot)) }

        expectedRawMail?.let { checkMailSendingValid(it, slot) }
        expectedStringTemplatedMail?.let { checkStringTemplatedMailSendingValid(it, slot) }
        expectedIdTemplatedMail?.let { checkIdTemplatedMailSendingValid(it, slot) }
    }

    fun build() = mock

    private data class EmailFields(val to: String, val subject: String, val html: String, val text: String)
}
