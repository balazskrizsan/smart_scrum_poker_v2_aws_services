package com.kbalazsworks.aws_services.modules.ses.api.controllers

import com.kbalazsworks.aws_services.modules.ses.api.builders.ResponseEntityBuilder
import com.kbalazsworks.aws_services.modules.ses.api.value_objects.ResponseData
import com.kbalazsworks.aws_services.modules.ses.domain.services.SendService
import com.kbalazsworks.aws_services.modules.ses.domain.value_objects.RawMail
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/ses/send")
class SendMailAction(private val sendService: SendService) {
    @PostMapping
    @PreAuthorize("hasAuthority(\"aws.ses\")")
    fun sendMail(@RequestBody request: SendMailRequest): ResponseEntity<ResponseData<String>> {
        sendService.send(map(request))

        return ResponseEntityBuilder<String>().build()
    }

    private fun map(request: SendMailRequest) = RawMail(request.to, request.subject, request.text, request.html)

    data class SendMailRequest(val to: String, val subject: String, val text: String, val html: String)
}
