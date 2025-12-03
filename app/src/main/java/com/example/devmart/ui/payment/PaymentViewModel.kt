package com.example.devmart.ui.payment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    // TODO: API 연동 시 AddressRepository 주입
    // private val addressRepo: AddressRepository
): ViewModel() {

    private val _address = MutableStateFlow<Address?>(null)
    val address = _address.asStateFlow()

    fun loadMyAddress() {
        // TODO: API 연동 시 아래 코드로 교체
        // viewModelScope.launch {
        //     val res = addressRepo.getMyAddress()
        //     if (res.isSuccess) {
        //         res.result?.let {
        //             _address.value = Address(
        //                 postalCode = it.zipcode ?: "",
        //                 roadAddress = it.address ?: "",
        //                 detail = it.addressDetail ?: "",
        //                 jibunAddress = ""
        //             )
        //         }
        //     }
        // }

        // 더미 데이터 (API 연동 전)
        _address.value = Address(
            postalCode = "",
            roadAddress = "",
            detail = "",
            jibunAddress = ""
        )
    }

    fun updateMyAddress(address: Address) {
        // TODO: API 연동 시 아래 코드로 교체
        // viewModelScope.launch {
        //     val req = UpdateAddressRequest(
        //         zipcode = address.postalCode,
        //         address = address.roadAddress,
        //         addressDetail = address.detail
        //     )
        //     val res = addressRepo.updateAddress(req)
        //     if (res.isSuccess) {
        //         loadMyAddress()
        //     }
        // }

        // 더미: 로컬에서만 업데이트
        _address.value = address
    }

    // 주소 검색에서 선택된 주소를 임시로 반영 (서버 저장 전)
    fun setSelectedAddress(selectedAddress: Address) {
        val current = _address.value ?: Address()
        _address.value = current.copy(
            postalCode = selectedAddress.postalCode,
            roadAddress = selectedAddress.roadAddress,
            jibunAddress = selectedAddress.jibunAddress
        )
    }
}
