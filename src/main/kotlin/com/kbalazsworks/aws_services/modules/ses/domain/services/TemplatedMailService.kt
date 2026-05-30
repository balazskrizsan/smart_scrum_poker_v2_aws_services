package com.kbalazsworks.aws_services.modules.ses.domain.services

import com.kbalazsworks.aws_services.modules.ses.domain.value_objects.IdTemplateMail
import com.kbalazsworks.aws_services.modules.ses.domain.value_objects.RawMail
import com.kbalazsworks.aws_services.modules.ses.domain.value_objects.StringTemplateMail
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import org.springframework.util.FileCopyUtils

@Service
class TemplatedMailService(
    private val mustacheTemplateService: MustacheTemplateService,
    private val sendService: SendService,
    private val resourceLoader: ResourceLoader
) {
    fun sendTemplatedMail(mail: StringTemplateMail) {
        val renderedHtml = mustacheTemplateService.render(mail.htmlTemplate, mail.templateVariables)
        val renderedText = mustacheTemplateService.render(mail.textTemplate, mail.templateVariables)

        sendService.send(RawMail(mail.to, mail.subject, renderedHtml, renderedText))
    }

    fun sendTemplatedMailById(mail: IdTemplateMail) {
        val htmlTemplate = loadTemplate("templates/${mail.templateId}/index.html")
        val textTemplate = loadTemplate("templates/${mail.templateId}/index.txt")

        sendTemplatedMail(StringTemplateMail(mail.to, mail.subject, htmlTemplate, textTemplate, mail.templateVariables))
    }

    private fun loadTemplate(path: String) = FileCopyUtils
        .copyToString(resourceLoader.getResource("classpath:$path").inputStream.reader())
}
