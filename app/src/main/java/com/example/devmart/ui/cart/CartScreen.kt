package com.example.devmart.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.devmart.ui.component.BottomNavigationBar
import com.example.devmart.ui.component.BottomNavItem
import com.example.devmart.ui.theme.DevBlack
import com.example.devmart.ui.theme.DevDarkgray
import com.example.devmart.ui.theme.DevDarkneyvy
import com.example.devmart.ui.theme.DevGray
import com.example.devmart.ui.theme.DevWhite

// -------------------- UI State / Actions --------------------

data class CartItemUiState(
    val id: Int,
    val name: String,            // 제품명
    val detail: String,          // 제품 상세명
    val priceText: String,       // 본문 가격 텍스트 (위쪽 "가격")
    val optionText: String,      // "기종/개수" 같은 옵션 텍스트
    val totalPriceText: String   // 오른쪽 하단 "가격"
)

data class CartPriceSummaryUiState(
    val productAmountText: String,  // 상품금액
    val shippingFeeText: String,    // 배송비
    val orderAmountText: String     // 주문금액
)

data class CartOrderInfoUiState(
    val totalQuantityText: String,       // 총 수량
    val totalProductAmountText: String,  // 총 상품금액
    val totalShippingFeeText: String     // 총 배송비
)

data class CartScreenState(
    val items: List<CartItemUiState>,
    val priceSummary: CartPriceSummaryUiState,
    val orderInfo: CartOrderInfoUiState
)

data class CartScreenActions(
    val onBackClick: () -> Unit = {},
    val onBottomNavClick: (String) -> Unit = {},
    val onChangeQuantity: (CartItemUiState) -> Unit = {},
    val onRemoveItem: (CartItemUiState) -> Unit = {}
)

// -------------------- 외부에서 쓰는 CartScreen --------------------

@Composable
fun CartScreen(
    state: CartScreenState,
    actions: CartScreenActions,
    currentRoute: String = BottomNavItem.Order.route
) {
    Scaffold(

        // 🔥 여기가 딱 하나 바뀐 부분
        topBar = {
            Column {
                Spacer(modifier = Modifier.height(44.dp))   // ← 상태바 영역

                CartTopBar(onBackClick = actions.onBackClick)
            }
        },

        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onItemClick = actions.onBottomNavClick
            )
        },
        containerColor = DevWhite
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DevWhite)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(16.dp))

                // 상단 Dev Mart 타이틀 + 구분선
                Text(
                    text = "Dev Mart",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DevDarkneyvy
                    )
                )

                Spacer(Modifier.height(4.dp))

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = DevDarkneyvy.copy(alpha = 0.8f),
                    thickness = 1.dp
                )

                Spacer(Modifier.height(16.dp))

                // 장바구니 상품 리스트
                state.items.forEachIndexed { index, item ->
                    CartItemRow(
                        item = item,
                        onChangeQuantity = { actions.onChangeQuantity(item) },
                        onRemoveItem = { actions.onRemoveItem(item) }
                    )

                    if (index != state.items.lastIndex) {
                        Spacer(Modifier.height(12.dp))
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            color = DevDarkgray.copy(alpha = 0.4f),
                            thickness = 1.dp
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }

                Spacer(Modifier.height(16.dp))

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = DevDarkneyvy.copy(alpha = 0.8f),
                    thickness = 1.dp
                )

                Spacer(Modifier.height(16.dp))

                // 금액 요약 카드 (상품금액 / 배송비 / 주문금액)
                CartPriceSummaryCard(state.priceSummary)

                Spacer(Modifier.height(16.dp))

                // 주문 정보 카드
                CartOrderInfoCard(state.orderInfo)

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

// -------------------- 상단 헤더 --------------------

@Composable
private fun CartTopBar(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DevWhite)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(59.dp),
        ) {
            // 왼쪽 뒤로가기 버튼 (기능 있음)
            Text(
                text = "←",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DevBlack
                ),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 20.dp)
                    .clickable { onBackClick() }
            )

            // 가운데 타이틀 (아무 기능 없음)
            Text(
                text = "장바구니",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = DevBlack
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // 아래 구분선
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = DevDarkgray.copy(alpha = 0.4f),
            thickness = 1.dp
        )
    }
}

