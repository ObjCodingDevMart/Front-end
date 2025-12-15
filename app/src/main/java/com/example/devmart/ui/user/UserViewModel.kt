package com.example.devmart.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmart.data.remote.AuthApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

data class UserUiState(
    val nickname: String = "",
    val emailLocal: String = "",
    val emailDomain: String = "",
    val point: Int = 0,
    val shippingCount: Int = 0,
    val likedCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class UserViewModel @Inject constructor(
    @Named("backend") retrofit: Retrofit
) : ViewModel() {
    private val api = retrofit.create(AuthApi::class.java)
    
    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()
    
    init {
        loadUserInfo()
    }
    
    fun loadUserInfo() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            kotlin.runCatching {
                api.me()
            }
                .onSuccess { response ->
                    if (response.success) {
                        val result = response.result
                        _uiState.value = _uiState.value.copy(
                            nickname = result.usernickname,
                            emailLocal = "", // 백엔드 응답에 이메일이 없음
                            emailDomain = "", // 백엔드 응답에 이메일이 없음
                            point = result.maxMilege,
                            shippingCount = result.orderStatusCounts.PROCESSING,
                            likedCount = result.userLikeCnt,
                            isLoading = false
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = response.message
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "사용자 정보 조회 실패"
                    )
                }
        }
    }
}

