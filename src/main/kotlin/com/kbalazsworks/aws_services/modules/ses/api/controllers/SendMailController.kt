package com.kbalazsworks.aws_services.modules.ses.api.controllers

import com.kbalazsworks.aws_services.modules.ses.domain.services.SendService
import com.kbalazsworks.aws_services.modules.ses.domain.value_objects.RawMail
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/ses")
class SendMailController(private val sendService: SendService) {
    @PostMapping("/send")
    fun sendMail(@RequestBody request: SendMailRequest) = sendService.send(map(request))

    private fun map(request: SendMailRequest) = RawMail(request.to, request.subject, request.text, request.html)

    data class SendMailRequest(val to: String, val subject: String, val text: String, val html: String)
}