// -------------------- 장바구니 개별 상품 Row --------------------

@Composable
private fun CartItemRow(
    item: CartItemUiState,
    onChangeQuantity: () -> Unit,
    onRemoveItem: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // 이미지 자리 (80x80)
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(DevGray, RoundedCornerShape(8.dp))
            )

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = DevBlack
                    )
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = item.detail,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = DevBlack.copy(alpha = 0.8f)
                    )
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = item.priceText,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                        color = DevBlack
                    )
                )
            }

            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable { onRemoveItem() },
                contentAlignment = Alignment.TopEnd
            ) {
                Text(
                    text = "✕",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = DevBlack
                    )
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.optionText,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = DevBlack.copy(alpha = 0.8f)
                ),
                modifier = Modifier.weight(1f)
            )

            OutlinedButton(
                onClick = onChangeQuantity,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                shape = RoundedCornerShape(999.dp)
            ) {
                Text(
                    text = "수량변경",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        color = DevBlack
                    )
                )
            }

            Spacer(Modifier.width(12.dp))

            Text(
                text = item.totalPriceText,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium,
                    color = DevBlack
                )
            )
        }
    }
}

// -------------------- 가격 요약 카드 --------------------

@Composable
private fun CartPriceSummaryCard(
    summary: CartPriceSummaryUiState
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = DevGray,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 0.dp,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            PriceRow(label = "상품금액", value = summary.productAmountText)
            Spacer(Modifier.height(4.dp))
            PriceRow(label = "배송비", value = summary.shippingFeeText)
            Spacer(Modifier.height(4.dp))
            PriceRow(label = "주문금액", value = summary.orderAmountText)
        }
    }
}

@Composable
private fun PriceRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = DevBlack
            )
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium,
                color = DevBlack
            )
        )
    }
}

// -------------------- 주문정보 카드 --------------------

@Composable
private fun CartOrderInfoCard(
    info: CartOrderInfoUiState
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = DevWhite,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 0.dp,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Text(
                text = "주문정보",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium,
                    color = DevBlack
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(8.dp))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = DevDarkneyvy.copy(alpha = 0.4f),
                thickness = 1.dp
            )

            Spacer(Modifier.height(8.dp))

            OrderInfoRow("총 수량", info.totalQuantityText)
            OrderInfoRow("총 상품금액", info.totalProductAmountText)
            OrderInfoRow("총 배송비", info.totalShippingFeeText)
        }
    }
}

@Composable
private fun OrderInfoRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = DevBlack
            )
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                color = DevBlack
            )
        )
    }
}

// -------------------- Preview --------------------

@Preview(showBackground = true)
@Composable
fun PreviewCartScreen() {
    val dummyItems = listOf(
        CartItemUiState(
            id = 1,
            name = "제품명",
            detail = "제품 상세명",
            priceText = "가격",
            optionText = "기종/개수",
            totalPriceText = "가격"
        ),
        CartItemUiState(
            id = 2,
            name = "제품명",
            detail = "제품 상세명",
            priceText = "가격",
            optionText = "기종/개수",
            totalPriceText = "가격"
        )
    )

    val priceSummary = CartPriceSummaryUiState(
        productAmountText = "가격",
        shippingFeeText = "가격",
        orderAmountText = "가격"
    )

    val orderInfo = CartOrderInfoUiState(
        totalQuantityText = "개수",
        totalProductAmountText = "가격",
        totalShippingFeeText = "배송비"
    )

    MaterialTheme {
        CartScreen(
            state = CartScreenState(
                items = dummyItems,
                priceSummary = priceSummary,
                orderInfo = orderInfo
            ),
            actions = CartScreenActions(),
            currentRoute = BottomNavItem.Order.route
        )
    }
}