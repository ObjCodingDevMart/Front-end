package com.example.devmart.ui.review

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmart.data.remote.CreateReviewRequest
import com.example.devmart.data.remote.ReviewApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

// 리뷰 등록 상태
sealed interface ReviewState {
    data object Idle : ReviewState
    data object Loading : ReviewState
    data class Success(val message: String) : ReviewState
    data class Error(val message: String) : ReviewState
}

// 리뷰 작성 대상 상품 정보
data class ReviewTargetItem(
    val itemId: Long,
    val itemName: String,
    val brand: String,
    val price: Int,
    val imagePath: String?
)

@HiltViewModel
class ReviewViewModel @Inject constructor(
    @Named("backend") retrofit: Retrofit
) : ViewModel() {
    
    private val reviewApi = retrofit.create(ReviewApi::class.java)
    
    private val _reviewState = MutableStateFlow<ReviewState>(ReviewState.Idle)
    val reviewState: StateFlow<ReviewState> = _reviewState.asStateFlow()
    
    private val _targetItem = MutableStateFlow<ReviewTargetItem?>(null)
    val targetItem: StateFlow<ReviewTargetItem?> = _targetItem.asStateFlow()
    
    /**
     * 리뷰 작성 대상 상품 설정
     */
    fun setTargetItem(item: ReviewTargetItem) {
        _targetItem.value = item
        Log.d("ReviewViewModel", "Target item set: ${item.itemName}")
    }
    
    /**
     * 리뷰 작성 대상 초기화
     */
    fun clearTargetItem() {
        _targetItem.value = null
        _reviewState.value = ReviewState.Idle
    }
    
    /**
     * 리뷰 등록
     */
    fun createReview(
        rating: Int,
        content: String,
        imgUrl: String = "",
        imgKey: String = ""
    ) {
        val item = _targetItem.value ?: run {
            Log.e("ReviewViewModel", "Target item is null")
            _reviewState.value = ReviewState.Error("상품 정보가 없습니다.")
            return
        }
        
        viewModelScope.launch {
            _reviewState.value = ReviewState.Loading
            Log.d("ReviewViewModel", "Creating review for itemId: ${item.itemId}")
            
            try {
                val request = CreateReviewRequest(
                    itemId = item.itemId,
                    rating = rating,
                    content = content,
                    imgUrl = imgUrl,
                    imgKey = imgKey
                )
                
                val response = reviewApi.createReview(request)
                Log.d("ReviewViewModel", "Review response: success=${response.success}, message=${response.message}")
                
                if (response.success) {
                    _reviewState.value = ReviewState.Success(response.message)
                } else {
                    _reviewState.value = ReviewState.Error(response.message)
                }
            } catch (e: Exception) {
                Log.e("ReviewViewModel", "Error creating review", e)
                _reviewState.value = ReviewState.Error(e.message ?: "리뷰 등록에 실패했습니다.")
            }
        }
    }
    
    /**
     * 리뷰 상태 초기화
     */
    fun resetReviewState() {
        _reviewState.value = ReviewState.Idle
    }
}

