package com.example.devmart.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmart.domain.model.Product
import com.example.devmart.domain.repo.ProductRepository
import com.example.devmart.domain.repo.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Top100ViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    // 전체 상품 목록 (TOP 100용)
    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())
    val allProducts: StateFlow<List<Product>> = _allProducts.asStateFlow()

    // 필터링된 상품 목록 (검색 결과)
    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
    val searchResults: StateFlow<List<Product>> = _searchResults.asStateFlow()

    // 검색어
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    // 로딩 상태 (향후 로딩 UI에 사용)
    @Suppress("unused")
    private val _isLoading = MutableStateFlow(false)
    @Suppress("unused")
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 최근 검색어
    private val _recentSearches = MutableStateFlow<List<String>>(emptyList())
    val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()

    // 인기 키워드 (하드코딩 또는 API에서 가져올 수 있음)
    val popularKeywords = listOf("키보드", "마우스", "모니터", "노트북", "이어폰", "스피커")

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = productRepository.list()) {
                is Result.Ok -> {
                    _allProducts.value = result.value
                }
                is Result.Err -> {
                    // 에러 처리 - 더미 데이터 사용
                    _allProducts.value = getDummyProducts()
                }
            }
            _isLoading.value = false
        }
    }

    fun updateQuery(newQuery: String) {
        _query.value = newQuery
        filterProducts()
    }

    fun search() {
        val currentQuery = _query.value.trim()
        if (currentQuery.isNotEmpty()) {
            addToRecentSearches(currentQuery)
        }
        filterProducts()
    }

    fun searchByKeyword(keyword: String) {
        _query.value = keyword
        addToRecentSearches(keyword)
        filterProducts()
    }

    fun clearQuery() {
        _query.value = ""
        _searchResults.value = emptyList()
    }

    private fun filterProducts() {
        val searchQuery = _query.value.trim().lowercase()
        
        if (searchQuery.isEmpty()) {
            _searchResults.value = emptyList()
        } else {
            _searchResults.value = _allProducts.value.filter { product ->
                product.title.lowercase().contains(searchQuery) ||
                product.brand.lowercase().contains(searchQuery)
            }
        }
    }

    private fun addToRecentSearches(keyword: String) {
        val current = _recentSearches.value.toMutableList()
        // 중복 제거 후 맨 앞에 추가
        current.remove(keyword)
        current.add(0, keyword)
        // 최대 10개 유지
        _recentSearches.value = current.take(10)
    }

    private fun getDummyProducts(): List<Product> {
        return listOf(
            Product("1", "로지텍", "게이밍 키보드 G Pro", 159000, null),
            Product("2", "로지텍", "게이밍 마우스 G502", 89000, null),
            Product("3", "삼성", "오디세이 게이밍 모니터 27인치", 450000, null),
            Product("4", "애플", "맥북 프로 14인치", 2390000, null),
            Product("5", "소니", "WH-1000XM5 무선 헤드폰", 429000, null),
            Product("6", "JBL", "블루투스 스피커", 129000, null),
            Product("7", "레이저", "기계식 키보드 BlackWidow", 189000, null),
            Product("8", "앱코", "게이밍 마우스패드 XL", 25000, null),
            Product("9", "LG", "울트라기어 게이밍 모니터 32인치", 550000, null),
            Product("10", "에이수스", "ROG 게이밍 노트북", 1890000, null),
            Product("11", "보스", "QuietComfort 이어버드", 329000, null),
            Product("12", "하만카돈", "오닉스 스피커", 299000, null)
        )
    }
}

