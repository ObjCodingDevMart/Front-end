package com.example.devmart.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.devmart.R
import com.example.devmart.domain.model.Product
import com.example.devmart.domain.model.Review
import com.example.devmart.ui.component.BackButton
import com.example.devmart.ui.theme.DevBlack
import com.example.devmart.ui.theme.DevDarkgray
import com.example.devmart.ui.theme.DevGray
import com.example.devmart.ui.theme.DevMartTheme
import com.example.devmart.ui.theme.DevNeyvy
import com.example.devmart.ui.theme.DevWhite
import com.example.devmart.ui.theme.PADDING_H
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product? = null,
    onBackClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onLikeClick: () -> Unit = {},
    onAddToCart: () -> Unit = {},
    onBuyNow: () -> Unit = {}
) {
    // 임시 데이터 (실제로는 파라미터로 받아야 함)
    val displayProduct = product ?: Product(
        id = "1",
        brand = "fkqlt",
        title = "fkqlt iphone 케이스",
        price = 16000,
        imageUrl = null
    )
    
    var isLiked by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
    
    val tabs = listOf("상품 상세", "구매 안내", "리뷰")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        BackButton(onClick = onBackClick)
                        Text(
                            text = "Dev Mart",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = DevBlack
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "검색",
                            tint = DevBlack
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DevWhite
                )
            )
        },
        bottomBar = {
            BottomActionBar(
                isLiked = isLiked,
                onLikeClick = {
                    isLiked = !isLiked
                    onLikeClick()
                },
                onAddToCart = onAddToCart,
                onBuyNow = onBuyNow
            )
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .background(DevWhite)
        ) {
            // 상품 이미지 영역부터 스크롤 가능하게 전체를 감싸기
            ProductImageSection(
                imageUrl = displayProduct.imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            )

            ProductInfoSection(
                product = displayProduct,
                modifier = Modifier.padding(horizontal = PADDING_H, vertical = 20.dp)
            )

            ShippingInfoSection(
                modifier = Modifier.padding(horizontal = PADDING_H, vertical = 16.dp)
            )

            TabSection(
                tabs = tabs,
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                modifier = Modifier.fillMaxWidth()
            )

            TabContent(
                selectedTab = selectedTab,
                productId = displayProduct.id,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = PADDING_H, vertical = 16.dp)
            )
        }
    }
}

@Composable
fun ProductImageSection(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(DevGray),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl != null && imageUrl.isNotBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "상품 이미지",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            // 플레이스홀더
            Text(
                text = "상품 이미지",
                color = DevDarkgray,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ProductInfoSection(
    product: Product,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // 제목
        Text(
            text = product.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = DevBlack,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 부제목 (브랜드 정보)
        Text(
            text = product.brand,
            fontSize = 16.sp,
            color = DevDarkgray,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 가격
        Text(
            text = "${product.price}원",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = DevNeyvy,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ShippingInfoSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DevWhite)
    ) {
        // 배송 정보
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "배송 정보",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = DevBlack
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "기본 배송비 3000원",
                fontSize = 13.sp,
                color = DevDarkgray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(12.dp)
                    .background(DevDarkgray.copy(alpha = 0.3f))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "대한통운",
                fontSize = 13.sp,
                color = DevDarkgray
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // 배송 출발일
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "배송출발일",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = DevBlack
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "지금 주문하면 11/12 - 11/19 사이에 출발해요",
                fontSize = 13.sp,
                color = DevDarkgray
            )
        }
    }
}

@Composable
fun TabSection(
    tabs: List<String>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    var rowWidth by remember { mutableFloatStateOf(0f) }
    
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    rowWidth = with(density) { coordinates.size.width.toFloat() }
                },
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            tabs.forEachIndexed { index, tab ->
                TabItem(
                    text = tab,
                    isSelected = index == selectedTab,
                    onClick = { 
                        onTabSelected(index)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        // 선택된 탭 아래 선
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(DevGray)
        ) {
            if (rowWidth > 0f) {
                val tabWidth = rowWidth / tabs.size
                Box(
                    modifier = Modifier
                        .width(with(density) { tabWidth.toDp() })
                        .offset(x = with(density) { (selectedTab * tabWidth).toDp() })
                        .height(2.dp)
                        .background(DevNeyvy)
                )
            }
        }
    }
}

@Composable
fun TabItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    @Suppress("UNUSED_VARIABLE")
    val isPressed by interactionSource.collectIsPressedAsState()
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = null
            )
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) DevNeyvy else DevDarkgray,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
fun TabContent(
    selectedTab: Int,
    productId: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(DevWhite)
    ) {
        when (selectedTab) {
            0 -> {
                // 상품 상세
                ProductDetailContent()
            }
            1 -> {
                // 구매 안내
                PurchaseGuideContent()
            }
            2 -> {
                // 리뷰
                ReviewSection(productId = productId)
            }
        }
    }
}

