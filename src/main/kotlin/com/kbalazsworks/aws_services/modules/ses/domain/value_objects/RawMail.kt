package com.kbalazsworks.aws_services.modules.ses.domain.value_objects

data class RawMail(
    val to: String,
    val subject: String,
    val html: String,
    val text: String
)
