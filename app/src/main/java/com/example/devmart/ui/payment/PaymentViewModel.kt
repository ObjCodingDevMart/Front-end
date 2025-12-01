package com.example.devmart.ui.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmart.data.remote.dto.UpdateAddressRequest
import com.example.devmart.data.repository.AddressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val addressRepo: AddressRepository
): ViewModel() {

    private val _address = MutableStateFlow<Address?>(null)
    val address = _address.asStateFlow()

    fun loadMyAddress() {
        viewModelScope.launch {
            val res = addressRepo.getMyAddress()
            if (res.isSuccess) {
                res.result?.let {
                    _address.value = Address(
                        postalCode = it.zipcode ?: "",
                        roadAddress = it.address ?: "",
                        detail = it.addressDetail ?: "",
                        jibunAddress = "" // 서버가 안 주면 공백으로
                    )
                }
            }
        }
    }

    fun updateMyAddress(address: Address) {
        viewModelScope.launch {
            val req = UpdateAddressRequest(
                zipcode = address.postalCode,
                address = address.roadAddress,
                addressDetail = address.detail
            )

            val res = addressRepo.updateAddress(req)
            if (res.isSuccess) {
                loadMyAddress() // 갱신 후 다시 조회로 상태 Sync
            }
        }
    }
}
