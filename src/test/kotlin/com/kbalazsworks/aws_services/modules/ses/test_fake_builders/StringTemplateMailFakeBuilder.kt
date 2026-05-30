package com.kbalazsworks.aws_services.modules.ses.test_fake_builders

import com.kbalazsworks.aws_services.modules.ses.domain.value_objects.StringTemplateMail

class StringTemplateMailFakeBuilder {
    companion object {
        val DEFAULT_TO = RawMailFakeBuilder.DEFAULT_TO
        val DEFAULT_SUBJECT = RawMailFakeBuilder.DEFAULT_SUBJECT
        val DEFAULT_HTML_TEMPLATE = "<html><body>##variable##</body></html>"
        val DEFAULT_TEXT_TEMPLATE = "txt ##variable## txt"
        val DEFAULT_TEMPLATE_VARIABLES = mapOf<String, Any>("variable" to "Test Value")
    }

    private var to = DEFAULT_TO
    private var subject = DEFAULT_SUBJECT
    private var htmlTemplate = DEFAULT_HTML_TEMPLATE
    private var textTemplate = DEFAULT_TEXT_TEMPLATE
    private var templateVariables = DEFAULT_TEMPLATE_VARIABLES

    fun to(to: String) = apply { this.to = to }
    fun subject(subject: String) = apply { this.subject = subject }
    fun htmlTemplate(htmlTemplate: String) = apply { this.htmlTemplate = htmlTemplate }
    fun textTemplate(textTemplate: String) = apply { this.textTemplate = textTemplate }
    fun templateVariables(templateVariables: Map<String, Any>) = apply { this.templateVariables = templateVariables }

    fun build() = StringTemplateMail(to, subject, htmlTemplate, textTemplate, templateVariables)
}
