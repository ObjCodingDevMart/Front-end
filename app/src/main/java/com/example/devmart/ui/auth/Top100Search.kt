package com.example.devmart.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
import com.example.devmart.ui.theme.DevMartTheme
import com.example.devmart.ui.theme.DevWhite

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> Top100SearchScreen(
    allProducts: List<T>,
    productCard: @Composable (T) -> Unit,
    currentRoute: String,
    onBottomNavClick: (String) -> Unit,
    recentSearches: List<String>,
    popularKeywords: List<String>,
    modifier: Modifier = Modifier,
    searchResults: List<T> = emptyList(),
    query: String = "",
    onQueryChange: (String) -> Unit = {},
    onClearQuery: () -> Unit = {},
    onSearchSubmit: () -> Unit = {},
    onKeywordClick: (String) -> Unit = {}
) {
    val hasSearchResults = query.isNotEmpty() && searchResults.isNotEmpty()

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
                        onValueChange = onQueryChange,
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

                    IconButton(onClick = onSearchSubmit) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "검색",
                            tint = DevDarkneyvy
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // X 버튼: 검색창 비우기
                IconButton(onClick = onClearQuery) {
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
            if (recentSearches.isNotEmpty()) {
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
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        recentSearches.forEach { keyword ->
                            KeywordChip(
                                label = keyword,
                                onClick = { onKeywordClick(keyword) },
                                backgroundColor = DevDarkneyvy.copy(alpha = 0.15f)
                            )
                        }
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

            // ----- 검색 결과 -----
            if (hasSearchResults) {
                item {
                    Text(
                        text = "검색 결과 (${searchResults.size}개)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DevDarkneyvy,
                        fontFamily = DevFonts.KakaoBigSans
                    )
                }

                val searchRows = searchResults.chunked(3)
                items(searchRows) { rowItems ->
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
                
                // 검색 결과와 TOP 100 사이 구분선
                item {
                    Spacer(modifier = Modifier.height(8.dp))
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
            val rows = allProducts.chunked(3)
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
    onClick: () -> Unit,
    backgroundColor: Color = DevGray.copy(alpha = 0.4f)
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
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
            allProducts = dummyProducts,
            productCard = { DummyProductCard() },
            currentRoute = BottomNavItem.Top100.route,
            onBottomNavClick = {},
            recentSearches = listOf("AI", "Kotlin", "Compose"),
            popularKeywords = listOf("키보드", "노트북", "의자", "마이크"),
            searchResults = listOf("result-1", "result-2"),
            query = "검색어",
            onQueryChange = {},
            onClearQuery = {},
            onSearchSubmit = {},
            onKeywordClick = {}
        )
    }
}
