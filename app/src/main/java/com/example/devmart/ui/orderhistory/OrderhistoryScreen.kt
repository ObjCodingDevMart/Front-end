package com.example.devmart.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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

//model
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

//screen
@Composable
fun OrderHistoryScreen(
    uiState: OrderHistoryUiState,
    onBack: () -> Unit = {},
    onBottomNavClick: (String) -> Unit = {},
    onReorder: (OrderSummaryUi) -> Unit = {},
) {
    Scaffold(
        topBar = {
            OrderHistoryTopBar(onBackClick = onBack)
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = BottomNavItem.MyPage.route,
                onItemClick = onBottomNavClick
            )
        },
        containerColor = DevWhite
    ) { innerPadding ->

        OrderHistoryContent(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(DevWhite),
            uiState = uiState,
            onReorder = onReorder
        )
    }
}
@Composable
private fun OrderHistoryContent(
    modifier: Modifier,
    uiState: OrderHistoryUiState,
    onReorder: (OrderSummaryUi) -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(Modifier.height(16.dp))

        DevMartHeader()

        Spacer(Modifier.height(16.dp))

        OrderHistoryList(
            orderGroups = uiState.orderGroups,
            onReorder = onReorder
        )
    }
}
@Composable
private fun DevMartHeader() {
    Text(
        text = "Dev Mart",
        fontFamily = DevFonts.KakaoBigSans,
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
}
@Composable
private fun OrderHistoryList(
    orderGroups: List<OrderGroupUi>,
    onReorder: (OrderSummaryUi) -> Unit
) {
    orderGroups.forEach { group ->
        OrderGroupSection(
            group = group,
            onReorder = onReorder
        )
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun OrderGroupSection(
    group: OrderGroupUi,
    onReorder: (OrderSummaryUi) -> Unit
) {
    Text(
        text = group.orderDateLabel,
        fontFamily = DevFonts.KakaoBigSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        color = DevBlack
    )

    Spacer(Modifier.height(8.dp))

    group.items.forEach { item ->
        OrderSummaryCard(
            item = item,
            onReorder = { onReorder(item) }
        )
        Spacer(Modifier.height(16.dp))
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
        Spacer(modifier = Modifier.height(44.dp)) // 상태바 패딩

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        ) {

            Text(
                text = "←",
                fontFamily = DevFonts.KakaoBigSans,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = DevBlack,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 20.dp)
                    .clickable { onBackClick() }
            )

            Text(
                text = "구매내역",
                fontFamily = DevFonts.KakaoBigSans,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = DevBlack,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        HorizontalDivider(
            color = DevDarkgray.copy(alpha = 0.4f),
            thickness = 1.dp
        )
    }
}

@Composable
fun OrderSummaryCard(
    item: OrderSummaryUi,
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
                .padding(12.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .background(DevGray, RoundedCornerShape(8.dp))
                )

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        text = item.brandName,
                        fontFamily = DevFonts.KakaoBigSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = DevBlack
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = item.productName,
                        fontFamily = DevFonts.KakaoBigSans,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        color = DevBlack,
                        maxLines = 2
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = item.optionText,
                        fontFamily = DevFonts.KakaoBigSans,
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp,
                        color = DevDarkgray
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = item.priceText,
                        fontFamily = DevFonts.KakaoBigSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = DevBlack
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onReorder,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "재구매",
                    fontFamily = DevFonts.KakaoBigSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = DevBlack
                )
            }
        }
    }
}

private fun previewState() = OrderHistoryUiState(
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
            uiState = previewState(),
            onBottomNavClick = {}
        )
    }
}

/* ============================================================
 *  Preview 1 — 전체 화면 미리보기 (상품 2개)
 * ============================================================ */

@Preview(showBackground = true, name = "전체 화면 Preview (2개 아이템)")
@Composable
private fun PreviewOrderHistoryScreen() {
    MaterialTheme {
        OrderHistoryScreen(
            uiState = OrderHistoryUiState(
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
            ),
            onBottomNavClick = {}
        )
    }
}
