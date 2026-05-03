package com.kbalazsworks.aws_services.modules.ses.domain.value_objects

data class RawMail(
    val to: String,
    val subject: String,
    val text: String,
    val html: String
)
