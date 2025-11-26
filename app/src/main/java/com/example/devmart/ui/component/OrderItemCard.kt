package com.example.devmart.ui.component

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
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
import com.example.devmart.ui.payment.OrderProduct
import com.example.devmart.ui.theme.DevBlack
import com.example.devmart.ui.theme.DevDarkgray
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
    onClickChangeQuantity: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ---------- 상품 이미지 ----------
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(DevGray)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // ---------- 상품 텍스트 정보 ----------
        Column(modifier = Modifier.weight(1f)) {
            Text(text = product.name, style = MaterialTheme.typography.titleMedium)
            Text(
                text = product.detail,
                style = MaterialTheme.typography.bodySmall,
                color = DevDarkgray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "${product.price}원", style = MaterialTheme.typography.bodyMedium, fontFamily = KakaoBigSans, fontWeight = FontWeight.Normal)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "수량: ${product.qty}", style = MaterialTheme.typography.bodySmall, fontFamily = KakaoBigSans, fontWeight = FontWeight.Normal)
        }

        Spacer(modifier = Modifier.width(8.dp))

        // ---------- 오른쪽 영역(모드에 따라 변경) ----------
        when (mode) {
            OrderItemMode.Editable -> {
                // 삭제 버튼 + 수량 변경
                Column(horizontalAlignment = Alignment.End) {
                    IconButton(onClick = { onClickRemove?.invoke() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "삭제"
                        )
                    }

                    Button(
                        onClick = { onClickChangeQuantity?.invoke() }
                    ) {
                        Text(
                            text = "수량변경",
                            fontFamily = KakaoBigSans,
                            fontWeight = FontWeight.Normal,
                            color = DevBlack
                        )
                    }
                }
            }

            OrderItemMode.ReadOnly -> {
                // 결제/주문 페이지에서는 버튼 없음
            }
        }
    }
}
