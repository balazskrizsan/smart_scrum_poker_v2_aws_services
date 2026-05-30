package com.kbalazsworks

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import tools.jackson.module.kotlin.jacksonObjectMapper

abstract class AbstractE2eTest : AbstractTest() {
    @Autowired
    protected lateinit var wac: WebApplicationContext

    protected val objectMapper = jacksonObjectMapper()

    fun getMockMvc() = getMockMvc(false)

    fun getMockMvc(apOverride: Boolean) = MockMvcBuilders.webAppContextSetup(this.wac).build()
}
