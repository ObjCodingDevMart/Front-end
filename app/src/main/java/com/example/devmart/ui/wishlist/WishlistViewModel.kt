package com.example.devmart.ui.wishlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmart.data.remote.LikeApi
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
class WishlistViewModel @Inject constructor(
    @Named("backend") retrofit: Retrofit
) : ViewModel() {

    private val likeApi = retrofit.create(LikeApi::class.java)

    private val _uiState = MutableStateFlow(WishlistScreenState())
    val uiState: StateFlow<WishlistScreenState> = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadWishlist()
    }

    fun loadWishlist() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("WishlistViewModel", "Loading wishlist...")
                val response = likeApi.getLikes()
                Log.d("WishlistViewModel", "Response: success=${response.success}, result size=${response.result.size}")
                
                if (response.success) {
                    val items = response.result.map { dto ->
                        // dto는 bookmarkId + item 구조
                        val item = dto.item
                        Log.d("WishlistViewModel", "Item: id=${item.itemId}, name=${item.itemName}")
                        WishlistItemUi(
                            id = item.itemId ?: 0L,
                            brand = item.brand,
                            name = item.itemName,
                            price = formatPrice(item.price.toLong()),
                            imageUrl = item.imagePath
                        )
                    }
                    _uiState.value = WishlistScreenState(items = items)
                    Log.d("WishlistViewModel", "Loaded ${items.size} items")
                } else {
                    Log.d("WishlistViewModel", "Response not successful")
                    _uiState.value = WishlistScreenState(items = emptyList())
                }
            } catch (e: Exception) {
                Log.e("WishlistViewModel", "Error loading wishlist", e)
                _uiState.value = WishlistScreenState(items = emptyList())
            }
            _isLoading.value = false
        }
    }

    private fun formatPrice(price: Long): String {
        val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
        return "${formatter.format(price)}원"
    }
}
