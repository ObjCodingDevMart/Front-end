package com.example.devmart.data.remote.dto

data class DefaultResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Any?
)
