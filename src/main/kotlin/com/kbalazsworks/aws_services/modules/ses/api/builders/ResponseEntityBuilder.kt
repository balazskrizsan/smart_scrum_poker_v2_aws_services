package com.kbalazsworks.aws_services.modules.ses.api.builders

import com.kbalazsworks.aws_services.modules.ses.api.value_objects.ResponseData
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ResponseEntityBuilder<T>(
    var data: T? = null,
    var errorCode: Int = 0,
    var statusCode: HttpStatus = HttpStatus.OK,
    val headers: HttpHeaders = HttpHeaders()
) {
    fun build(): ResponseEntity<ResponseData<T>> {
        val success = errorCode == 0

        require(!(errorCode > 0 && statusCode == HttpStatus.OK)) {
            "Status code setup is needed for error response"
        }

        val responseData = ResponseData(data, success, errorCode)

        return ResponseEntity(responseData, headers, statusCode)
    }
}
