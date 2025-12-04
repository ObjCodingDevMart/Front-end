package com.example.devmart.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devmart.ui.component.BottomNavigationBar
import com.example.devmart.ui.component.BottomNavItem
import com.example.devmart.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> Top100SearchScreen(
    products: List<T>,
    productCard: @Composable (T) -> Unit,
    currentRoute: String,
    onBottomNavClick: (String) -> Unit,

    // 동적 데이터로 변경됨
    recentSearches: List<String>,
    popularKeywords: List<String>,

    modifier: Modifier = Modifier,
    onSearchSubmit: (String) -> Unit = {},
    onKeywordClick: (String) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DevWhite)
                    .padding(horizontal = 16.dp, vertical = 26.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(DevGray.copy(alpha = 0.4f))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                text = "DevMart.search.IsTrue()",
                                fontSize = 14.sp,
                                color = DevDarkgray,
                                fontFamily = DevFonts.KakaoBigSans
                            )
                        },
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 14.sp,
                            color = DevBlack,
                            fontFamily = DevFonts.KakaoBigSans
                        ),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            cursorColor = DevDarkneyvy,
                            focusedTextColor = DevBlack,
                            unfocusedTextColor = DevBlack,
                            disabledTextColor = DevDarkgray
                        )
                    )

                    IconButton(onClick = { onSearchSubmit(query) }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "검색",
                            tint = DevDarkneyvy
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // X 버튼: 검색창 비우기
                IconButton(onClick = { query = "" }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "검색창 비우기",
                        tint = DevDarkgray
                    )
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onItemClick = onBottomNavClick
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(DevWhite),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ----- 최근 검색어 -----
            item {
                Text(
                    text = "최근 검색어",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DevBlack,
                    fontFamily = DevFonts.KakaoBigSans
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    recentSearches.forEach {
                        Text(
                            text = it,
                            fontSize = 13.sp,
                            color = DevBlack,
                            fontFamily = DevFonts.KakaoBigSans,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // ----- 인기 키워드 -----
            item {
                Text(
                    text = "인기 키워드",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DevBlack,
                    fontFamily = DevFonts.KakaoBigSans
                )
            }

            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    popularKeywords.forEach { keyword ->
                        KeywordChip(label = keyword, onClick = { onKeywordClick(keyword) })
                    }
                }
            }

            // ----- TOP 100 -----
            item {
                Text(
                    text = "인기 TOP 100",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DevBlack,
                    fontFamily = DevFonts.KakaoBigSans
                )
            }

            // ----- 3열 상품 카드 -----
            val rows = products.chunked(3)
            items(rows) { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { product ->
                        Box(modifier = Modifier.weight(1f)) {
                            productCard(product)
                        }
                    }
                    if (rowItems.size < 3) {
                        repeat(3 - rowItems.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KeywordChip(
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(DevGray.copy(alpha = 0.4f))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = DevBlack,
            fontFamily = DevFonts.KakaoBigSans
        )
    }
}

@Composable
private fun DummyProductCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f) // 정사각형
                .clip(RoundedCornerShape(8.dp))
                .background(DevGray.copy(alpha = 0.6f))
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Json",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = DevBlack,
            fontFamily = DevFonts.KakaoBigSans
        )
        Text(
            text = "Json 명함 제작",
            fontSize = 11.sp,
            color = DevDarkgray,
            fontFamily = DevFonts.KakaoBigSans
        )
        Text(
            text = "60,000원",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = DevBlack,
            fontFamily = DevFonts.KakaoBigSans
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Top100SearchScreenPreview() {
    DevMartTheme {
        val dummyProducts = List(12) { "dummy-$it" }

        Top100SearchScreen(
            products = dummyProducts,
            productCard = { DummyProductCard() },
            currentRoute = BottomNavItem.Top100.route,
            onBottomNavClick = {},
            recentSearches = listOf("AI", "Kotlin", "Compose"),
            popularKeywords = listOf("키보드", "노트북", "의자", "마이크")
        )
    }
}
