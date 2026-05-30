package com.kbalazsworks.aws_services.modules.ses.domain.value_objects

data class IdTemplateMail(
    val to: String,
    val subject: String,
    val templateId: String,
    val templateVariables: Map<String, Any>,
)
