package com.example.devmart.ui.orderhistory

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devmart.ui.component.BottomNavigationBar
import com.example.devmart.ui.component.BottomNavItem
import com.example.devmart.ui.theme.*

data class OrderHistoryUiState(
    val orderGroups: List<OrderGroupUi> = emptyList()
)

data class OrderGroupUi(
    val orderDateLabel: String,
    val items: List<OrderSummaryUi>
)

data class OrderSummaryUi(
    val orderId: String,
    val brandName: String,
    val productName: String,
    val optionText: String,
    val priceText: String
)

@Composable
fun OrderHistoryScreen(
    uiState: OrderHistoryUiState,
    onBack: () -> Unit = {},
    onBottomNavClick: (String) -> Unit = {},
    onTrackDelivery: (OrderSummaryUi) -> Unit = {},
    onReorder: (OrderSummaryUi) -> Unit = {},
) {
    Scaffold(
        topBar = {
            Column {
                // 장바구니와 동일한 상태바 영역
                Spacer(modifier = Modifier.height(44.dp))
                OrderHistoryTopBar(onBackClick = onBack)
            }
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = BottomNavItem.MyPage.route, // 마이페이지 선택 상태
                onItemClick = onBottomNavClick
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

                // 상단 Dev Mart 타이틀 + 구분선 (Cart와 동일 구조)
                Text(
                    text = "Dev Mart",
                    //fontFamily = DevFonts.KakaoBigSans,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = DevDarkneyvy
                )

                Spacer(Modifier.height(4.dp))

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = DevDarkneyvy.copy(alpha = 0.8f),
                    thickness = 1.dp
                )

                Spacer(Modifier.height(16.dp))

                // 날짜별 상품 리스트
                uiState.orderGroups.forEach { group ->
                    Text(
                        text = group.orderDateLabel,
                        //fontFamily = DevFonts.KakaoBigSans,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = DevBlack
                    )

                    Spacer(Modifier.height(8.dp))

                    group.items.forEach { item ->
                        OrderSummaryCard(
                            item = item,
                            onTrackDelivery = { onTrackDelivery(item) },
                            onReorder = { onReorder(item) }
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun OrderHistoryTopBar(
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
            // 왼쪽 뒤로가기
            Text(
                text = "←",
                //fontFamily = DevFonts.KakaoBigSans,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = DevBlack,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 20.dp)
                    .clickable { onBackClick() }
            )

            // 가운데 타이틀
            Text(
                text = "구매내역",
                //fontFamily = DevFonts.KakaoBigSans,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = DevBlack,
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

@Composable
fun OrderSummaryCard(
    item: OrderSummaryUi,
    onTrackDelivery: () -> Unit,
    onReorder: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = DevWhite,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // 상품 사진 영역
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .background(DevGray, RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.brandName,
                        //fontFamily = DevFonts.KakaoBigSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = DevBlack
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.productName,
                        //fontFamily = DevFonts.KakaoBigSans,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        color = DevBlack,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.optionText,
                        //fontFamily = DevFonts.KakaoBigSans,
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp,
                        color = DevDarkgray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = item.priceText,
                        //fontFamily = DevFonts.KakaoBigSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = DevBlack
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onTrackDelivery,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "배송 조회",
                        //fontFamily = DevFonts.KakaoBigSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = DevBlack
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = onReorder,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "재구매",
                        //fontFamily = DevFonts.KakaoBigSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = DevBlack
                    )
                }
            }
        }
    }
}


private fun previewOrderHistoryState() = OrderHistoryUiState(
    orderGroups = listOf(
        OrderGroupUi(
            orderDateLabel = "25.08.17(일)",
            items = listOf(
                OrderSummaryUi(
                    orderId = "1",
                    brandName = "Dev",
                    productName = "황금 마우스",
                    optionText = "BLACK / 1개",
                    priceText = "166,500원"
                ),
                OrderSummaryUi(
                    orderId = "2",
                    brandName = "커먼유니크",
                    productName = "보이 크롭 반팔티셔츠",
                    optionText = "그레이 / L / 1개",
                    priceText = "29,690원"
                )
            )
        )
    )
)

@Preview(showBackground = true)
@Composable
private fun PreviewOrderHistory() {
    MaterialTheme {
        OrderHistoryScreen(
            uiState = previewOrderHistoryState(),
            onBottomNavClick = {}
        )
    }
}