package com.example.devmart.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT

// 장바구니 추가 요청
data class AddToCartRequest(
    val itemId: Long,
    val quantity: Int
)

// 장바구니 수량 변경 요청
data class UpdateCartQuantityRequest(
    val cartItemId: Long,
    val quantity: Int
)

// 장바구니 상품 삭제 요청
data class RemoveCartItemRequest(
    val cartItemId: Long
)

// 장바구니 응답 (추가/삭제/수정)
data class CartResponse(
    val success: Boolean? = null,
    val code: String? = null,
    val message: String? = null,
    val result: CartResultDto? = null
)

// 장바구니 상품 정보
data class CartItemDetailDto(
    val itemId: Long?,
    val itemName: String,
    val price: Int,
    val imagePath: String?,
    val brand: String,
    val categories: List<String>,
    val productDetailImgUrl: String? = null,
    val productDetailContent: String? = null
)

// 장바구니 아이템 DTO (백엔드 CartItemResponse와 동일)
data class CartItemDto(
    val cartItemId: Long?,
    val item: CartItemDetailDto,
    val quantity: Int,
    val unitPrice: Int,
    val totalPrice: Int
)

// 장바구니 result 내부 객체
data class CartResultDto(
    val cartId: Long?,
    val items: List<CartItemDto>?,
    val totalAmount: Int? = null  // 총 금액
)

// 장바구니 목록 응답
data class CartListResponse(
    val success: Boolean? = null,
    val code: String? = null,
    val message: String? = null,
    val result: CartResultDto? = null
)

interface CartApi {
    // 장바구니 상품 추가
    @POST("cart/items")
    suspend fun addToCart(@Body request: AddToCartRequest): CartResponse

    // 장바구니 목록 조회
    @GET("cart")
    suspend fun getCartItems(): CartListResponse

    // 장바구니 상품 수량 변경
    @PUT("cart/items")
    suspend fun updateQuantity(@Body request: UpdateCartQuantityRequest): CartResponse

    // 장바구니 상품 삭제
    @HTTP(method = "DELETE", path = "cart/items", hasBody = true)
    suspend fun removeFromCart(@Body request: RemoveCartItemRequest): CartResponse

    // 장바구니 초기화 (모든 항목 삭제)
    @DELETE("cart")
    suspend fun clearCart(): CartResponse
}

