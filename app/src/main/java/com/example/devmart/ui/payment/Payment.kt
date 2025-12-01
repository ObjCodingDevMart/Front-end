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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.devmart.ui.component.OrderItemCard
import com.example.devmart.ui.component.OrderItemMode
import com.example.devmart.ui.theme.DevDarkneyvy
import com.example.devmart.ui.theme.DevWhite

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
    viewModel: PaymentViewModel = hiltViewModel(),
    onNavigateToAddressSearch: () -> Unit
) {
    val addressState by viewModel.address.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMyAddress()
    }

    val address = addressState ?: Address()

    // 더미 상품
    val products = listOf(
        OrderProduct("1", "게이밍 키보드", "청축 스위치 / RGB", 99000, 1),
        OrderProduct("2", "게이밍 마우스", "16000 DPI / 블랙", 59000, 2)
    )

    val totalPrice = products.sumOf { it.price * it.qty }
    val deliveryFee = 3000
    val finalAmount = totalPrice + deliveryFee

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("결제하기", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        // ------------------ 배송 정보 ------------------
        DeliveryInfoBox(
            address = address,
            onClickSearchPostal = onNavigateToAddressSearch,
            onClickSave = {
                viewModel.updateMyAddress(it)
            }
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
            onClick = { /* TODO: 결제 API 연동 */ },
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
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
                onValueChange = { postal = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("우편번호") }
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
                            jibunAddress = address.jibunAddress, // 그대로 유지
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
            .background(Color(0xFFE9E9E9), RoundedCornerShape(12.dp))
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
    PaymentScreen(
        onNavigateToAddressSearch = {}
    )
}
