package com.example.devmart.ui.payment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddressSearchViewModel @Inject constructor(
    // TODO: API 연동 시 KakaoAddressRepository 주입
    // private val repo: KakaoAddressRepository
) : ViewModel() {

    var keyword by mutableStateOf("")
        private set

    var results by mutableStateOf<List<Address>>(emptyList())
        private set

    // 더미 주소 데이터
    private val dummyAddresses = listOf(
        Address("서울특별시 강남구 테헤란로 123", "서울특별시 강남구 역삼동 123-45", "06134"),
        Address("서울특별시 강남구 삼성로 456", "서울특별시 강남구 삼성동 67-89", "06178"),
        Address("서울특별시 서초구 서초대로 789", "서울특별시 서초구 서초동 101-11", "06621"),
        Address("서울특별시 송파구 올림픽로 300", "서울특별시 송파구 잠실동 22", "05551"),
        Address("서울특별시 마포구 월드컵북로 396", "서울특별시 마포구 상암동 1601", "03925"),
        Address("경기도 성남시 분당구 판교역로 235", "경기도 성남시 분당구 삼평동 681", "13494"),
        Address("경기도 수원시 영통구 광교중앙로 170", "경기도 수원시 영통구 이의동 906-5", "16514"),
        Address("부산광역시 해운대구 센텀중앙로 79", "부산광역시 해운대구 우동 1495", "48058")
    )

    // 키워드 입력 (검색 실행 X)
    fun updateKeyword(newKeyword: String) {
        keyword = newKeyword
    }

    // 검색 버튼 클릭 시 실행
    fun search() {
        if (keyword.length < 2) {
            results = emptyList()
            return
        }

        // TODO: API 연동 시 아래 코드로 교체
        // viewModelScope.launch {
        //     results = repo.search(keyword)
        // }

        // 더미 데이터에서 키워드로 필터링
        results = dummyAddresses.filter { address ->
            address.roadAddress.contains(keyword) || 
            address.jibunAddress.contains(keyword)
        }
    }
}
