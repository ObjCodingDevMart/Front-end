package com.example.devmart.ui.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devmart.R
import com.example.devmart.ui.component.BottomNavigationBar
import com.example.devmart.ui.component.BottomNavItem   // ⭐ currentRoute 기본값에 사용

// 색상 정의
private val Dark = Color(0xFF30343F)
private val HeaderIconColor = Color(0xFF1E2749)
private val DividerGray = Color(0xFF898989).copy(alpha = 0.2f)
private val ScreenBackground = Color(0xFFFAFAFF)

// -------------------- State & Event 객체 --------------------

data class UserUiState(
    val nickname: String,
    val emailLocal: String,
    val emailDomain: String,
    val point: Int,
    val shippingCount: Int,
    val likedCount: Int,
)

data class UserScreenActions(
    val onEditProfile: () -> Unit,
    val onBackClick: () -> Unit = {},
    val onSearchClick: () -> Unit = {},
)

// -------------------- 외부에서 부르는 UserScreen (그대로 써도 됨) --------------------

@Composable
fun UserScreen(
    nickname: String,
    emailLocal: String,
    emailDomain: String,
    point: Int,
    shippingCount: Int,
    likedCount: Int,
    onEditProfile: () -> Unit,
    onBackClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    currentRoute: String = BottomNavItem.MyPage.route,          // ⭐ 기본값: MyPage
    onBottomNavClick: (String) -> Unit = {}                     // ⭐ 클릭 시 동작
) {
    val state = UserUiState(
        nickname = nickname,
        emailLocal = emailLocal,
        emailDomain = emailDomain,
        point = point,
        shippingCount = shippingCount,
        likedCount = likedCount
    )

    val actions = UserScreenActions(
        onEditProfile = onEditProfile,
        onBackClick = onBackClick,
        onSearchClick = onSearchClick
    )

    UserScreen(
        state = state,
        actions = actions,
        currentRoute = currentRoute,
        onBottomNavClick = onBottomNavClick
    )
}

// -------------------- Scaffold 적용된 실제 화면 --------------------

@Composable
fun UserScreen(
    state: UserUiState,
    actions: UserScreenActions,
    currentRoute: String,
    onBottomNavClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            UserTopBar(
                title = "Dev Mart",
                onBackClick = actions.onBackClick,
                onSearchClick = actions.onSearchClick
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onItemClick = onBottomNavClick
            )
        },
        containerColor = ScreenBackground
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ScreenBackground)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(24.dp))

                UserProfileSection(
                    nickname = state.nickname,
                    emailLocal = state.emailLocal,
                    emailDomain = state.emailDomain
                )

                Spacer(Modifier.height(32.dp))

                UserSummarySection(
                    point = state.point,
                    shippingCount = state.shippingCount,
                    likedCount = state.likedCount
                )

                Spacer(Modifier.height(40.dp))

                UserMenuSection(onEditProfile = actions.onEditProfile)

                Spacer(Modifier.height(16.dp)) // bottomBar와 살짝 간격
            }
        }
    }
}

// -------------------- 상단 헤더 --------------------

@Composable
private fun UserTopBar(
    title: String,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Text("←", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = HeaderIconColor)
        }

        Spacer(Modifier.width(10.dp))

        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = HeaderIconColor
        )

        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(38.dp)
                .clickable { onSearchClick() },
            contentAlignment = Alignment.Center
        ) {
            Text("돋보기", fontSize = 20.sp, color = HeaderIconColor)
        }
    }
}

// -------------------- 프로필 섹션 --------------------

@Composable
private fun UserProfileSection(
    nickname: String,
    emailLocal: String,
    emailDomain: String,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_profile_default),
            contentDescription = null,
            modifier = Modifier
                .size(135.dp)
                .clip(CircleShape)
        )
    }

    Spacer(Modifier.height(20.dp))

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${nickname}님, 반가워요!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Dark
        )
    }

    Spacer(Modifier.height(8.dp))

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("$emailLocal@", fontSize = 14.sp, color = Color(0xFF898989))
            Spacer(Modifier.width(4.dp))
            Text(emailDomain, fontSize = 14.sp, color = Color(0xFF898989))
        }
    }
}

// -------------------- 요약 정보 --------------------

@Composable
private fun UserSummarySection(
    point: Int,
    shippingCount: Int,
    likedCount: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            UserInfoSmall("포인트", "${point}P")
        }
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            UserInfoSmall("배송중", "$shippingCount")
        }
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            UserInfoSmall("좋아요 한 상품", "$likedCount")
        }
    }
}

// -------------------- 메뉴 섹션 --------------------

@Composable
private fun UserMenuSection(
    onEditProfile: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        MenuItem("구매내역") {}
        MenuItem("장바구니") {}
        MenuItem("좋아요") {}
        MenuItem("회원 정보 수정") { onEditProfile() }
    }
}

// 작은 정보 UI
@Composable
fun UserInfoSmall(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, fontSize = 12.sp, color = Dark.copy(alpha = 0.7f))
        Text(value, fontSize = 14.sp, color = Dark)
    }
}

// 메뉴 항목 UI
@Composable
fun MenuItem(
    text: String,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 18.dp)
        ) {
            Text(text, fontSize = 12.sp, color = Dark)
        }

        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = DividerGray,
            thickness = 1.dp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserScreen() {
    MaterialTheme {
        UserScreen(
            nickname = "젊은달팽이#8",
            emailLocal = "gildong",
            emailDomain = "kau.kr",
            point = 0,
            shippingCount = 0,
            likedCount = 0,
            onEditProfile = {},
            // 프리뷰에서는 기본값이라 currentRoute/onBottomNavClick 안 넘겨도 됨
        )
    }
}