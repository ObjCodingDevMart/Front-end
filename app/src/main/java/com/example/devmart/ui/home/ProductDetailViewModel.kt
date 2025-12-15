package com.example.devmart.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

data class ProductDetailUiState(
    val product: Product? = null,
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = false,
    val isReviewLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    @Named("backend") retrofit: Retrofit,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val reviewApi = retrofit.create(ReviewApi::class.java)
    private val productId: String = savedStateHandle["id"] ?: ""

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    init {
        if (productId.isNotEmpty()) {
            loadProduct(productId)
            loadReviews(productId)
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
}
