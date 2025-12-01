package com.example.devmart.ui.payment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmart.data.repository.KakaoAddressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressSearchViewModel @Inject constructor(
    private val repo: KakaoAddressRepository
) : ViewModel() {

    var keyword by mutableStateOf("")
        private set

    var results by mutableStateOf<List<Address>>(emptyList())
        private set

    fun search(newKeyword: String) {
        keyword = newKeyword

        if (keyword.length < 2) {
            results = emptyList()
            return
        }

        viewModelScope.launch {
            // Kakao API 검색 결과를 Address 리스트로 변환하는 부분은
            // KakaoAddressRepository 쪽에서 처리해야 함
            results = repo.search(keyword)
        }
    }
}
