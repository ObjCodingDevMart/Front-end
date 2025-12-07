package com.example.devmart.ui.payment

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devmart.ui.component.BackButton
import com.example.devmart.ui.theme.DevBlack
import com.example.devmart.ui.theme.DevDarkgray
import com.example.devmart.ui.theme.DevFonts
import com.example.devmart.ui.theme.DevGray
import com.example.devmart.ui.theme.DevMartTheme
import com.example.devmart.ui.theme.DevNeyvy
import com.example.devmart.ui.theme.DevWhite

@Composable
fun AddressSearchScreen(
    keyword: String = "",
    results: List<Address> = emptyList(),
    onKeywordChange: (String) -> Unit = {},
    onSearch: () -> Unit = {},
    onSelect: (Address) -> Unit = {},
    onBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DevWhite)
    ) {
        // ------------------ 상단 타이틀바 ------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(onClick = onBack)
            
            Text(
                text = "우편번호 검색",
                fontSize = 18.sp,
                fontFamily = DevFonts.KakaoBigSans,
                fontWeight = FontWeight.Bold,
                color = DevBlack,
                modifier = Modifier.weight(1f)
            )
        }

        // ------------------ 검색창 + 검색 버튼 ------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DevGray.copy(alpha = 0.3f))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = keyword,
                onValueChange = onKeywordChange,
                modifier = Modifier.weight(1f),
                placeholder = { 
                    Text(
                        "도로명, 건물명, 번지 검색",
                        fontFamily = DevFonts.KakaoBigSans,
                        fontWeight = FontWeight.Normal,
                        color = DevDarkgray
                    ) 
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = DevNeyvy,
                    unfocusedBorderColor = DevGray
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onSearch,
                modifier = Modifier
                    .height(56.dp)
                    .width(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DevNeyvy,
                    contentColor = DevWhite
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "검색",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        HorizontalDivider(color = DevGray.copy(alpha = 0.5f))

        // ------------------ 검색 결과 또는 검색 Tip ------------------
        if (results.isEmpty()) {
            // 검색 Tip 표시
            SearchTipSection()
        } else {
            // 검색 결과 리스트
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(results) { item ->
                    AddressResultItem(
                        address = item,
                        onClick = { onSelect(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchTipSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "우편번호 통합검색 Tip",
            fontSize = 16.sp,
            fontFamily = DevFonts.KakaoBigSans,
            fontWeight = FontWeight.Bold,
            color = DevBlack
        )

        Spacer(modifier = Modifier.height(12.dp))

        TipItem(
            title = "도로명 + 건물번호",
            example = "예: 송파대로 570"
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        TipItem(
            title = "동/읍/면/리 + 번지",
            example = "예: 신천동 7-30"
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        TipItem(
            title = "건물명, 아파트명",
            example = "예: 반포자이아파트"
        )
    }
}

@Composable
fun TipItem(
    title: String,
    example: String
) {
    Text(
        text = buildAnnotatedString {
            append("• ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold, color = DevBlack)) {
                append(title)
            }
            append(" ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, color = DevDarkgray)) {
                append("($example)")
            }
        },
        fontSize = 14.sp,
        fontFamily = DevFonts.KakaoBigSans
    )
}

@Composable
fun AddressResultItem(
    address: Address,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        // 우편번호
        Box(
            modifier = Modifier
                .background(DevNeyvy, RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = address.postalCode,
                color = DevWhite,
                fontSize = 12.sp,
                fontFamily = DevFonts.KakaoBigSans,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 도로명 주소
        Text(
            text = address.roadAddress,
            fontSize = 15.sp,
            fontFamily = DevFonts.KakaoBigSans,
            fontWeight = FontWeight.Bold,
            color = DevBlack
        )

        // 지번 주소
        if (address.jibunAddress.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = address.jibunAddress,
                fontSize = 13.sp,
                fontFamily = DevFonts.KakaoBigSans,
                fontWeight = FontWeight.Normal,
                color = DevDarkgray
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = DevGray.copy(alpha = 0.5f))
    }
}

// ------------------ Preview ------------------

@Preview(showBackground = true, heightDp = 800)
@Composable
fun PreviewAddressSearchScreenEmpty() {
    DevMartTheme {
        AddressSearchScreen(
            keyword = "",
            results = emptyList()
        )
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun PreviewAddressSearchScreenWithResults() {
    DevMartTheme {
        AddressSearchScreen(
            keyword = "강남",
            results = listOf(
                Address(
                    roadAddress = "서울특별시 강남구 테헤란로 123",
                    jibunAddress = "서울특별시 강남구 역삼동 123-45",
                    postalCode = "06134"
                ),
                Address(
                    roadAddress = "서울특별시 강남구 삼성로 456",
                    jibunAddress = "서울특별시 강남구 삼성동 67-89",
                    postalCode = "06178"
                ),
                Address(
                    roadAddress = "서울특별시 강남구 선릉로 789",
                    jibunAddress = "",
                    postalCode = "06192"
                )
            )
        )
    }
}

