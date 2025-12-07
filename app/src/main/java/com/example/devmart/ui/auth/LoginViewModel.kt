package com.example.devmart.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmart.data.remote.AuthApi
import com.example.devmart.data.remote.LoginRequest
import com.example.devmart.data.local.TokenStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LoginViewModel @Inject constructor(
    @Named("backend") retrofit: Retrofit,
    private val tokenStore: TokenStore
) : ViewModel() {
    private val api = retrofit.create(AuthApi::class.java)

    @Suppress("unused") // 로컬 로그인 제거됨, 카카오 로그인만 사용
    fun login(email: String, password: String, onError: (String)->Unit) {
        viewModelScope.launch {
            kotlin.runCatching { api.login(LoginRequest(email, password)) }
                .onSuccess { tokenStore.save(it.accessToken) } // 성공 → Session이 Authenticated로 변함
                .onFailure { onError(it.message ?: "로그인 실패") }
        }
    }
}
