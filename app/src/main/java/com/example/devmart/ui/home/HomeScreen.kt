package com.example.devmart.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.devmart.domain.model.Product
import com.example.devmart.ui.component.BottomNavigationBar
import com.example.devmart.ui.component.ProductCard
import com.example.devmart.ui.theme.DevBlack
import com.example.devmart.ui.theme.DevGray
import com.example.devmart.ui.theme.DevMartTheme
import com.example.devmart.ui.theme.DevNeyvy
import com.example.devmart.ui.theme.DevWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    openDetail: (String) -> Unit = {},
    detailId: String? = null,
    onNavigateToRoute: (String) -> Unit = {}
) {
    // 임시 데이터
    val products = remember {
        listOf(
            Product("1", "Json","Json 명함 제작", 60000, null),
            Product("1", "Json","Json 명함 제작", 60000, null),
            Product("1", "Json","Json 명함 제작", 60000, null),
            Product("1", "Json","Json 명함 제작", 60000, null),
            Product("1", "Json","Json 명함 제작", 60000, null),
            Product("1", "Json","Json 명함 제작", 60000, null),
        )
    }
    
    val categories = listOf("전체", "마우스", "키보드", "키캡", "마우스패드")
    var selectedCategory by remember { mutableStateOf("전체") }
    var currentRoute by remember { mutableStateOf("home") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Dev Mart",
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp
                    )
                },


                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DevWhite
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onItemClick = { route ->
                    currentRoute = route
                    onNavigateToRoute(route)
                }
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 0.dp,
                bottom = 16.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 배너 (전체 너비 차지)
            item(span = { GridItemSpan(2) }) {
                ImageSliderBanner()
            }
            
            // 필터 칩 (전체 너비 차지)
            item(span = { GridItemSpan(2) }) {
                FilterChips(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
            }
            
            // 상품 그리드
            items(products) { product ->
                ProductCard(
                    product = product,
                    onClick = { openDetail(product.id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ImageSliderBanner() {
    // 배너 이미지 URL 리스트 (나중에 ViewModel이나 API에서 가져올 수 있음)
    val bannerImages = remember {
        listOf(
            "https://via.placeholder.com/400x280/1976D2/FFFFFF?text=Banner+1",
            "https://via.placeholder.com/400x280/388E3C/FFFFFF?text=Banner+2",
            "https://via.placeholder.com/400x280/F57C00/FFFFFF?text=Banner+3",
            "https://via.placeholder.com/400x280/7B1FA2/FFFFFF?text=Banner+4"
        )
    }
    
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var currentPage by remember { mutableStateOf(0) }
    
    // 스크롤 위치에 따라 현재 페이지 업데이트
    LaunchedEffect(listState.firstVisibleItemIndex) {
        currentPage = listState.firstVisibleItemIndex
    }
    
    // 자동 슬라이드 (5초마다)
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            if (currentPage < bannerImages.size - 1) {
                currentPage++
                listState.animateScrollToItem(currentPage)
            } else {
                currentPage = 0
                listState.animateScrollToItem(0)
            }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {
        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        ) {
            itemsIndexed(bannerImages) { index, imageUrl ->
                Box(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .height(256.dp)
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "배너 이미지 ${index + 1}",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { 
                                currentPage = index
                                coroutineScope.launch {
                                    listState.animateScrollToItem(index)
                                }
                                /* 배너 클릭 처리 */ 
                            },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        
        // 페이지 인디케이터
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(bannerImages.size) { index ->
                Box(
                    modifier = Modifier
                        .width(if (currentPage == index) 24.dp else 8.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (currentPage == index) 
                                Color.White
                            else 
                                Color.White.copy(alpha = 0.5f)
                        )
                )
            }
        }
    }
}

@Composable
fun FilterChips(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 0.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                selected = category == selectedCategory,
                onClick = { onCategorySelected(category) },
                label = { Text(category, fontSize = 13.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DevNeyvy,
                    selectedLabelColor = DevWhite,
                    containerColor = DevGray,
                    labelColor = DevBlack
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }


}
@Preview(name = "Login 360x800", widthDp = 360, heightDp = 800, showBackground = true)
@Preview(name = "Pixel 7a", device = Devices.PIXEL_7A, showBackground = true)
@Composable
private fun PreviewHome() {
    DevMartTheme {
        HomeScreen()
    }
}
