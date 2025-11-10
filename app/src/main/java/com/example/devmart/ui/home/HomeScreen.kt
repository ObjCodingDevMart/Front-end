package com.example.devmart.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(openDetail: (String) -> Unit = {},   // ← 기본값 추가
               detailId: String? = null) {
    Scaffold(topBar = { TopAppBar(title = { Text("Home") }) }) { pad ->
        Column(Modifier.padding(pad).padding(16.dp)) {
            Text("로그인 성공! 여기에 목록/상품 등을 붙이면 됨.")
        }
    }
}
