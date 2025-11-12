package com.example.devmart.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

// 기존 템플릿의 Purple80/Purple40, Typography, Shapes 등을 없애고
// 최소 테마만 남긴 안전한 버전입니다.
// 우리 화면들에서는 각 컴포넌트에 색/간격을 직접 지정하고 있어서 이걸로 충분해요.
@Composable
fun DevMartTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content
    )
}
