package com.example.devmart.data.remote.dto

data class DefaultResponse(
    val success: Boolean,  // API 응답 필드명: "success"
    val code: String,
    val message: String,
    val result: Any?
)
