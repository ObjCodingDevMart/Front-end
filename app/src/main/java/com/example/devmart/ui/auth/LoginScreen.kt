package com.example.devmart.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devmart.ui.theme.BUTTON_RADIUS
import com.example.devmart.ui.theme.DevBlack
import com.example.devmart.ui.theme.DevDarkgray
import com.example.devmart.ui.theme.DevDarkneyvy
import com.example.devmart.ui.theme.DevFonts.KakaoBigSans
import com.example.devmart.ui.theme.DevMartTheme
import com.example.devmart.ui.theme.DevWhite
import com.example.devmart.ui.theme.KakaoBlack
import com.example.devmart.ui.theme.KakaoYellow
import com.example.devmart.ui.theme.META_SIZE
import com.example.devmart.ui.theme.PADDING_H
import com.example.devmart.ui.theme.SOCIAL_HEIGHT
import com.example.devmart.ui.auth.LoginUiState


// --- Screen ---
@Composable
fun LoginScreen(
    state: LoginUiState = LoginUiState(),
    onClickKakao: () -> Unit = {}
) {
    Surface(modifier = Modifier.fillMaxSize(), color = DevWhite) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = PADDING_H),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 로고 영역
            Text(
                text = "Dev Mart", 
                color = DevDarkneyvy, 
                fontFamily = KakaoBigSans, 
                fontWeight = FontWeight.Bold, 
                fontSize = 36.sp
            )

            Spacer(Modifier.height(16.dp))
            
            // 서브 타이틀
            Text(
                text = "개발자를 위한 쇼핑몰",
                color = DevDarkgray,
                fontFamily = KakaoBigSans,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(160.dp))

            // 에러 메시지 (있을 때만)
            state.error?.let {
                Text(
                    text = it,
                    color = DevBlack,
                    fontSize = META_SIZE,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }

            // 카카오 로그인 버튼
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SOCIAL_HEIGHT)
                    .clip(RoundedCornerShape(BUTTON_RADIUS))
                    .background(KakaoYellow)
                    .clickable(enabled = !state.isLoading, onClick = onClickKakao),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(KakaoBlack, CircleShape)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "카카오 로그인", 
                        color = KakaoBlack, 
                        fontFamily = KakaoBigSans, 
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }
            }
            
            Spacer(Modifier.height(24.dp))
            
            // 안내 문구
            Text(
                text = "카카오 계정으로 간편하게 시작하세요",
                color = DevDarkgray,
                fontFamily = KakaoBigSans,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp
            )
        }
    }
}

/* ===================== PREVIEW ===================== */
@Preview(name = "Login 360x800", widthDp = 360, heightDp = 800, showBackground = true)
@Preview(name = "Pixel 7a", device = Devices.PIXEL_7A, showBackground = true)
@Composable
private fun PreviewLogin() {
    DevMartTheme {
        LoginScreen()
    }
}
