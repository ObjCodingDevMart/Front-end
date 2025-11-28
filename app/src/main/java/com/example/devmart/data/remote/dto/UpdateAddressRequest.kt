package com.example.devmart.data.remote.dto

data class UpdateAddressRequest(
    val zipcode: String,
    val address: String,
    val addressDetail: String
)
