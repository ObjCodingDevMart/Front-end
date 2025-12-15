package com.example.devmart.ui.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmart.data.remote.AddressApi
import com.example.devmart.data.remote.AuthApi
import com.example.devmart.data.remote.dto.UpdateAddressRequest
import com.example.devmart.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class PaymentViewModel @Inject constructor(
    @Suppress("unused") private val orderRepository: OrderRepository, // TODO: 실제 결제 연동 시 사용
    @Named("backend") retrofit: Retrofit
): ViewModel() {

    private val authApi = retrofit.create(AuthApi::class.java)
    private val addressApi = retrofit.create(AddressApi::class.java)

    private val _address = MutableStateFlow<Address?>(null)
    val address = _address.asStateFlow()

    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState = _paymentState.asStateFlow()

    private val _mileage = MutableStateFlow(0)
    val mileage = _mileage.asStateFlow()

    init {
        loadMyMileage()
        loadMyAddress()
    }

    // 마일리지 조회 (user/me API에서 maxMilege 가져옴)
    fun loadMyMileage() {
        viewModelScope.launch {
            runCatching {
                authApi.me()
            }.onSuccess { response ->
                if (response.success) {
                    _mileage.value = response.result.maxMilege
                }
            }.onFailure {
                // 실패 시 0으로 유지
                _mileage.value = 0
            }
        }
    }

    // 주소 조회 (API 연동 준비됨, 실패 시 빈 주소)
    fun loadMyAddress() {
        viewModelScope.launch {
            runCatching {
                addressApi.getMyAddress()
            }.onSuccess { response ->
                if (response.success && response.result != null) {
                    _address.value = Address(
                        postalCode = response.result.zipcode ?: "",
                        roadAddress = response.result.address ?: "",
                        detail = response.result.addressDetail ?: "",
                        jibunAddress = ""
                    )
                } else {
                    // API 성공했지만 결과 없음 - 빈 주소
                    _address.value = Address()
                }
            }.onFailure {
                // API 실패 시 빈 주소 (나중에 입력하도록)
                _address.value = Address()
            }
        }
    }

    // 주소 저장
    fun updateMyAddress(address: Address) {
        viewModelScope.launch {
            runCatching {
                addressApi.updateAddress(
                    UpdateAddressRequest(
                        zipcode = address.postalCode,
                        address = address.roadAddress,
                        addressDetail = address.detail
                    )
                )
            }.onSuccess { response ->
                if (response.success) {
                    // 저장 성공 - 로컬 상태 업데이트
                    _address.value = address
                }
            }.onFailure {
                // API 실패해도 로컬에서는 업데이트 (UX 개선)
                _address.value = address
            }
        }
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

    // 주문 생성 (결제) - 현재는 바로 성공 처리
    @Suppress("UNUSED_PARAMETER")
    fun createOrder(
        itemId: Long,
        quantity: Int,
        mileageToUse: Int = 0
    ) {
        viewModelScope.launch {
            _paymentState.value = PaymentState.Loading
            
            // 잠시 로딩 표시 후 성공 처리
            kotlinx.coroutines.delay(1000)
            
            _paymentState.value = PaymentState.Success(
                message = "주문이 정상적으로 완료되었습니다."
            )
            
            // TODO: 실제 결제 연동 시 아래 코드 사용
            // orderRepository.createOrder(
            //     itemId = itemId,
            //     quantity = quantity,
            //     mileageToUse = mileageToUse
            // ).onSuccess { response ->
            //     _paymentState.value = PaymentState.Success(
            //         message = response.message.ifEmpty { "주문이 정상적으로 완료되었습니다." }
            //     )
            // }.onFailure { error ->
            //     _paymentState.value = PaymentState.Error(
            //         message = error.message ?: "결제 처리 중 오류가 발생했습니다."
            //     )
            // }
        }
    }

    // 결제 상태 초기화
    fun resetPaymentState() {
        _paymentState.value = PaymentState.Idle
    }
}
