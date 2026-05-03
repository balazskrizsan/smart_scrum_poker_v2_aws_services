package com.kbalazsworks.aws_services.modules.ses.api.value_objects

data class ResponseData<T>(val data: T?, val success: Boolean, val errorCode: Int)
