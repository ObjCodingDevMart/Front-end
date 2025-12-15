package com.example.devmart.ui.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmart.data.remote.CartApi
import com.example.devmart.data.remote.CartItemDto
import com.example.devmart.data.remote.RemoveCartItemRequest
import com.example.devmart.data.remote.UpdateCartQuantityRequest
import com.example.devmart.ui.payment.OrderProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class CartViewModel @Inject constructor(
    @Named("backend") retrofit: Retrofit
) : ViewModel() {

    private val cartApi = retrofit.create(CartApi::class.java)

    private val _cartItems = MutableStateFlow<List<CartItemDto>>(emptyList())
    
    private val _uiState = MutableStateFlow(CartScreenState(
        products = emptyList(),
        priceSummary = CartPriceSummaryUiState(
            productAmountText = "0원",
            shippingFeeText = "0원",
            orderAmountText = "0원"
        ),
        orderInfo = CartOrderInfoUiState(
            totalQuantityText = "0개",
            totalProductAmountText = "0원",
            totalShippingFeeText = "0원"
        )
    ))
    val uiState: StateFlow<CartScreenState> = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    init {
        loadCartItems()
    }

    fun loadCartItems() {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d("CartViewModel", "========== Loading cart items... ==========")
            
            try {
                val response = cartApi.getCartItems()
                val items = response.result?.items ?: emptyList()
                
                Log.d("CartViewModel", "Response code: ${response.code}")
                Log.d("CartViewModel", "Response message: ${response.message}")
                Log.d("CartViewModel", "Response cartId: ${response.result?.cartId}")
                Log.d("CartViewModel", "Response items size: ${items.size}")
                
                items.forEachIndexed { index, item ->
                    Log.d("CartViewModel", "Item[$index]: cartItemId=${item.cartItemId}, itemName=${item.item.itemName}, qty=${item.quantity}")
                }
                
                // success가 true면 성공
                if (response.success == true) {
                    _cartItems.value = items
                    updateUiState(items)
                    Log.d("CartViewModel", "Cart loaded successfully with ${items.size} items")
                } else {
                    // 빈 장바구니로 처리
                    Log.d("CartViewModel", "Empty or failed response, showing empty cart")
                    _cartItems.value = emptyList()
                    updateUiState(emptyList())
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error loading cart: ${e.message}", e)
                // 에러 시 빈 장바구니로 표시 (빈 응답일 수 있음)
                Log.d("CartViewModel", "Exception occurred, showing empty cart")
                _cartItems.value = emptyList()
                updateUiState(emptyList())
            }
            
            _isLoading.value = false
            Log.d("CartViewModel", "========== Cart loading finished ==========")
        }
    }

    private fun updateUiState(items: List<CartItemDto>) {
        val products = items.map { dto ->
            OrderProduct(
                id = dto.item.itemId?.toString() ?: "",
                name = dto.item.itemName,
                detail = dto.item.brand,
                price = dto.unitPrice,
                qty = dto.quantity
            )
        }

        val totalProductAmount = items.sumOf { it.totalPrice.toLong() }
        val shippingFee = if (items.isEmpty()) 0L else 3000L  // 배송비 3000원 (상품 있을 때만)
        val orderAmount = totalProductAmount + shippingFee
        val totalQuantity = items.sumOf { it.quantity }

        _uiState.value = CartScreenState(
            products = products,
            priceSummary = CartPriceSummaryUiState(
                productAmountText = formatPrice(totalProductAmount),
                shippingFeeText = formatPrice(shippingFee),
                orderAmountText = formatPrice(orderAmount)
            ),
            orderInfo = CartOrderInfoUiState(
                totalQuantityText = "${totalQuantity}개",
                totalProductAmountText = formatPrice(totalProductAmount),
                totalShippingFeeText = formatPrice(shippingFee)
            )
        )
    }

    fun removeFromCart(product: OrderProduct) {
        val cartItem = _cartItems.value.find { it.item.itemId?.toString() == product.id }
        if (cartItem == null || cartItem.cartItemId == null) {
            Log.e("CartViewModel", "Cart item not found for product: ${product.id}")
            return
        }

        viewModelScope.launch {
            try {
                Log.d("CartViewModel", "Removing item: cartItemId=${cartItem.cartItemId}")
                val response = cartApi.removeFromCart(RemoveCartItemRequest(cartItem.cartItemId))
                
                if (response.success == true) {
                    _message.value = response.message ?: "상품이 삭제되었습니다"
                    loadCartItems()  // 다시 로드
                } else {
                    _message.value = response.message ?: "상품 삭제에 실패했습니다"
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error removing item", e)
                _message.value = "상품 삭제 중 오류가 발생했습니다"
            }
        }
    }

    // 장바구니 초기화 (결제 완료 후 호출)
    fun clearCart() {
        viewModelScope.launch {
            try {
                Log.d("CartViewModel", "Clearing cart...")
                val response = cartApi.clearCart()
                
                if (response.success == true) {
                    Log.d("CartViewModel", "Cart cleared successfully")
                    _cartItems.value = emptyList()
                    updateUiState(emptyList())
                } else {
                    Log.e("CartViewModel", "Failed to clear cart: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error clearing cart", e)
            }
        }
    }

    // 수량 증가
    fun incrementQuantity(product: OrderProduct) {
        val cartItem = _cartItems.value.find { it.item.itemId?.toString() == product.id }
        if (cartItem == null || cartItem.cartItemId == null) {
            Log.e("CartViewModel", "Cart item not found for product: ${product.id}")
            return
        }

        val newQuantity = cartItem.quantity + 1
        updateQuantityApi(cartItem.cartItemId, newQuantity)
    }

    // 수량 감소
    fun decrementQuantity(product: OrderProduct) {
        val cartItem = _cartItems.value.find { it.item.itemId?.toString() == product.id }
        if (cartItem == null || cartItem.cartItemId == null) {
            Log.e("CartViewModel", "Cart item not found for product: ${product.id}")
            return
        }

        if (cartItem.quantity > 1) {
            val newQuantity = cartItem.quantity - 1
            updateQuantityApi(cartItem.cartItemId, newQuantity)
        }
    }

    private fun updateQuantityApi(cartItemId: Long, newQuantity: Int) {
        viewModelScope.launch {
            try {
                Log.d("CartViewModel", "Updating quantity: cartItemId=$cartItemId, newQuantity=$newQuantity")
                val response = cartApi.updateQuantity(
                    UpdateCartQuantityRequest(cartItemId, newQuantity)
                )

                if (response.success == true) {
                    // 성공하면 로컬 상태 업데이트
                    val currentItems = _cartItems.value.toMutableList()
                    val index = currentItems.indexOfFirst { it.cartItemId == cartItemId }
                    if (index >= 0) {
                        currentItems[index] = currentItems[index].copy(quantity = newQuantity)
                        _cartItems.value = currentItems
                        updateUiState(currentItems)
                    }
                    Log.d("CartViewModel", "Quantity updated successfully")
                } else {
                    Log.e("CartViewModel", "Failed to update quantity: ${response.message}")
                    _message.value = "수량 변경에 실패했습니다"
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error updating quantity", e)
                _message.value = "수량 변경 중 오류가 발생했습니다"
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }

    private fun formatPrice(price: Long): String {
        val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
        return "${formatter.format(price)}원"
    }
}

