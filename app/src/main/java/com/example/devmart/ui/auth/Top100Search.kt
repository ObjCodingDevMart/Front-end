package com.example.devmart.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.devmart.ui.theme.DevBlack
import com.example.devmart.ui.theme.DevDarkgray
import com.example.devmart.ui.theme.DevDarkneyvy
import com.example.devmart.ui.theme.DevFonts
import com.example.devmart.ui.theme.DevGray
import com.example.devmart.ui.theme.DevWhite
import com.example.devmart.ui.theme.DevMartTheme   // <- 프로젝트 테마 이름 다르면 수정

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> Top100SearchScreen(
    products: List<T>,                     // Top100 상품 리스트
    productCard: @Composable (T) -> Unit, // ProductCard를 그리는 람다
    currentRoute: String,                 // 현재 선택된 탭 (예: "top100")
    onBottomNavClick: (String) -> Unit,   // 하단바 탭 클릭 시 호출
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onSearchSubmit: (String) -> Unit = {},
    onKeywordClick: (String) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }

    val recentSearches = listOf(
        "GPT-5 초보자용 프롬프트 엔지니어링",
        "카페인 3000배"
    )

    val popularKeywords = listOf(
        "마우스", "키보드", "스피커", "손목 보호 패드", "모니터 암",
        "키캡", "리무버블 스티커", "머그컵", "노트북 케이스", "후드티"
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onItemClick = onBottomNavClick
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(DevWhite),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 24.dp,
                bottom = 16.dp
            )
        ) {
            // ───── 상단 검색 바 + X 버튼 ─────
            item(span = { GridItemSpan(maxLineSpan) }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
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

                    IconButton(onClick = onCloseClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "닫기",
                            tint = DevDarkgray
                        )
                    }
                }
            }

            // ───── 최근 검색어 제목 ─────
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "최근 검색어",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DevBlack,
                    fontFamily = DevFonts.KakaoBigSans,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            // ───── 최근 검색어 리스트 ─────
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    recentSearches.forEach { text ->
                        Text(
                            text = text,
                            fontSize = 13.sp,
                            color = DevBlack,
                            fontFamily = DevFonts.KakaoBigSans,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // ───── 인기 키워드 제목 ─────
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "인기 키워드",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DevBlack,
                    fontFamily = DevFonts.KakaoBigSans,
                    modifier = Modifier.padding(top = 20.dp, bottom = 8.dp)
                )
            }

            // ───── 키워드 칩들 ─────
            item(span = { GridItemSpan(maxLineSpan) }) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    popularKeywords.forEach { keyword ->
                        KeywordChip(
                            label = keyword,
                            onClick = { onKeywordClick(keyword) }
                        )
                    }
                }
            }

            // ───── 인기 TOP 100 제목 ─────
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "인기 TOP 100",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DevBlack,
                    fontFamily = DevFonts.KakaoBigSans,
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                )
            }

            // ───── 실제 상품 카드 그리드 ─────
            items(products) { product ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    productCard(product)
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

/* ----- Preview용 더미 카드 ----- */

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
                .aspectRatio(1f)
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

/* ----- 실제 Preview ----- */

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Top100SearchScreenPreview() {
    DevMartTheme {   // <- 이름 다르면 프로젝트 테마 이름으로 변경
        val dummyProducts = List(9) { "dummy-$it" }

        Top100SearchScreen(
            products = dummyProducts,
            productCard = { DummyProductCard() },
            currentRoute = BottomNavItem.Top100.route,
            onBottomNavClick = {}
        )
    }
}
