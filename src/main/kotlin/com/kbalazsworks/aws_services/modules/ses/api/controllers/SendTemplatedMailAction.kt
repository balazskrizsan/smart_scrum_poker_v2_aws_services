package com.kbalazsworks.aws_services.modules.ses.api.controllers

import com.kbalazsworks.aws_services.modules.ses.api.builders.ResponseEntityBuilder
import com.kbalazsworks.aws_services.modules.ses.api.value_objects.ResponseData
import com.kbalazsworks.aws_services.modules.ses.domain.services.TemplatedMailService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/ses/send-templated")
class SendTemplatedMailAction(private val templatedMailService: TemplatedMailService) {

    @PostMapping
    @PreAuthorize("hasAuthority(\"aws.ses\")")
    fun sendTemplatedMail(@RequestBody request: SendTemplatedMailRequest): ResponseEntity<ResponseData<String>> {
        templatedMailService.sendTemplatedMail(
            to = request.to,
            subject = request.subject,
            htmlTemplate = request.htmlTemplate,
            textTemplate = request.textTemplate,
            templateVariables = request.templateVariables
        )

        return ResponseEntityBuilder<String>().build()
    }

    data class SendTemplatedMailRequest(
        val to: String,
        val subject: String,
        val htmlTemplate: String,
        val textTemplate: String,
        val templateVariables: Map<String, String>
    )
}
