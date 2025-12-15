package com.example.devmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 카카오 SDK 초기화 (BuildConfig에서 읽기)
        KakaoSdk.init(this, BuildConfig.NATIVE_APP_KEY)
        
        setContent { AppNav() }
    }
}
