package com.kbalazsworks.aws_services.modules.ses.test_fake_builders

import com.kbalazsworks.aws_services.modules.ses.domain.value_objects.RawMail

class RawMailFakeBuilder {
    companion object {
        val DEFAULT_TO = "test@example.com"
        val DEFAULT_SUBJECT = "Test Subject"
        val DEFAULT_HTML = "<html><body>Test Value</body></html>"
        val DEFAULT_TEXT = "txt Test Value txt"
    }

    private var to = DEFAULT_TO
    private var subject = DEFAULT_SUBJECT
    private var html = DEFAULT_HTML
    private var text = DEFAULT_TEXT

    fun to(to: String) = apply { this.to = to }
    fun subject(subject: String) = apply { this.subject = subject }
    fun html(html: String) = apply { this.html = html }
    fun text(text: String) = apply { this.text = text }

    fun build() = RawMail(to, subject, html, text)
}
