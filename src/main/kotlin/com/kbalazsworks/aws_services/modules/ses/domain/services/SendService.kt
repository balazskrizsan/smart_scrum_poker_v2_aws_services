package com.kbalazsworks.aws_services.modules.ses.domain.services

import com.kbalazsworks.aws_services.modules.ses.common.exception.SesSendException
import software.amazon.awssdk.services.ses.SesClient
import com.kbalazsworks.aws_services.modules.ses.domain.value_objects.RawMail
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.ses.model.*

@Service
class SendService(private val sesClient: SesClient) {
    companion object {
        private const val EMAIL_SOURCE = "krizsan.balazs@gmail.com"
        private val logger = LoggerFactory.getLogger(SendService::class.toString())
    }

    @Throws(SesSendException::class)
    fun send(email: RawMail) {
        try {
            val request: SendEmailRequest = SendEmailRequest.builder()
                .destination(Destination.builder().toAddresses(email.to).build())
                .message(createMessage(email))
                .source(EMAIL_SOURCE)
                .build()
            val response = sesClient.sendEmail(request)

            logger.info("AWS SES: Message sent: " + response.messageId())
        } catch (e: Exception) {
            logger.error("AWS SES: E-mail sending error", e)

            throw SesSendException("E-mail sending error")
        }
    }

    private fun createMessage(email: RawMail) = Message.builder().body(
        Body.builder()
            .html(Content.builder().charset("UTF-8").data(email.html).build())
            .text(Content.builder().charset("UTF-8").data(email.text).build())
            .build()
    )
        .subject(Content.builder().charset("UTF-8").data(email.subject).build())
        .build()
}