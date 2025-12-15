package com.example.devmart.ui.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.devmart.ui.theme.DevBlack
import com.example.devmart.ui.theme.DevDarkneyvy
import com.example.devmart.ui.theme.DevFonts
import com.example.devmart.ui.theme.DevGray
import com.example.devmart.ui.theme.DevMartTheme
import com.example.devmart.ui.theme.DevNeyvy
import com.example.devmart.ui.theme.DevWhite

// 결제 상태
sealed interface PaymentState {
    data object Idle : PaymentState
    data object Loading : PaymentState
    data class Success(val message: String) : PaymentState
    data class Error(val message: String) : PaymentState
}

@Composable
fun PaymentConfirmDialog(
    products: List<OrderProduct>,
    totalAmount: Int,
    deliveryFee: Int,
    availableMileage: Int = 0,
    address: Address = Address(),
    paymentState: PaymentState = PaymentState.Idle,
    onConfirm: (mileageToUse: Int) -> Unit,
    onDismiss: () -> Unit,
    onNavigateToHome: () -> Unit = {}
) {
    var mileageInput by remember { mutableStateOf("") }
    var mileageToUse by remember { mutableIntStateOf(0) }
    
    val finalAmount = (totalAmount + deliveryFee - mileageToUse).coerceAtLeast(0)
    
    // 주소 유효성 검사
    val isAddressValid = address.postalCode.isNotBlank() && address.roadAddress.isNotBlank()
    
    Dialog(
        onDismissRequest = { 
            if (paymentState !is PaymentState.Loading) onDismiss() 
        },
        properties = DialogProperties(
            dismissOnBackPress = paymentState !is PaymentState.Loading,
            dismissOnClickOutside = paymentState !is PaymentState.Loading
        )
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DevWhite
        ) {
            when (paymentState) {
                is PaymentState.Loading -> LoadingContent()
                is PaymentState.Success -> SuccessContent(
                    message = paymentState.message,
                    onConfirm = onNavigateToHome
                )
                is PaymentState.Error -> ErrorContent(
                    message = paymentState.message,
                    onRetry = { onConfirm(mileageToUse) },
                    onDismiss = onDismiss
                )
                is PaymentState.Idle -> ConfirmContent(
                    products = products,
                    totalAmount = totalAmount,
                    deliveryFee = deliveryFee,
                    availableMileage = availableMileage,
                    address = address,
                    isAddressValid = isAddressValid,
                    mileageInput = mileageInput,
                    mileageToUse = mileageToUse,
                    finalAmount = finalAmount,
                    onMileageChange = { input ->
                        mileageInput = input
                        val value = input.toIntOrNull() ?: 0
                        mileageToUse = value
                    },
                    onUseAllMileage = {
                        mileageToUse = availableMileage
                        mileageInput = availableMileage.toString()
                    },
                    onConfirm = { 
                        // ConfirmContent에서 검증 후 호출됨
                        onConfirm(mileageToUse.coerceIn(0, availableMileage))
                    },
                    onDismiss = onDismiss
                )
            }
        }
    }
}

