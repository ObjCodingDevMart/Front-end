package com.example.devmart.ui.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmart.data.local.TokenStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject


sealed interface AuthState {
    data object Loading : AuthState
    data object Unauthenticated : AuthState
    data class Authenticated(val token: String) : AuthState
}

@HiltViewModel
class SessionViewModel @Inject constructor(tokenStore: TokenStore) : ViewModel() {
    val auth: StateFlow<AuthState> = tokenStore.tokenFlow
        .map { t -> if (t.isNullOrBlank()) AuthState.Unauthenticated else AuthState.Authenticated(t) }
        .onStart { emit(AuthState.Loading) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, AuthState.Loading)
}
