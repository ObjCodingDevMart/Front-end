package com.example.devmart.data.remote.dto

data class AddressResponse(
    val success: Boolean,  // API 응답 필드명: "success"
    val code: String,
    val message: String,
    val result: AddressResult?
)

data class AddressResult(
    val zipcode: String?,
    val address: String?,
    val addressDetail: String?
)
