package com.example.devmart.ui.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devmart.ui.component.BackButton
import com.example.devmart.ui.component.OrderItemCard
import com.example.devmart.ui.component.OrderItemMode
import com.example.devmart.ui.theme.DevDarkneyvy
import com.example.devmart.ui.theme.DevFonts
import com.example.devmart.ui.theme.DevMartTheme
import com.example.devmart.ui.theme.DevWhite
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ----------------------- 데이터 클래스 -----------------------

data class OrderProduct(
    val id: String,
    val name: String,
    val detail: String,
    val price: Int,
    val qty: Int
)

data class Address(
    val roadAddress: String = "",
    val jibunAddress: String = "",
    val postalCode: String = "",
    val detail: String = ""
)

// ------------------------- Payment Screen -------------------------

@Composable
fun PaymentScreen(
    address: Address = Address(),
    products: List<OrderProduct> = emptyList(),
    onBackClick: () -> Unit = {},
    onNavigateToAddressSearch: () -> Unit = {},
    onSaveAddress: (Address) -> Unit = {},
    onClickPayment: () -> Unit = {}
) {
    val totalPrice = products.sumOf { it.price * it.qty }
    val deliveryFee = if (products.isEmpty()) 0 else 3000
    val finalAmount = totalPrice + deliveryFee

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 상단 헤더: 뒤로가기 + 결제하기
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(onClick = onBackClick)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "결제하기",
                fontSize = 20.sp,
                fontFamily = DevFonts.KakaoBigSans,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // ------------------ 배송 정보 ------------------
        DeliveryInfoBox(
            address = address,
            onClickSearchPostal = onNavigateToAddressSearch,
            onClickSave = onSaveAddress
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ------------------ 주문 상품 리스트 ------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(products) { product ->
                    OrderItemCard(product = product, mode = OrderItemMode.ReadOnly)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ------------------ 가격 요약 ------------------
        PriceSummaryBox(
            productPrice = totalPrice,
            deliveryFee = deliveryFee,
            finalAmount = finalAmount
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ------------------ 결제 버튼 ------------------
        Button(
            onClick = onClickPayment,
            colors = ButtonDefaults.buttonColors(
                containerColor = DevDarkneyvy,
                contentColor = DevWhite
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("${finalAmount}원 결제하기")
        }
    }
}

// ------------------ 개별 UI Box 컴포넌트 ------------------

@Composable
fun DeliveryInfoBox(
    address: Address,
    onClickSearchPostal: () -> Unit,
    onClickSave: (Address) -> Unit
) {
    var postal by remember { mutableStateOf(address.postalCode) }
    var road by remember { mutableStateOf(address.roadAddress) }
    var detail by remember { mutableStateOf(address.detail) }

    // 외부에서 address가 변경되면 내부 상태도 동기화
    LaunchedEffect(address.postalCode, address.roadAddress) {
        postal = address.postalCode
        road = address.roadAddress
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DevWhite, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "배송지 관리",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ------------------ 우편번호 + 찾기 버튼 ------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = postal,
                onValueChange = { /* 읽기 전용 - 우편번호 검색으로만 입력 */ },
                modifier = Modifier.weight(1f),
                placeholder = { Text("우편번호") },
                readOnly = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onClickSearchPostal,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DevDarkneyvy,
                    contentColor = DevWhite
                )
            ) {
                Text("우편번호 찾기")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ------------------ 도로명 주소 ------------------
        OutlinedTextField(
            value = road,
            onValueChange = { road = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("도로명 주소") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ------------------ 상세주소 + 저장하기 ------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = detail,
                onValueChange = { detail = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("상세주소") }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    onClickSave(
                        Address(
                            postalCode = postal,
                            roadAddress = road,
                            jibunAddress = address.jibunAddress,
                            detail = detail
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = DevDarkneyvy,
                    contentColor = DevWhite
                )
            ) {
                Text("저장하기")
            }
        }
    }
}

@Composable
fun PriceSummaryBox(
    productPrice: Int,
    deliveryFee: Int,
    finalAmount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DevWhite, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        PriceRow(label = "상품금액", value = productPrice)
        PriceRow(label = "배송비", value = deliveryFee)
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        PriceRow(label = "총 결제금액", value = finalAmount, bold = true)
    }
}

@Composable
fun PriceRow(
    label: String,
    value: Int,
    bold: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        if (bold) {
            Text(
                text = "${value}원",
                style = MaterialTheme.typography.titleMedium
            )
        } else {
            Text(text = "${value}원")
        }
    }
}

// ------------------ Preview ------------------

@Preview(showBackground = true)
@Composable
fun PreviewPaymentScreen() {
    DevMartTheme {
        PaymentScreen(
            address = Address(
                postalCode = "12345",
                roadAddress = "서울시 강남구 테헤란로 123",
                detail = "101동 1001호"
            ),
            products = listOf(
                OrderProduct("1", "게이밍 키보드", "청축 스위치 / RGB", 99000, 1),
                OrderProduct("2", "게이밍 마우스", "16000 DPI / 블랙", 59000, 2)
            )
        )
    }
}