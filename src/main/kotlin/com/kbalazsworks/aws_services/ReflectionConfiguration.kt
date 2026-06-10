package com.kbalazsworks.aws_services

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.core.spi.ContextAwareBase
import org.springframework.aot.hint.MemberCategory
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity

@Configuration
@RegisterReflectionForBinding(
    ResponseEntity::class,
    LoggerContext::class,
    ContextAwareBase::class,
)
class AppRuntimeHintsRegistrar : RuntimeHintsRegistrar {
    override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
        val categories = MemberCategory.entries.toTypedArray()

        hints.reflection().registerType(ResponseEntity::class.java, *categories)
        hints.reflection().registerType(LoggerContext::class.java, *categories)
        hints.reflection().registerType(ContextAwareBase::class.java, *categories)
    }
}
