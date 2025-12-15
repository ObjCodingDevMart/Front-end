package com.example.devmart.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmart.data.remote.CategoryApi
import com.example.devmart.data.remote.CategoryDto
import com.example.devmart.domain.model.Product
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

data class HomeUiState(
    val products: List<Product> = emptyList(),
    val categories: List<CategoryDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    @Named("backend") retrofit: Retrofit
) : ViewModel() {

    private val categoryApi = retrofit.create(CategoryApi::class.java)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            // 카테고리와 상품 동시 로딩
            loadCategories()
            loadProducts()
        }
    }

    private suspend fun loadCategories() {
        runCatching {
            categoryApi.getCategories()
        }.onSuccess { response ->
            if (response.success) {
                _uiState.value = _uiState.value.copy(
                    categories = response.result
                )
            }
        }
    }

    private suspend fun loadProducts() {
        when (val result = productRepository.list()) {
            is Result.Ok -> {
                _uiState.value = _uiState.value.copy(
                    products = result.value,
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
