package com.kbalazsworks.aws_services.modules.ses.domain.services

import com.kbalazsworks.aws_services.modules.ses.domain.value_objects.RawMail
import org.springframework.stereotype.Service

@Service
class TemplatedMailService(
    private val mustacheTemplateService: MustacheTemplateService,
    private val sendService: SendService
) {
    fun sendTemplatedMail(
        to: String,
        subject: String,
        htmlTemplate: String,
        textTemplate: String,
        templateVariables: Map<String, Any>
    ) {
        val renderedHtml = mustacheTemplateService.renderWithCustomDelimiters(htmlTemplate, templateVariables)
        val renderedText = mustacheTemplateService.renderWithCustomDelimiters(textTemplate, templateVariables)
        
        val rawMail = RawMail(to, subject, renderedText, renderedHtml)
        sendService.send(rawMail)
    }
}
