package com.kbalazsworks.aws_services.modules.ses.test_fake_builders

import com.kbalazsworks.aws_services.modules.ses.domain.value_objects.IdTemplateMail

class IdTemplateMailFakeBuilder {
    companion object {
        val DEFAULT_TO = RawMailFakeBuilder.DEFAULT_TO
        val DEFAULT_SUBJECT = RawMailFakeBuilder.DEFAULT_SUBJECT
        val DEFAULT_TEMPLATE_ID = "test-template"
        val DEFAULT_TEMPLATE_VARIABLES = mapOf<String, Any>("variable" to "Test Value")
    }

    private var to = DEFAULT_TO
    private var subject = DEFAULT_SUBJECT
    private var templateId = DEFAULT_TEMPLATE_ID
    private var templateVariables = DEFAULT_TEMPLATE_VARIABLES

    fun to(to: String) = apply { this.to = to }
    fun subject(subject: String) = apply { this.subject = subject }
    fun templateId(templateId: String) = apply { this.templateId = templateId }
    fun templateVariables(templateVariables: Map<String, Any>) = apply { this.templateVariables = templateVariables }

    fun build() = IdTemplateMail(to, subject, templateId, templateVariables)
}
