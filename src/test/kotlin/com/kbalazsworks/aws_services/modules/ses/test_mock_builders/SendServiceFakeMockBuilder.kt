package com.kbalazsworks.aws_services.modules.ses.test_mock_builders

import com.kbalazsworks.aws_services.modules.ses.domain.services.SendService
import com.kbalazsworks.aws_services.modules.ses.domain.value_objects.RawMail
import io.mockk.mockk
import io.mockk.verify

class SendServiceFakeMockBuilder {
    private val mock = mockk<SendService>(relaxed = true)
    private var expectedMail: RawMail? = null

    fun verifySend(mail: RawMail) = apply { this.expectedMail = mail }

    fun verifySend() = expectedMail?.let { verify { mock.send(it) } }

    fun build() = mock
}
