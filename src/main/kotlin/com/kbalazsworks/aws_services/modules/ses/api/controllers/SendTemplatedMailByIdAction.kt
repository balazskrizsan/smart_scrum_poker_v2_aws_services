package com.kbalazsworks.aws_services.modules.ses.api.controllers

import com.kbalazsworks.aws_services.modules.ses.api.builders.ResponseEntityBuilder
import com.kbalazsworks.aws_services.modules.ses.api.value_objects.ResponseData
import com.kbalazsworks.aws_services.modules.ses.domain.services.TemplatedMailService
import com.kbalazsworks.aws_services.modules.ses.domain.value_objects.IdTemplateMail
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/ses/send-templated-by-id")
class SendTemplatedMailByIdAction(
    private val templatedMailService: TemplatedMailService
) {
    @PostMapping
    @PreAuthorize("hasAuthority(\"aws.ses\")")
    fun sendTemplatedMailById(@RequestBody request: SendTemplatedMailByIdRequest): ResponseEntity<ResponseData<String>> {
        val mail = IdTemplateMail(
            to = request.to,
            subject = request.subject,
            templateId = request.templateId,
            templateVariables = request.templateVariables
        )
        templatedMailService.sendTemplatedMailById(mail)
        return ResponseEntityBuilder<String>().build()
    }

    data class SendTemplatedMailByIdRequest(
        val to: String,
        val subject: String,
        val templateId: String,
        val templateVariables: Map<String, String>
    )
}
