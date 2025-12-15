package com.example.devmart.ui.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devmart.R
import com.example.devmart.ui.component.BackButton
import com.example.devmart.ui.theme.DevBlack
import com.example.devmart.ui.theme.DevDarkgray
import com.example.devmart.ui.theme.DevGray
import com.example.devmart.ui.theme.DevWhite


// -------------------- 메인 화면 --------------------

@Composable
fun UserScreen(
    nickname: String,
    emailLocal: String,
    emailDomain: String,
    point: Int,
    shippingCount: Int,
    likedCount: Int,
    onEditProfile: () -> Unit,          // 회원 정보 수정 눌렀을 때 호출
    onBackClick: () -> Unit = {},       // 필요하면 뒤로가기 네비게이션
    onOrderHistoryClick: () -> Unit = {}, // 구매내역
    onCartClick: () -> Unit = {},       // 장바구니
    onLikedClick: () -> Unit = {},      // 좋아요
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DevWhite)
    ) {
        // 상태바 높이
        Spacer(Modifier.height(44.dp))

        // 헤더 (← Dev Mart)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(onClick = onBackClick)

            Text(
                text = "Dev Mart",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DevBlack
                )
        }

        // 프로필 이미지
        Spacer(Modifier.height(40.dp))

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

        // 닉네임 (나중에 변경되면 여기 값만 바뀌도록)
        Spacer(Modifier.height(20.dp))

        Text(
            text = "${nickname}님, 반가워요!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DevBlack,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(8.dp))

        // 이메일 (local + domain)
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("$emailLocal@", fontSize = 14.sp, color = DevDarkgray)
            Spacer(Modifier.width(4.dp))
            Text(emailDomain, fontSize = 14.sp, color = DevDarkgray)
        }

        // 포인트 / 배송중 / 좋아요
        Spacer(Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                UserInfoSmall("포인트", "${point}P")
            }
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                UserInfoSmall("배송중", "$shippingCount")
            }
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                UserInfoSmall("좋아요 한 상품", "$likedCount")
            }
        }

        // 메뉴
        Spacer(Modifier.height(40.dp))

        MenuItem("구매내역") { onOrderHistoryClick() }
        MenuItem("장바구니") { onCartClick() }
        MenuItem("좋아요") { onLikedClick() }
        MenuItem("회원 정보 수정") { onEditProfile() }
    }
}

// 작은 정보 박스
@Composable
fun UserInfoSmall(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, fontSize = 12.sp, color = DevDarkgray)
        Text(value, fontSize = 14.sp, color = DevBlack)
    }
}

// 메뉴 + 구분선 (항상 선 표시)
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
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = text,
                fontSize = 12.sp,
                color = DevBlack,
                modifier = Modifier.padding(vertical = 18.dp)
            )
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = DevGray,
            thickness = 1.dp
        )
    }
}

// Preview용 더미 데이터
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
            onEditProfile = {}
        )
    }
}