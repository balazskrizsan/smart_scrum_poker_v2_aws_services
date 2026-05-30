package com.kbalazsworks.aws_services.modules.ses.domain.value_objects

data class StringTemplateMail(
    val to: String,
    val subject: String,
    val htmlTemplate: String,
    val textTemplate: String,
    val templateVariables: Map<String, Any>,
)
