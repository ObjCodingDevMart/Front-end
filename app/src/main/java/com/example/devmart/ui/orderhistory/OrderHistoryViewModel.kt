package com.example.devmart.ui.orderhistory

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmart.data.remote.OrderApi
import com.example.devmart.data.remote.OrderDto
import com.example.devmart.ui.order.OrderGroupUi
import com.example.devmart.ui.order.OrderHistoryUiState
import com.example.devmart.ui.order.OrderSummaryUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    @Named("backend") retrofit: Retrofit
) : ViewModel() {

    private val orderApi = retrofit.create(OrderApi::class.java)

    private val _uiState = MutableStateFlow(OrderHistoryUiState())
    val uiState: StateFlow<OrderHistoryUiState> = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadOrderHistory()
    }

    fun loadOrderHistory() {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d("OrderHistoryVM", "Loading order history...")

            try {
                val response = orderApi.getOrders()
                val orders = response.result ?: emptyList()
                Log.d("OrderHistoryVM", "getOrders response: success=${response.success}, size=${orders.size}")

                if (response.success) {
                    val groupedOrders = groupOrdersByDate(orders)
                    _uiState.value = OrderHistoryUiState(orderGroups = groupedOrders)
                } else {
                    Log.e("OrderHistoryVM", "Failed to load orders: ${response.message}")
                    _uiState.value = OrderHistoryUiState(orderGroups = emptyList())
                }
            } catch (e: Exception) {
                Log.e("OrderHistoryVM", "Error loading orders", e)
                _uiState.value = OrderHistoryUiState(orderGroups = emptyList())
            }

            _isLoading.value = false
        }
    }

    private fun groupOrdersByDate(orders: List<OrderDto>): List<OrderGroupUi> {
        // 날짜별로 그룹핑
        val grouped = orders.groupBy { order ->
            formatDateLabel(order.createdAt ?: "")
        }

        return grouped.map { (dateLabel, items) ->
            OrderGroupUi(
                orderDateLabel = dateLabel,
                items = items.map { order ->
                    OrderSummaryUi(
                        orderId = order.orderId?.toString() ?: "",
                        itemId = order.orderItem.itemId ?: 0L,
                        brandName = order.orderItem.brand ?: "",
                        productName = order.orderItem.itemName,
                        optionText = "${order.quantity}개",
                        priceText = formatPrice(order.finalPrice.toLong()),
                        price = order.orderItem.price,
                        imagePath = order.orderItem.imagePath
                    )
                }
            )
        }.sortedByDescending { it.orderDateLabel }  // 최신 날짜 먼저
    }

    private fun formatDateLabel(dateString: String): String {
        return try {
            // ISO 8601 형식 파싱 시도
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA)
            val outputFormat = SimpleDateFormat("yy.MM.dd(E)", Locale.KOREA)
            val date = inputFormat.parse(dateString)
            date?.let { outputFormat.format(it) } ?: dateString
        } catch (e: Exception) {
            try {
                // 다른 형식 시도
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
                val outputFormat = SimpleDateFormat("yy.MM.dd(E)", Locale.KOREA)
                val date = inputFormat.parse(dateString)
                date?.let { outputFormat.format(it) } ?: dateString
            } catch (e: Exception) {
                dateString  // 파싱 실패 시 원본 반환
            }
        }
    }

    private fun formatPrice(price: Long): String {
        val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
        return "${formatter.format(price)}원"
    }
}