@Composable
fun ProductDetailContent(
    modifier: Modifier = Modifier
) {
    @Suppress("UNUSED_VARIABLE")
    val scrollState = rememberScrollState()
    
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        // 상품 상세 이미지 영역 (img.png)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(DevGray.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            // drawable에서 이미지 로드
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = "상품 상세 이미지",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentScale = ContentScale.Fit
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 상품 정보 텍스트
        ProductDetailInfo()
    }
}

@Composable
fun ProductDetailInfo(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "상품 상세 정보",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DevBlack,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        Text(
            text = """
                • 제품명: fkqlt iphone 케이스
                • 브랜드: fkqlt
                • 재질: 실리콘 소재로 제작된 고품질 케이스
                • 색상: 다양한 색상 옵션 제공
                • 호환 모델: iPhone 14, iPhone 14 Pro, iPhone 15, iPhone 15 Pro
                
                [제품 특징]
                - 충격 흡수력이 뛰어난 실리콘 소재
                - 미끄럼 방지 처리로 안전한 사용
                - 카메라 렌즈 보호 기능
                - 얇고 가벼운 디자인으로 휴대성 우수
                
                [사용 방법]
                1. 스마트폰 뒷면을 깨끗이 닦아주세요
                2. 케이스를 스마트폰에 맞게 끼워주세요
                3. 가장자리가 잘 맞는지 확인해주세요
                
                [주의사항]
                - 직사광선을 피해 보관해주세요
                - 화학 용품과의 접촉을 피해주세요
                - 정기적으로 청소해주세요
            """.trimIndent(),
            fontSize = 14.sp,
            color = DevBlack,
            lineHeight = 22.sp
        )
    }
}

@Composable
fun PurchaseGuideContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "구매 안내",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DevBlack,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        GuideSection(
            title = "배송 안내",
            content = """
                • 배송비: 기본 배송비 3,000원 (50,000원 이상 구매 시 무료배송)
                • 배송업체: 대한통운
                • 배송 소요시간: 평일 기준 2-3일 소요
                • 제주 및 도서산간 지역은 추가 배송비가 발생할 수 있습니다.
            """.trimIndent()
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        GuideSection(
            title = "교환/반품 안내",
            content = """
                • 교환/반품 기간: 상품 수령 후 7일 이내
                • 교환/반품 불가 사유:
                  - 고객의 사용 또는 일부 소비로 상품의 가치가 감소한 경우
                  - 시간 경과로 재판매가 어려운 경우
                  - 상품의 포장을 훼손하여 상품의 가치가 감소한 경우
                • 교환/반품 배송비: 고객 부담 (단순 변심의 경우)
            """.trimIndent()
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        GuideSection(
            title = "결제 안내",
            content = """
                • 결제 방법: 신용카드, 무통장 입금, 카카오페이, 네이버페이
                • 무통장 입금의 경우 입금 확인 후 배송이 시작됩니다.
                • 결제 완료 후 주문 취소는 마이페이지에서 가능합니다.
            """.trimIndent()
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        GuideSection(
            title = "문의 안내",
            content = """
                • 고객센터: 1588-0000 (평일 09:00 ~ 18:00)
                • 이메일: support@devmart.shop
                • 카카오톡: @devmart 고객센터
            """.trimIndent()
        )
    }
}

@Composable
fun GuideSection(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = DevNeyvy,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = content,
            fontSize = 14.sp,
            color = DevBlack,
            lineHeight = 22.sp
        )
    }
}

// ===================== 리뷰 섹션 =====================

