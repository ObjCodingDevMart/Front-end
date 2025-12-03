package com.example.devmart.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devmart.ui.payment.OrderProduct
import java.util.Locale
import com.example.devmart.ui.theme.DevDarkgray
import com.example.devmart.ui.theme.DevDarkneyvy
import com.example.devmart.ui.theme.DevFonts.KakaoBigSans
import com.example.devmart.ui.theme.DevGray

enum class OrderItemMode {
    Editable,   // 장바구니 페이지
    ReadOnly    // 결제/주문확인 페이지
}

@Composable
fun OrderItemCard(
    product: OrderProduct,
    mode: OrderItemMode,
    onClickRemove: (() -> Unit)? = null,
    onIncrement: (() -> Unit)? = null,
    onDecrement: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        // ---------- 상품 이미지 ----------
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(DevGray)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // ---------- 상품 텍스트 정보 ----------
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.name, 
                style = MaterialTheme.typography.titleMedium,
                fontFamily = KakaoBigSans,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = product.detail,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = KakaoBigSans,
                color = DevDarkgray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${String.format(Locale.KOREA, "%,d", product.price * product.qty)}원", 
                fontSize = 16.sp,
                fontFamily = KakaoBigSans, 
                fontWeight = FontWeight.Bold,
                color = DevDarkneyvy
            )
            
            // Editable 모드일 때 수량 조절 UI
            if (mode == OrderItemMode.Editable) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // - 버튼
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .border(1.dp, DevDarkgray, CircleShape)
                            .clickable { onDecrement?.invoke() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "−",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DevDarkgray
                        )
                    }
                    
                    // 수량
                    Text(
                        text = "${product.qty}",
                        fontSize = 16.sp,
                        fontFamily = KakaoBigSans,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    
                    // + 버튼
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(DevDarkneyvy)
                            .clickable { onIncrement?.invoke() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = androidx.compose.ui.graphics.Color.White
                        )
                    }
                }
            } else {
                // ReadOnly 모드
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "수량: ${product.qty}",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = KakaoBigSans,
                    color = DevDarkgray
                )
            }
        }

        // ---------- 삭제 버튼 (Editable 모드만) ----------
        if (mode == OrderItemMode.Editable) {
            IconButton(
                onClick = { onClickRemove?.invoke() },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "삭제",
                    tint = DevDarkgray
                )
            }
        }
    }
}
