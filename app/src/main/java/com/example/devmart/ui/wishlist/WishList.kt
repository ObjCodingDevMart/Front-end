package com.example.devmart.ui.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.devmart.ui.component.BottomNavigationBar
import com.example.devmart.ui.component.BottomNavItem
import com.example.devmart.ui.theme.DevBlack
import com.example.devmart.ui.theme.DevDarkgray
import com.example.devmart.ui.theme.DevDarkneyvy
import com.example.devmart.ui.theme.DevGray
import com.example.devmart.ui.theme.DevMartTheme
import com.example.devmart.ui.theme.DevWhite


data class WishlistItemUi(
    val id: Long,
    val brand: String,
    val name: String,
    val price: String,
    val imageUrl: String? = null
)

data class WishlistScreenState(
    val items: List<WishlistItemUi> = emptyList()
)

data class WishlistScreenActions(
    val onBackClick: () -> Unit = {},
    val onBottomNavClick: (String) -> Unit = {},
    val onItemClick: (WishlistItemUi) -> Unit = {}
)

/* ───────────────── 화면 ───────────────── */

@Composable
fun WishlistScreen(
    state: WishlistScreenState,
    actions: WishlistScreenActions,
    currentRoute: String = BottomNavItem.MyPage.route
) {
    Scaffold(
        topBar = {
            WishlistTopBar(
                onBackClick = actions.onBackClick
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onItemClick = actions.onBottomNavClick
            )
        },
        containerColor = DevWhite
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(DevWhite)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(16.dp))

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

            Spacer(Modifier.height(24.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(state.items, key = { it.id }) { item ->
                    WishlistProductItem(
                        item = item,
                        onClick = { actions.onItemClick(item) }
                    )
                }
            }
        }
    }
}

/* ───────────────── 상단바 (수정됨) ───────────────── */

@Composable
fun WishlistTopBar(
    onBackClick: () -> Unit,
) {
    Column {

        Spacer(modifier = Modifier.height(44.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 왼쪽 Back
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = DevBlack
                )
            }

            // 중앙 Title
            Text(
                text = "좋아요",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DevDarkneyvy
                )
            )

            // 오른쪽 빈 아이콘 (중앙 정렬 맞추기용)
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = DevWhite.copy(alpha = 0f)
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = DevDarkneyvy.copy(alpha = 0.8f),
            thickness = 1.dp
        )
    }
}

/* ───────────────── Item Card ───────────────── */

@Composable
private fun WishlistProductItem(
    item: WishlistItemUi,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.8f)
                .background(DevGray),
            contentAlignment = Alignment.Center
        ) {
            if (!item.imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = item.brand,
            fontSize = 12.sp,
            color = DevDarkgray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(2.dp))

        Text(
            text = item.name,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = DevBlack,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = item.price,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = DevBlack
        )
    }
}

/* ───────────────── Preview ───────────────── */

@Preview(showBackground = true)
@Composable
fun WishlistScreenPreview() {
    DevMartTheme {
        WishlistScreen(
            state = WishlistScreenState(
                items = listOf(
                    WishlistItemUi(1, "수아레", "데일리 헨리넥 니트 - 5 COLOR", "29,900원"),
                    WishlistItemUi(2, "에이카화이트", "EVERYDAY AECA CLOVER HOODIE...", "63,200원"),
                    WishlistItemUi(3, "수아레", "데일리 라운드 니트 - 12 COLOR", "29,900원"),
                    WishlistItemUi(4, "무드인사이드", "노먼 브러쉬 노르딕 니트_6Color", "45,900원"),
                    WishlistItemUi(5, "테이크이지", "빈티지 오버 듀플린 체크 셔츠 (차콜)", "33,900원"),
                    WishlistItemUi(6, "도프제이슨", "[데일리룩 PICK] 솔리드 무톤 자켓", "169,000원"),
                )
            ),
            actions = WishlistScreenActions(),
        )
    }
}