@Composable
fun ReviewSection(
    @Suppress("UNUSED_PARAMETER") productId: String,
    modifier: Modifier = Modifier
) {
    // 더미 리뷰 데이터
    val reviews = remember {
        listOf(
            Review(
                id = "1",
                userId = "user1",
                userName = "김**",
                rating = 5,
                content = "정말 만족스러운 제품이에요! 품질도 좋고 배송도 빠르네요. 다음에도 또 구매할게요.",
                date = "2024.11.05",
                images = listOf()
            ),
            Review(
                id = "2",
                userId = "user2",
                userName = "이**",
                rating = 4,
                content = "가격 대비 괜찮은 것 같아요. 다만 색상이 사진보다 조금 다르게 보이긴 해요.",
                date = "2024.11.03",
                images = listOf()
            ),
            Review(
                id = "3",
                userId = "user3",
                userName = "박**",
                rating = 5,
                content = "완벽해요! 디자인도 예쁘고 실용적이에요. 추천합니다!",
                date = "2024.11.01",
                images = listOf()
            ),
            Review(
                id = "4",
                userId = "user4",
                userName = "최**",
                rating = 3,
                content = "무난한 제품이에요. 특별히 좋거나 나쁘지는 않네요.",
                date = "2024.10.28",
                images = listOf()
            ),
            Review(
                id = "5",
                userId = "user5",
                userName = "정**",
                rating = 5,
                content = "친구 추천으로 샀는데 정말 좋아요! 가성비 최고예요. 배송도 빠르고 포장도 깔끔했어요.",
                date = "2024.10.25",
                images = listOf()
            ),
            Review(
                id = "6",
                userId = "user6",
                userName = "강**",
                rating = 4,
                content = "전반적으로 만족스러워요. 다만 사이즈가 생각보다 작을 수 있으니 참고하세요.",
                date = "2024.10.20",
                images = listOf()
            )
        )
    }
    
    // 리뷰 통계
    val averageRating = reviews.map { it.rating }.average()
    val totalReviews = reviews.size
    
    Column(modifier = modifier.fillMaxWidth()) {
        // 리뷰 헤더 (평점 및 통계)
        ReviewHeader(
            averageRating = averageRating,
            totalReviews = totalReviews,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 리뷰 목록
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            reviews.forEach { review ->
                ReviewItem(
                    review = review,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ReviewHeader(
    averageRating: Double,
    totalReviews: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DevGray.copy(alpha = 0.1f))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 평균 별점
            Text(
                text = String.format(Locale.US, "%.2f", averageRating),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = DevNeyvy
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // 별점 표시
            StarRating(
                rating = averageRating.toInt(),
                modifier = Modifier.padding(top = 4.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // 리뷰 개수
            Text(
                text = "($totalReviews)",
                fontSize = 14.sp,
                color = DevDarkgray
            )
        }
    }
}

@Composable
fun ReviewItem(
    review: Review,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = DevWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 리뷰 헤더 (작성자, 별점, 날짜)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = review.userName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DevBlack
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    StarRating(
                        rating = review.rating,
                        modifier = Modifier
                    )
                }
                
                Text(
                    text = review.date,
                    fontSize = 12.sp,
                    color = DevDarkgray
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 리뷰 내용
            Text(
                text = review.content,
                fontSize = 14.sp,
                color = DevBlack,
                lineHeight = 20.sp
            )
            
            // 리뷰 이미지가 있는 경우
            review.images?.takeIf { it.isNotEmpty() }?.let { images ->
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(images) { imageUrl ->
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "리뷰 이미지",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StarRating(
    rating: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        repeat(5) { index ->
            Icon(
                imageVector = if (index < rating) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = null,
                tint = if (index < rating) Color(0xFFFFD700) else DevGray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun BottomActionBar(
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    onAddToCart: () -> Unit,
    onBuyNow: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = DevWhite,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 좋아요 버튼
            Column(
                modifier = Modifier
                    .clickable(onClick = onLikeClick),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "좋아요",
                    tint = DevNeyvy,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "좋아요",
                    fontSize = 11.sp,
                    color = DevNeyvy
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // 장바구니 버튼
            OutlinedButton(
                onClick = onAddToCart,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = DevNeyvy
                ),
                border = ButtonDefaults.outlinedButtonBorder(enabled = true)
            ) {
                Text(
                    text = "장바구니",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // 바로 구매하기 버튼
            Button(
                onClick = onBuyNow,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DevNeyvy,
                    contentColor = DevWhite
                )
            ) {
                Text(
                    text = "바로 구매하기",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/* ===================== PREVIEW ===================== */
@Preview(name = "Product Detail 360x800", widthDp = 360, heightDp = 800, showBackground = true)
@Preview(name = "Pixel 7a", device = Devices.PIXEL_7A, showBackground = true)
@Composable
private fun PreviewProductDetail() {
    DevMartTheme {
        ProductDetailScreen()
    }
}
