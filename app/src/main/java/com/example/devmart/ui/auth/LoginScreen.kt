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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devmart.ui.theme.BUTTON_HEIGHT
import com.example.devmart.ui.theme.BUTTON_RADIUS
import com.example.devmart.ui.theme.BUTTON_TOP
import com.example.devmart.ui.theme.DevBlack
import com.example.devmart.ui.theme.DevDarkgray
import com.example.devmart.ui.theme.DevDarkneyvy
import com.example.devmart.ui.theme.DevFonts.KakaoBigSans
import com.example.devmart.ui.theme.DevGray
import com.example.devmart.ui.theme.DevMartTheme
import com.example.devmart.ui.theme.DevNeyvy
import com.example.devmart.ui.theme.DevWhite
import com.example.devmart.ui.theme.FIELD_HEIGHT
import com.example.devmart.ui.theme.FIELD_SPACING
import com.example.devmart.ui.theme.FOOTER_SIZE
import com.example.devmart.ui.theme.KakaoBlack
import com.example.devmart.ui.theme.KakaoYellow
import com.example.devmart.ui.theme.LOGO_H
import com.example.devmart.ui.theme.LOGO_TOP
import com.example.devmart.ui.theme.LOGO_W
import com.example.devmart.ui.theme.META_SIZE
import com.example.devmart.ui.theme.PADDING_H
import com.example.devmart.ui.theme.SOCIAL_HEIGHT
import com.example.devmart.ui.theme.SOCIAL_TOP
import com.example.devmart.ui.theme.TITLE_TOP


// --- UI State ---
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

// --- Screen ---
@Composable
fun LoginScreen(
    state: LoginUiState = LoginUiState(),
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onClickLogin: () -> Unit = {},
    onClickKakao: () -> Unit = {},
    onFindId: () -> Unit = {},
    onFindPw: () -> Unit = {},
    onSignUp: () -> Unit = {}
) {
    Surface(modifier = Modifier.fillMaxSize(), color = DevWhite) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = PADDING_H),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(LOGO_TOP))

            // 로고 영역(임시)
            Box(
                modifier = Modifier
                    .size(LOGO_W, LOGO_H)
                    .clip(RoundedCornerShape(10.dp))
                    .background(DevWhite),
                contentAlignment = Alignment.Center
            ) {
                Text("Dev Mart", color = DevDarkneyvy, fontFamily = KakaoBigSans, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }

            Spacer(Modifier.height(TITLE_TOP))
            Spacer(Modifier.height(20.dp))

            // ===== 입력칸: 밑줄 + 내부 placeholder =====
            UnderlineField(
                value = state.email,
                onValueChange = onEmailChange,
                placeholder = "이메일주소",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(Modifier.height(FIELD_SPACING))
            UnderlineField(
                value = state.password,
                onValueChange = onPasswordChange,
                placeholder = "비밀번호",
                isPassword = true
            )

            // 에러 메시지 (있을 때만)
            state.error?.let {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = it,
                    color = DevBlack,
                    fontSize = META_SIZE,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(BUTTON_TOP))

            // 로그인 버튼
            Button(
                onClick = onClickLogin,
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(BUTTON_HEIGHT),
                shape = RoundedCornerShape(BUTTON_RADIUS),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DevGray,
                    contentColor = DevNeyvy,
                    disabledContainerColor = DevGray.copy(alpha = 0.4f),
                    disabledContentColor = DevNeyvy.copy(alpha = 0.8f)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) { Text("로그인", fontFamily = KakaoBigSans, fontWeight = FontWeight.Normal) }

            // 로그인 아래 중앙 분기: 아이디 찾기 | 비밀번호 찾기
            Spacer(Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                MetaLink("아이디 찾기", onFindId)
                VerticalSep()
                MetaLink("비밀번호 찾기", onFindPw)
            }

            Spacer(Modifier.height(25.dp))



            // 카카오 로그인 버튼
            Spacer(Modifier.height(SOCIAL_TOP))
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
                    Text("Kakao 로그인", color = KakaoBlack, fontFamily = KakaoBigSans, fontWeight = FontWeight.Normal)
                }
            }

            // 하단 고정 영역을 위해 여유 공간 밀어내기
            Spacer(Modifier.weight(1f))

            // 하단 안내 + 회원가입
            Text(
                text = "아직 Dev Mart 회원이 아니신가요?",
                color = DevGray.copy(alpha = 0.7f),
                fontSize = FOOTER_SIZE,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )
            Text(
                text = "회원가입하기",
                color = DevDarkneyvy,
                fontSize = FOOTER_SIZE,
                fontFamily = KakaoBigSans,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .clickable(onClick = onSignUp)
                    .padding(bottom = 16.dp)
            )
        }
    }
}

// ===== 밑줄 스타일 입력칸(placeholder 내부 표시) =====
@Composable
private fun UnderlineField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onImeAction: (() -> Unit)? = null
) {
    val visual = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        placeholder = { Text(placeholder) },
        visualTransformation = visual,
        keyboardOptions = keyboardOptions.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = { onImeAction?.invoke()}
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(FIELD_HEIGHT),
        shape = RoundedCornerShape(0.dp),
        colors = TextFieldDefaults.colors(
            // 배경 투명 + 밑줄만 사용
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = DevGray, // 연한 회색 밑줄
            errorIndicatorColor = MaterialTheme.colorScheme.error,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedPlaceholderColor = DevDarkgray,
            unfocusedPlaceholderColor = DevDarkgray
        )
    )
}

// ===== 작은 링크/구분선 =====
@Composable
private fun MetaLink(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        color = DevGray,
        fontFamily = KakaoBigSans,
        fontWeight = FontWeight.Normal,
        fontSize = META_SIZE,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp)
    )
}

@Composable
private fun VerticalSep() {
    Box(
        modifier = Modifier
            .height(16.dp)
            .width(1.dp)
            .background(DevDarkgray.copy(alpha = 0.6f))
    )
}

/* ===================== PREVIEW ===================== */
@Preview(name = "Login 360x800", widthDp = 360, heightDp = 800, showBackground = true)
@Preview(name = "Pixel 7a", device = Devices.PIXEL_7A, showBackground = true)
@Composable
private fun PreviewLogin() {
    DevMartTheme {
        LoginScreen(state = LoginUiState(email = "", password = ""))
    }
}
