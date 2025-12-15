package com.example.devmart.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// 주문 생성 요청
data class CreateOrderRequest(
    val itemId: Long,
    val quantity: Int,
    val mileageToUse: Int = 0
)

// 주문 응답
data class OrderResponse(
    val success: Boolean,  // 백엔드가 "success" 사용
    val code: String,
    val message: String,
    val result: Any?
)

// 주문에 포함된 상품 정보
data class OrderItemDetailDto(
    val itemId: Long?,
    val itemName: String,
    val price: Int,
    val imagePath: String?,
    val brand: String,
    val new: Boolean,  // 백엔드가 "new" 사용
    val categories: List<String>,
    val productDetailImgUrl: String? = null,
    val productDetailContent: String? = null
)

// 주문 내역 DTO
data class OrderDto(
    val orderId: Long?,
    val usernickname: String?,
    val orderItem: OrderItemDetailDto,
    val quantity: Int,
    val totalPrice: Int,
    val finalPrice: Int,
    val mileageToUse: Int,
    val status: String?,       // "PROCESSING", "COMPLETE", "CANCEL"
    val createdAt: String?     // "2024-12-15T10:30:00" 형식
)

// 주문 내역 목록 응답
data class OrderListResponse(
    val success: Boolean,  // 백엔드가 "success" 사용
    val code: String,
    val message: String,
    val result: List<OrderDto>?
)

interface OrderApi {
    // 주문 생성
    @POST("orders")
    suspend fun createOrder(@Body request: CreateOrderRequest): OrderResponse

    // 주문 내역 조회
    @GET("orders")
    suspend fun getOrders(): OrderListResponse
}
