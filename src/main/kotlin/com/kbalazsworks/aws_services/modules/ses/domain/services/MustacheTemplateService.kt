package com.kbalazsworks.aws_services.modules.ses.domain.services

import com.github.mustachejava.Mustache
import com.github.mustachejava.MustacheFactory
import org.springframework.stereotype.Service
import java.io.StringReader
import java.io.StringWriter

@Service
class MustacheTemplateService(private val mustacheFactory: MustacheFactory) {
    fun render(templateContent: String, variables: Map<String, Any>): String {
        val mustache: Mustache = mustacheFactory.compile(StringReader(templateContent), "template")
        val writer = StringWriter()
        mustache.execute(writer, variables).flush()

        return writer.toString()
    }
    
    fun renderWithCustomDelimiters(templateContent: String, variables: Map<String, Any>): String {
        val mustache: Mustache = mustacheFactory.compile(StringReader("{{=## ##=}}$templateContent"), "template")
        val writer = StringWriter()
        mustache.execute(writer, variables).flush()

        return writer.toString()
    }
}
