package com.kbalazsworks.aws_services.configurations.logback

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import com.kbalazsworks.aws_services.common.services.ApplicationPropertiesService
import jakarta.annotation.PostConstruct
import net.logstash.logback.appender.LogstashTcpSocketAppender
import net.logstash.logback.encoder.LogstashEncoder
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class LogbackConfig(
    private var ap: ApplicationPropertiesService,
    private var logBackState: LogBackState
) {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    @PostConstruct
    fun setupLogger() {
        val currentEnv = ap.springApplicationEnv!!
        val currentApp = ap.springApplicationName!!

        log.info(
            "LogbackConfig setup / logstash enabled: {}, app: {} env: {}, url: {}",
            ap.logbackLogstashEnabled,
            currentApp,
            currentEnv,
            ap.logbackLogstashFullHost
        )

        val context = LoggerFactory.getILoggerFactory() as LoggerContext
        context.apply {
            reset()
            addTurboFilter(LogbackMdcTurboFilter(currentEnv, currentApp, logBackState))
        }

        with(context.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME)) {
            detachAndStopAllAppenders()
            level = Level.INFO
            if (ap.logbackLogstashEnabled) {
                addAppender(getLogstashTcpSocketAppender(context))
            }
            addAppender(getLoggingEventConsoleAppender(context))
        }

        log.info("LogbackConfig setup")
    }

    private fun getLoggingEventConsoleAppender(context: LoggerContext) = ConsoleAppender<ILoggingEvent?>().apply {
        log.info("LogbackConfig console created")

        this.context = context
        encoder = getLogstashEncoder(context)
        start()
    }

    private fun getLogstashTcpSocketAppender(context: LoggerContext) = LogstashTcpSocketAppender().apply {
        log.info("LogbackConfig logstash created")

        this.context = context
        try {
            addDestination(ap.logbackLogstashFullHost)
        } catch (e: Exception) {
            log.error("Logstash connection error", e)
        }

        encoder = getLogstashEncoder(context)
        start()
    }

    private fun getLogstashEncoder(context: LoggerContext) = LogstashEncoder().apply {
        this.context = context
        includeMdcKeyNames = listOf("env", "long_term")
        start()
    }
}
