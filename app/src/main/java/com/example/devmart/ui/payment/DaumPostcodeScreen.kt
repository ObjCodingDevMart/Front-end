package com.example.devmart.ui.payment

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.devmart.ui.component.BackButton
import com.example.devmart.ui.theme.DevBlack
import com.example.devmart.ui.theme.DevFonts
import com.example.devmart.ui.theme.DevWhite

/**
 * Daum 우편번호 서비스를 WebView로 표시하는 화면
 * 
 * @param onAddressSelected 주소 선택 시 콜백 (우편번호, 도로명주소, 지번주소, 건물명)
 * @param onBack 뒤로가기 콜백
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DaumPostcodeScreen(
    onAddressSelected: (postalCode: String, roadAddress: String, jibunAddress: String, buildingName: String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DevWhite)
            .statusBarsPadding() // 시스템 상태바 패딩
            .padding(16.dp)
    ) {
        // 상단 헤더
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(onClick = onBack)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "우편번호 검색",
                fontSize = 20.sp,
                fontFamily = DevFonts.KakaoBigSans,
                fontWeight = FontWeight.Bold,
                color = DevBlack
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // WebView로 Daum 우편번호 서비스 표시
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            factory = { context ->
                WebView(context).apply {
                    @Suppress("DEPRECATION")
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        setSupportZoom(false)
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        
                        // file:// 에서 https:// 스크립트 로드 허용
                        allowUniversalAccessFromFileURLs = true
                        allowFileAccessFromFileURLs = true
                        allowFileAccess = true
                        allowContentAccess = true
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    }
                    
                    // JavaScript Interface 추가
                    addJavascriptInterface(
                        DaumPostcodeInterface(
                            onAddressSelectedCallback = { postalCode, roadAddr, jibunAddr, buildingName ->
                                onAddressSelected(postalCode, roadAddr, jibunAddr, buildingName)
                            },
                            onCloseCallback = onBack
                        ),
                        "Android"
                    )
                    
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            Log.d("DaumPostcode", "Page loaded: $url")
                        }
                    }
                    
                    webChromeClient = object : WebChromeClient() {
                        override fun onConsoleMessage(message: android.webkit.ConsoleMessage?): Boolean {
                            Log.d("DaumPostcode", "Console: ${message?.message()}")
                            return super.onConsoleMessage(message)
                        }
                    }
                    
                    // assets 폴더의 HTML 로드
                    loadUrl("file:///android_asset/postcode.html")
                }
            }
        )
    }
}

/**
 * WebView와 통신하기 위한 JavaScript Interface
 * JavaScript에서 Android.onAddressSelected(), Android.onClose()로 호출됨
 */
class DaumPostcodeInterface(
    private val onAddressSelectedCallback: (postalCode: String, roadAddress: String, jibunAddress: String, buildingName: String) -> Unit,
    private val onCloseCallback: () -> Unit
) {
    private val mainHandler = android.os.Handler(android.os.Looper.getMainLooper())
    
    @Suppress("unused") // JavaScript에서 Android.onAddressSelected()로 호출됨
    @JavascriptInterface
    fun onAddressSelected(postalCode: String, roadAddress: String, jibunAddress: String, buildingName: String) {
        Log.d("DaumPostcode", "Interface called: $postalCode, $roadAddress")
        // 메인 스레드에서 콜백 실행
        mainHandler.post {
            onAddressSelectedCallback.invoke(postalCode, roadAddress, jibunAddress, buildingName)
        }
    }
    
    @Suppress("unused") // JavaScript에서 Android.onClose()로 호출됨
    @JavascriptInterface
    fun onClose() {
        mainHandler.post {
            onCloseCallback.invoke()
        }
    }
}

