package com.example.devmart.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

// 주문 생성 요청
data class CreateOrderRequest(
    val itemId: Long,
    val quantity: Int,
    val mileageToUse: Int = 0
)

// 주문 생성 응답
data class OrderResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Any?
)

interface OrderApi {
    @POST("orders")
    suspend fun createOrder(@Body request: CreateOrderRequest): OrderResponse
}

