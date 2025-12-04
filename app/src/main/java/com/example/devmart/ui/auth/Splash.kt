package com.example.devmart.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.devmart.ui.theme.DevDarkneyvy
import com.example.devmart.ui.theme.DevFonts
import com.example.devmart.ui.theme.DevMartTheme
import com.example.devmart.ui.theme.DevViolte
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onTimeout: () -> Unit = {}
) {
    // 2초 후 다음 화면으로 이동
    LaunchedEffect(Unit) {
        delay(2000L)
        onTimeout()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DevViolte),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 위쪽 2/5 공간
        Spacer(modifier = Modifier.weight(2f))
        
        // 앱 이름 (전체의 3/5 위치 = 위에서 2/5 지점)
        Text(
            text = "Dev Mart",
            fontSize = 44.sp,
            fontFamily = DevFonts.KakaoBigSans,
            fontWeight = FontWeight.ExtraBold,
            color = DevDarkneyvy,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        // 아래쪽 3/5 공간
        Spacer(modifier = Modifier.weight(3f))
    }
}

// ------------------ Preview ------------------

@Preview(showBackground = true, heightDp = 800)
@Composable
fun PreviewSplashScreen() {
    DevMartTheme {
        SplashScreen()
    }
}
