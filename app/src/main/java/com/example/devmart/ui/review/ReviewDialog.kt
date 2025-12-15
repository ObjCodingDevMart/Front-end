package com.example.devmart.ui.review

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.devmart.ui.theme.DevBlack
import com.example.devmart.ui.theme.DevDarkneyvy
import com.example.devmart.ui.theme.DevFonts
import com.example.devmart.ui.theme.DevGray
import com.example.devmart.ui.theme.DevMartTheme
import com.example.devmart.ui.theme.DevNeyvy
import com.example.devmart.ui.theme.DevWhite

@Composable
fun ReviewDialog(
    targetItem: ReviewTargetItem,
    reviewState: ReviewState = ReviewState.Idle,
    onSubmit: (rating: Int, content: String) -> Unit,
    onDismiss: () -> Unit,
    onComplete: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = {
            if (reviewState !is ReviewState.Loading) onDismiss()
        },
        properties = DialogProperties(
            dismissOnBackPress = reviewState !is ReviewState.Loading,
            dismissOnClickOutside = reviewState !is ReviewState.Loading
        )
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DevWhite
        ) {
            when (reviewState) {
                is ReviewState.Loading -> ReviewLoadingContent()
                is ReviewState.Success -> ReviewSuccessContent(
                    message = reviewState.message,
                    onConfirm = onComplete
                )
                is ReviewState.Error -> ReviewErrorContent(
                    message = reviewState.message,
                    onDismiss = onDismiss
                )
                is ReviewState.Idle -> ReviewInputContent(
                    targetItem = targetItem,
                    onSubmit = onSubmit,
                    onDismiss = onDismiss
                )
            }
        }
    }
}

@Composable
private fun ReviewInputContent(
    targetItem: ReviewTargetItem,
    onSubmit: (rating: Int, content: String) -> Unit,
    onDismiss: () -> Unit
) {
    var rating by remember { mutableIntStateOf(5) }
    var content by remember { mutableStateOf("") }
    var contentError by remember { mutableStateOf<String?>(null) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        // 헤더
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "리뷰 작성",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = DevFonts.KakaoBigSans,
                color = DevBlack
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "닫기",
                    tint = DevBlack
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 상품 정보
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 상품 이미지
            AsyncImage(
                model = targetItem.imagePath,
                contentDescription = targetItem.itemName,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(DevGray),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = targetItem.brand,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = DevFonts.KakaoBigSans
                )
                Text(
                    text = targetItem.itemName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = DevBlack,
                    fontFamily = DevFonts.KakaoBigSans,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${targetItem.price.formatPrice()}원",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DevBlack,
                    fontFamily = DevFonts.KakaoBigSans
                )
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(color = DevGray)
        Spacer(modifier = Modifier.height(20.dp))
        
        // 별점 입력
        Text(
            text = "상품은 어떠셨나요?",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = DevBlack,
            fontFamily = DevFonts.KakaoBigSans
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // 별점 선택
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(5) { index ->
                val starNumber = index + 1
                Icon(
                    imageVector = if (starNumber <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "별 $starNumber",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { rating = starNumber },
                    tint = if (starNumber <= rating) Color(0xFFFFD700) else DevGray
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 별점 텍스트
        Text(
            text = when (rating) {
                1 -> "별로예요"
                2 -> "그저 그래요"
                3 -> "보통이에요"
                4 -> "좋아요"
                5 -> "최고예요!"
                else -> ""
            },
            fontSize = 13.sp,
            color = Color(0xFFFFD700),
            fontFamily = DevFonts.KakaoBigSans,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(color = DevGray)
        Spacer(modifier = Modifier.height(20.dp))
        
        // 리뷰 내용 입력
        Text(
            text = "리뷰를 작성해주세요",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = DevBlack,
            fontFamily = DevFonts.KakaoBigSans
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = content,
            onValueChange = { 
                content = it
                contentError = null
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = { 
                Text(
                    "상품에 대한 솔직한 리뷰를 남겨주세요.",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            },
            isError = contentError != null
        )
        
        if (contentError != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = contentError!!,
                fontSize = 12.sp,
                color = Color(0xFFF44336)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // 버튼들
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("취소")
            }
            Button(
                onClick = {
                    if (content.isBlank()) {
                        contentError = "리뷰 내용을 입력해주세요."
                        return@Button
                    }
                    onSubmit(rating, content)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DevDarkneyvy,
                    contentColor = DevWhite
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("등록하기")
            }
        }
    }
}

@Composable
private fun ReviewLoadingContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = DevNeyvy
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "리뷰 등록 중...",
            fontSize = 16.sp,
            color = DevBlack
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "잠시만 기다려주세요",
            fontSize = 13.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun ReviewSuccessContent(
    message: String,
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(Color(0xFF4CAF50).copy(alpha = 0.1f), RoundedCornerShape(36.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "성공",
                modifier = Modifier.size(48.dp),
                tint = Color(0xFF4CAF50)
            )
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text(
            text = "리뷰 등록 완료!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = DevBlack
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = message,
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = DevDarkneyvy,
                contentColor = DevWhite
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("확인")
        }
    }
}

@Composable
private fun ReviewErrorContent(
    message: String,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(Color(0xFFF44336).copy(alpha = 0.1f), RoundedCornerShape(36.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "실패",
                modifier = Modifier.size(48.dp),
                tint = Color(0xFFF44336)
            )
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text(
            text = "리뷰 등록 실패",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = DevBlack
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = message,
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = DevDarkneyvy,
                contentColor = DevWhite
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("닫기")
        }
    }
}

// 숫자 포맷팅 (천 단위 콤마)
private fun Int.formatPrice(): String {
    return String.format(java.util.Locale.KOREA, "%,d", this)
}

// Preview
@Preview(showBackground = true)
@Composable
private fun PreviewReviewDialog() {
    DevMartTheme {
        ReviewDialog(
            targetItem = ReviewTargetItem(
                itemId = 1,
                itemName = "집중력 개선 후드집업",
                brand = "특양면 베이직 후드",
                price = 39900,
                imagePath = null
            ),
            onSubmit = { _, _ -> },
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewReviewSuccessDialog() {
    DevMartTheme {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DevWhite
        ) {
            ReviewSuccessContent(
                message = "리뷰가 정상적으로 등록되었습니다.",
                onConfirm = {}
            )
        }
    }
}