@Composable
private fun ConfirmContent(
    products: List<OrderProduct>,
    totalAmount: Int,
    deliveryFee: Int,
    availableMileage: Int,
    address: Address,
    isAddressValid: Boolean,
    mileageInput: String,
    mileageToUse: Int,
    @Suppress("UNUSED_PARAMETER") finalAmount: Int, // 내부에서 계산하므로 미사용
    onMileageChange: (String) -> Unit,
    onUseAllMileage: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    var mileageError by remember { mutableStateOf<String?>(null) }
    var addressError by remember { mutableStateOf<String?>(null) }
    
    // 입력값 검증
    val inputValue = mileageInput.toIntOrNull() ?: 0
    val isInputExceedsAvailable = inputValue > availableMileage
    val validMileageToUse = if (isInputExceedsAvailable) 0 else mileageToUse.coerceIn(0, availableMileage)
    val calculatedFinalAmount = (totalAmount + deliveryFee - validMileageToUse).coerceAtLeast(0)
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        // 헤더
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "결제 확인",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = DevFonts.KakaoBigSans,
                color = DevBlack
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "닫기",
                    tint = DevBlack
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 주문 상품 요약
        Text(
            text = "주문 상품",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = DevBlack
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        products.forEach { product ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${product.name} x${product.qty}",
                    fontSize = 13.sp,
                    color = DevBlack
                )
                Text(
                    text = "${(product.price * product.qty).formatPrice()}원",
                    fontSize = 13.sp,
                    color = DevBlack
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = DevGray)
        Spacer(modifier = Modifier.height(16.dp))
        
        // 배송지 정보
        Text(
            text = "배송지",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = DevBlack
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        if (isAddressValid) {
            Text(
                text = "(${address.postalCode}) ${address.roadAddress}",
                fontSize = 13.sp,
                color = DevBlack
            )
            if (address.detail.isNotBlank()) {
                Text(
                    text = address.detail,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        } else {
            Text(
                text = "배송지가 설정되지 않았습니다.",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
        
        // 주소 에러 메시지
        if (addressError != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = addressError!!,
                fontSize = 12.sp,
                color = Color(0xFFF44336)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = DevGray)
        Spacer(modifier = Modifier.height(16.dp))
        
        // 마일리지 사용 (항상 표시)
        Text(
            text = "마일리지 사용",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = DevBlack
        )
        Text(
            text = "사용 가능: ${availableMileage.formatPrice()}원",
            fontSize = 12.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = mileageInput,
                onValueChange = { 
                    onMileageChange(it)
                    mileageError = null // 입력 시 에러 초기화
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text("0") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = mileageError != null
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = onUseAllMileage,
                enabled = availableMileage > 0
            ) {
                Text("전액 사용")
            }
        }
        
        // 에러 메시지 표시
        if (mileageError != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = mileageError!!,
                fontSize = 12.sp,
                color = Color(0xFFF44336)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = DevGray)
        Spacer(modifier = Modifier.height(16.dp))
        
        // 결제 금액 요약
        PriceRowCompact(label = "상품금액", value = totalAmount)
        PriceRowCompact(label = "배송비", value = deliveryFee)
        if (validMileageToUse > 0 && !isInputExceedsAvailable) {
            PriceRowCompact(label = "마일리지 할인", value = -validMileageToUse, isDiscount = true)
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = DevGray)
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "최종 결제금액",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DevBlack
            )
            Text(
                text = "${calculatedFinalAmount.formatPrice()}원",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DevNeyvy
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // 버튼들
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("취소")
            }
            Button(
                onClick = {
                    // 주소 검증
                    if (!isAddressValid) {
                        addressError = "배송지를 입력해주세요."
                        return@Button
                    }
                    
                    // 마일리지 검증
                    if (isInputExceedsAvailable) {
                        mileageError = "보유 마일리지(${availableMileage.formatPrice()}원)를 초과했습니다."
                        return@Button
                    }
                    
                    // 검증 통과 - 결제 진행
                    onConfirm()
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DevDarkneyvy,
                    contentColor = DevWhite
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("결제하기")
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = DevNeyvy
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "결제 처리 중...",
            fontSize = 16.sp,
            color = DevBlack
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "잠시만 기다려주세요",
            fontSize = 13.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun SuccessContent(
    message: String,
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(Color(0xFF4CAF50).copy(alpha = 0.1f), RoundedCornerShape(36.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "성공",
                modifier = Modifier.size(48.dp),
                tint = Color(0xFF4CAF50)
            )
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text(
            text = "결제 완료!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = DevBlack
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = message,
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = DevDarkneyvy,
                contentColor = DevWhite
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("확인")
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(Color(0xFFF44336).copy(alpha = 0.1f), RoundedCornerShape(36.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "실패",
                modifier = Modifier.size(48.dp),
                tint = Color(0xFFF44336)
            )
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text(
            text = "결제 실패",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = DevBlack
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = message,
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("닫기")
            }
            Button(
                onClick = onRetry,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DevDarkneyvy,
                    contentColor = DevWhite
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("다시 시도")
            }
        }
    }
}

@Composable
private fun PriceRowCompact(
    label: String,
    value: Int,
    isDiscount: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = Color.Gray
        )
        Text(
            text = if (isDiscount) "-${(-value).formatPrice()}원" else "${value.formatPrice()}원",
            fontSize = 13.sp,
            color = if (isDiscount) Color(0xFFF44336) else DevBlack
        )
    }
}

// 숫자 포맷팅 (천 단위 콤마)
private fun Int.formatPrice(): String {
    return String.format(java.util.Locale.KOREA, "%,d", this)
}

// Preview
@Preview(showBackground = true)
@Composable
private fun PreviewPaymentConfirmDialog() {
    DevMartTheme {
        PaymentConfirmDialog(
            products = listOf(
                OrderProduct("1", "게이밍 키보드", "청축", 99000, 1),
                OrderProduct("2", "게이밍 마우스", "블랙", 59000, 2)
            ),
            totalAmount = 217000,
            deliveryFee = 3000,
            availableMileage = 5000,
            address = Address(
                postalCode = "12345",
                roadAddress = "서울시 강남구 테헤란로 123",
                detail = "101동 1001호"
            ),
            paymentState = PaymentState.Idle,
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLoadingDialog() {
    DevMartTheme {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DevWhite
        ) {
            LoadingContent()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSuccessDialog() {
    DevMartTheme {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DevWhite
        ) {
            SuccessContent(
                message = "주문이 정상적으로 완료되었습니다.",
                onConfirm = {}
            )
        }
    }
}

