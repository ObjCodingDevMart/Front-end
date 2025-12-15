package com.example.devmart.ui.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmart.data.remote.LikeApi
import com.example.devmart.data.remote.LikeRequest
import com.example.devmart.data.remote.ReviewApi
import com.example.devmart.domain.model.Product
import com.example.devmart.domain.model.Review
import com.example.devmart.domain.repo.ProductRepository
import com.example.devmart.domain.repo.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

data class ProductDetailUiState(
    val product: Product? = null,
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = false,
    val isReviewLoading: Boolean = false,
    val isLiked: Boolean = false,
    val likeMessage: String? = null,
    val error: String? = null
)

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    @Named("backend") retrofit: Retrofit,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val reviewApi = retrofit.create(ReviewApi::class.java)
    private val likeApi = retrofit.create(LikeApi::class.java)
    private val productId: String = savedStateHandle["id"] ?: ""

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    init {
        Log.d("ProductDetailVM", "Init with productId: $productId")
        if (productId.isNotEmpty()) {
            loadProduct(productId)
            loadReviews(productId)
            checkIfLiked(productId)
        }
    }

    fun loadProduct(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = productRepository.detail(id)) {
                is Result.Ok -> {
                    _uiState.value = _uiState.value.copy(
                        product = result.value,
                        isLoading = false
                    )
                }
                is Result.Err -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun loadReviews(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isReviewLoading = true)

            try {
                val itemId = id.toLongOrNull() ?: 0L
                val response = reviewApi.getReviewsByItemId(itemId)

                if (response.success) {
                    val reviews = response.result.map { dto ->
                        Review(
                            id = dto.reviewId.toString(),
                            userId = dto.userId.toString(),
                            userName = dto.userName,
                            rating = dto.rating,
                            content = dto.content,
                            date = dto.createdAt.take(10).replace("-", "."),
                            images = dto.images ?: emptyList()
                        )
                    }
                    _uiState.value = _uiState.value.copy(
                        reviews = reviews,
                        isReviewLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        reviews = emptyList(),
                        isReviewLoading = false
                    )
                }
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(
                    reviews = emptyList(),
                    isReviewLoading = false
                )
            }
        }
    }

    // 상품이 이미 좋아요 되어있는지 확인
    private fun checkIfLiked(id: String) {
        viewModelScope.launch {
            try {
                val itemId = id.toLongOrNull() ?: return@launch
                Log.d("ProductDetailVM", "Checking if liked: itemId=$itemId")
                
                val response = likeApi.getLikes()
                Log.d("ProductDetailVM", "getLikes response: success=${response.success}, size=${response.result.size}")
                
                if (response.success) {
                    // 새로운 DTO 구조: bookmarkId + item
                    val isLiked = response.result.any { it.item.itemId == itemId }
                    Log.d("ProductDetailVM", "isLiked=$isLiked for itemId=$itemId")
                    _uiState.value = _uiState.value.copy(isLiked = isLiked)
                }
            } catch (e: Exception) {
                Log.e("ProductDetailVM", "Error checking if liked", e)
                // 에러 시 기본값 유지
            }
        }
    }

    fun toggleLike() {
        val currentProduct = _uiState.value.product ?: return
        val itemId = currentProduct.id.toLongOrNull() ?: return
        val currentlyLiked = _uiState.value.isLiked
        
        Log.d("ProductDetailVM", "toggleLike: itemId=$itemId, currentlyLiked=$currentlyLiked")

        viewModelScope.launch {
            try {
                if (currentlyLiked) {
                    // 좋아요 해제 (DELETE)
                    Log.d("ProductDetailVM", "Removing like for itemId=$itemId")
                    val response = likeApi.removeLike(LikeRequest(itemId))
                    Log.d("ProductDetailVM", "removeLike response: success=${response.success}")
                    if (response.success) {
                        _uiState.value = _uiState.value.copy(
                            isLiked = false,
                            likeMessage = "좋아요가 해제되었습니다"
                        )
                    } else {
                        // 실패해도 상태 동기화를 위해 다시 확인
                        checkIfLiked(itemId.toString())
                    }
                } else {
                    // 좋아요 추가 (POST)
                    Log.d("ProductDetailVM", "Adding like for itemId=$itemId")
                    val response = likeApi.addLike(LikeRequest(itemId))
                    Log.d("ProductDetailVM", "addLike response: success=${response.success}")
                    if (response.success) {
                        _uiState.value = _uiState.value.copy(
                            isLiked = true,
                            likeMessage = "좋아요에 추가되었습니다"
                        )
                    } else {
                        // 실패해도 상태 동기화를 위해 다시 확인
                        checkIfLiked(itemId.toString())
                    }
                }
            } catch (e: HttpException) {
                // HTTP 에러 처리 (400 등)
                Log.e("ProductDetailVM", "HttpException: ${e.code()}", e)
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("ProductDetailVM", "Error body: $errorBody")
                
                // 에러 발생 시 서버 상태와 동기화
                checkIfLiked(itemId.toString())
                
                if (errorBody?.contains("LIKE_4001") == true) {
                    // 이미 좋아요 된 상품
                    _uiState.value = _uiState.value.copy(
                        likeMessage = "이미 좋아요한 상품입니다"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        likeMessage = "좋아요 처리 중 오류가 발생했습니다"
                    )
                }
            } catch (e: Exception) {
                Log.e("ProductDetailVM", "Exception in toggleLike", e)
                // 에러 발생 시 서버 상태와 동기화
                checkIfLiked(itemId.toString())
                _uiState.value = _uiState.value.copy(
                    likeMessage = "좋아요 처리 중 오류가 발생했습니다"
                )
            }
        }
    }

    fun clearLikeMessage() {
        _uiState.value = _uiState.value.copy(likeMessage = null)
    }
}
