package com.example.devmart.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmart.data.remote.AuthApi
import com.example.devmart.data.remote.KakaoLoginRequest
import com.example.devmart.data.local.TokenStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

// --- UI State ---
data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    @Named("backend") retrofit: Retrofit,
    private val tokenStore: TokenStore
) : ViewModel() {
    private val api = retrofit.create(AuthApi::class.java)
    
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    
    // 카카오 로그인: 카카오 액세스 토큰을 백엔드에 전달하여 서버 액세스 토큰 받기
    fun loginWithKakao(kakaoAccessToken: String, onError: (String) -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            kotlin.runCatching { 
                api.loginWithKakao(KakaoLoginRequest(kakaoAccessToken)) 
            }
                .onSuccess { response ->
                    tokenStore.save(response.accessToken)
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "카카오 로그인 실패"
                    )
                    onError(error.message ?: "카카오 로그인 실패")
                }
        }
    }
}
