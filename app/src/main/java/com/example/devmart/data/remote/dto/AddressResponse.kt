package com.example.devmart.data.remote.dto

data class AddressResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: AddressResult?
)

data class AddressResult(
    val zipcode: String?,
    val address: String?,
    val addressDetail: String?
)
