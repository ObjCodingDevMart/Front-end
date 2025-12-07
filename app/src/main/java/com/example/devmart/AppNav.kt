package com.example.devmart

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.devmart.ui.auth.LoginScreen
import com.example.devmart.ui.auth.SplashScreen
import com.example.devmart.ui.auth.Top100SearchScreen
import com.example.devmart.ui.auth.Top100ViewModel
import com.example.devmart.ui.cart.CartScreen
import com.example.devmart.ui.cart.CartScreenState
import com.example.devmart.ui.cart.CartScreenActions
import com.example.devmart.ui.cart.CartPriceSummaryUiState
import com.example.devmart.ui.cart.CartOrderInfoUiState
import com.example.devmart.ui.component.ProductCard
import com.example.devmart.ui.home.HomeScreen
import com.example.devmart.ui.home.ProductDetailScreen
import com.example.devmart.ui.session.AuthState
import com.example.devmart.ui.session.SessionViewModel
import com.example.devmart.ui.payment.PaymentScreen
import com.example.devmart.ui.payment.PaymentViewModel
import com.example.devmart.ui.payment.DaumPostcodeScreen
import com.example.devmart.ui.payment.OrderProduct
import com.example.devmart.ui.order.OrderHistoryScreen
import com.example.devmart.ui.order.OrderHistoryUiState
import com.example.devmart.ui.order.OrderGroupUi
import com.example.devmart.ui.order.OrderSummaryUi
import com.example.devmart.ui.user.UserScreen
import com.example.devmart.ui.wishlist.WishlistScreen
import com.example.devmart.ui.wishlist.WishlistScreenState
import com.example.devmart.ui.wishlist.WishlistScreenActions
import com.example.devmart.ui.wishlist.WishlistItemUi

@Suppress("DEPRECATION")
@Composable
fun AppNav() {
    val nav = rememberNavController()
    @Suppress("DEPRECATION")
    val session: SessionViewModel = hiltViewModel()
    val auth by session.auth.collectAsState()

    NavHost(
        navController = nav,
        startDestination = Route.Splash.path
    ) {
        // 스플래시 화면
        composable(Route.Splash.path) {
            SplashScreen(
                onTimeout = {
                    // 2초 후 인증 상태에 따라 이동
                    when (auth) {
                        is AuthState.Authenticated ->
                            nav.navigate(Route.MainGraph.path) { popUpTo(0) { inclusive = true } }
                        else ->
                            nav.navigate(Route.AuthGraph.path) { popUpTo(0) { inclusive = true } }
                    }
                }
            )
        }

        // 인증 그래프
        navigation(startDestination = Route.Login.path, route = Route.AuthGraph.path) {
            composable(Route.Login.path) {
                // 로그인 상태 관찰 - 인증되면 MainGraph로 이동
                LaunchedEffect(auth) {
                    if (auth is AuthState.Authenticated) {
                        nav.navigate(Route.MainGraph.path) { 
                            popUpTo(Route.AuthGraph.path) { inclusive = true } 
                        }
                    }
                }
                
                LoginScreen(
                    onClickKakao = {
                        // TODO: 카카오 로그인 구현
                        // 테스트용: 바로 HomeScreen으로 이동
                        nav.navigate(Route.MainGraph.path) {
                            popUpTo(Route.AuthGraph.path) { inclusive = true }
                        }
                    }
                )
            }
        }
        navigation(startDestination = Route.Home.path, route = Route.MainGraph.path) {
            composable(Route.Home.path) { 
                HomeScreen(
                    openDetail = { id -> nav.navigate(Route.Detail.withId(id)) },
                    onNavigateToRoute = { route -> 
                        // 하단 네비게이션 바 라우팅 처리
                        when (route) {
                            "home" -> { /* 현재 화면 */ }
                            "top100" -> nav.navigate(Route.Top100.path) {
                                popUpTo(Route.MainGraph.path) { inclusive = false }
                                launchSingleTop = true
                            }
                            "order" -> nav.navigate(Route.Cart.path) {
                                popUpTo(Route.MainGraph.path) { inclusive = false }
                                launchSingleTop = true
                            }
                            "MyPage" -> nav.navigate(Route.MyPage.path) {
                                popUpTo(Route.MainGraph.path) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    }
                ) 
            }
            composable(Route.Detail.path) { backStackEntry ->
                // TODO: id로 상품 정보 가져오기 (API 연동 시 사용)
                val id = backStackEntry.arguments?.getString("id").orEmpty()
                Log.d("AppNav", "ProductDetail: $id")
                
                // 현재 표시 중인 상품 (나중에 API에서 가져오도록 수정)
                val currentProduct = com.example.devmart.domain.model.Product(
                    id = id,
                    brand = "fkqlt",
                    title = "fkqlt iphone 케이스",
                    price = 16000,
                    imageUrl = null
                )
                
                ProductDetailScreen(
                    product = currentProduct,
                    onBackClick = { nav.popBackStack() },
                    onSearchClick = { /* TODO: 검색 화면으로 이동 */ },
                    onLikeClick = { /* TODO: 좋아요 기능 구현 */ },
                    onAddToCart = { /* TODO: 장바구니 추가 기능 구현 */ },
                    onBuyNow = {
                        // 바로 구매: 현재 상품 정보를 Payment로 전달
                        nav.currentBackStackEntry?.savedStateHandle?.apply {
                            set("buyNowProductId", currentProduct.id)
                            set("buyNowProductName", currentProduct.title)
                            set("buyNowProductBrand", currentProduct.brand)
                            set("buyNowProductPrice", currentProduct.price)
                        }
                        nav.navigate(Route.Payment.path)
                    }
                )
            }
            composable(Route.Payment.path) { backStackEntry ->
                @Suppress("DEPRECATION")
                val paymentViewModel: PaymentViewModel = hiltViewModel()
                val addressState by paymentViewModel.address.collectAsState()
                
                LaunchedEffect(Unit) {
                    paymentViewModel.loadMyAddress()
                }
                
                // 주소 검색 화면에서 선택한 주소 받기
                val savedStateHandle = backStackEntry.savedStateHandle
                val selectedRoadAddress = savedStateHandle.get<String>("selectedRoadAddress")
                val selectedPostalCode = savedStateHandle.get<String>("selectedPostalCode")
                val selectedJibunAddress = savedStateHandle.get<String>("selectedJibunAddress")
                
                LaunchedEffect(selectedRoadAddress, selectedPostalCode) {
                    if (!selectedRoadAddress.isNullOrEmpty() && !selectedPostalCode.isNullOrEmpty()) {
                        paymentViewModel.setSelectedAddress(
                            com.example.devmart.ui.payment.Address(
                                roadAddress = selectedRoadAddress,
                                postalCode = selectedPostalCode,
                                jibunAddress = selectedJibunAddress ?: ""
                            )
                        )
                        // 사용 후 초기화
                        savedStateHandle.remove<String>("selectedRoadAddress")
                        savedStateHandle.remove<String>("selectedPostalCode")
                        savedStateHandle.remove<String>("selectedJibunAddress")
                    }
                }
                
                // 바로 구매 상품 확인 (ProductDetail에서 전달)
                val prevSavedStateHandle = nav.previousBackStackEntry?.savedStateHandle
                val buyNowProductId = prevSavedStateHandle?.get<String>("buyNowProductId")
                val buyNowProductName = prevSavedStateHandle?.get<String>("buyNowProductName")
                val buyNowProductBrand = prevSavedStateHandle?.get<String>("buyNowProductBrand")
                val buyNowProductPrice = prevSavedStateHandle?.get<Long>("buyNowProductPrice")
                
                // 바로 구매 상품이 있으면 해당 상품만, 없으면 장바구니 상품
                val products = if (buyNowProductId != null && buyNowProductName != null && buyNowProductPrice != null) {
                    // 바로 구매: 해당 상품 1개만
                    listOf(
                        OrderProduct(
                            id = buyNowProductId,
                            name = buyNowProductName,
                            detail = buyNowProductBrand ?: "",
                            price = buyNowProductPrice.toInt(),
                            qty = 1
                        )
                    )
                } else {
                    // 장바구니에서 온 경우: 장바구니 상품들 (더미)
                    listOf(
                        OrderProduct("1", "게이밍 키보드", "청축 스위치 / RGB", 99000, 1),
                        OrderProduct("2", "게이밍 마우스", "16000 DPI / 블랙", 59000, 2)
                    )
                }
                
                PaymentScreen(
                    address = addressState ?: com.example.devmart.ui.payment.Address(),
                    products = products,
                    onBackClick = { 
                        // 바로 구매 데이터 정리
                        prevSavedStateHandle?.remove<String>("buyNowProductId")
                        prevSavedStateHandle?.remove<String>("buyNowProductName")
                        prevSavedStateHandle?.remove<String>("buyNowProductBrand")
                        prevSavedStateHandle?.remove<Long>("buyNowProductPrice")
                        
                        val popped = nav.popBackStack()
                        if (!popped) {
                            // 백스택이 비어있으면 Cart로 이동
                            nav.navigate(Route.Cart.path) {
                                popUpTo(Route.MainGraph.path) { inclusive = false }
                            }
                        }
                    },
                    onNavigateToAddressSearch = {
                        nav.navigate(Route.AddressSearch.path)
                    },
                    onSaveAddress = { address ->
                        paymentViewModel.updateMyAddress(address)
                    },
                    onClickPayment = {
                        // TODO: 결제 처리
                    }
                )
            }
            composable(Route.AddressSearch.path) {
                DaumPostcodeScreen(
                    onAddressSelected = { postalCode, roadAddress, jibunAddress, _ ->
                        // 선택한 주소를 Payment 화면으로 전달
                        nav.previousBackStackEntry?.savedStateHandle?.apply {
                            set("selectedRoadAddress", roadAddress)
                            set("selectedPostalCode", postalCode)
                            set("selectedJibunAddress", jibunAddress)
                        }
                        nav.popBackStack()
                    },
                    onBack = { nav.popBackStack() }
                )
            }
            
            // Top100 검색 화면
            composable(Route.Top100.path) {
                val viewModel: Top100ViewModel = hiltViewModel()
                val allProducts by viewModel.allProducts.collectAsState()
                val searchResults by viewModel.searchResults.collectAsState()
                val query by viewModel.query.collectAsState()
                val recentSearches by viewModel.recentSearches.collectAsState()
                
                Top100SearchScreen(
                    allProducts = allProducts,
                    productCard = { product ->
                        ProductCard(
                            product = product,
                            onClick = { nav.navigate(Route.Detail.withId(product.id)) }
                        )
                    },
                    currentRoute = "top100",
                    onBottomNavClick = { route ->
                        when (route) {
                            "home" -> nav.navigate(Route.Home.path) {
                                popUpTo(Route.MainGraph.path) { inclusive = false }
                                launchSingleTop = true
                            }
                            "top100" -> { /* 현재 화면 */ }
                            "order" -> nav.navigate(Route.Cart.path) {
                                popUpTo(Route.MainGraph.path) { inclusive = false }
                                launchSingleTop = true
                            }
                            "MyPage" -> nav.navigate(Route.MyPage.path) {
                                popUpTo(Route.MainGraph.path) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    },
                    recentSearches = recentSearches,
                    popularKeywords = viewModel.popularKeywords,
                    searchResults = searchResults,
                    query = query,
                    onQueryChange = { viewModel.updateQuery(it) },
                    onClearQuery = { viewModel.clearQuery() },
                    onSearchSubmit = { viewModel.search() },
                    onKeywordClick = { viewModel.searchByKeyword(it) }
                )
            }
            
            // 장바구니 화면
            composable(Route.Cart.path) {
                val dummyCartState = CartScreenState(
                    products = listOf(
                        OrderProduct("1", "게이밍 키보드", "청축 / RGB", 99000, 1),
                        OrderProduct("2", "게이밍 마우스", "16000 DPI / 블랙", 59000, 2)
                    ),
                    priceSummary = CartPriceSummaryUiState(
                        productAmountText = "217,000원",
                        shippingFeeText = "3,000원",
                        orderAmountText = "220,000원"
                    ),
                    orderInfo = CartOrderInfoUiState(
                        totalQuantityText = "3개",
                        totalProductAmountText = "217,000원",
                        totalShippingFeeText = "3,000원"
                    )
                )
                
                CartScreen(
                    state = dummyCartState,
                    actions = CartScreenActions(
                        onBackClick = { 
                            val popped = nav.popBackStack()
                            if (!popped) {
                                nav.navigate(Route.Home.path) {
                                    popUpTo(Route.MainGraph.path) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        },
                        onBottomNavClick = { route ->
                            when (route) {
                                "home" -> nav.navigate(Route.Home.path) {
                                    popUpTo(Route.MainGraph.path) { inclusive = false }
                                    launchSingleTop = true
                                }
                                "top100" -> nav.navigate(Route.Top100.path) {
                                    popUpTo(Route.MainGraph.path) { inclusive = false }
                                    launchSingleTop = true
                                }
                                "order" -> { /* 현재 화면 */ }
                                "MyPage" -> nav.navigate(Route.MyPage.path) {
                                    popUpTo(Route.MainGraph.path) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        },
                        onProductIncrement = { /* TODO: 수량 증가 */ },
                        onProductDecrement = { /* TODO: 수량 감소 */ },
                        onProductRemove = { /* TODO: 아이템 삭제 */ },
                        onClickPayment = { nav.navigate(Route.Payment.path) }
                    ),
                    currentRoute = "order"
                )
            }
            
            // 마이페이지 화면
            composable(Route.MyPage.path) {
                UserScreen(
                    nickname = "사용자",
                    emailLocal = "user",
                    emailDomain = "devmart.com",
                    point = 1000,
                    shippingCount = 2,
                    likedCount = 5,
                    onEditProfile = { /* TODO: 프로필 수정 */ },
                    onBackClick = { nav.navigate(Route.Home.path) {
                        popUpTo(Route.MainGraph.path) { inclusive = false }
                        launchSingleTop = true
                    }},
                    onOrderHistoryClick = { nav.navigate(Route.OrderHistory.path) },
                    onCartClick = { nav.navigate(Route.Cart.path) },
                    onLikedClick = { nav.navigate(Route.Wishlist.path) }
                )
            }
            
            // 좋아요(위시리스트) 화면
            composable(Route.Wishlist.path) {
                // 더미 위시리스트 데이터
                val dummyWishlistState = WishlistScreenState(
                    items = listOf(
                        WishlistItemUi(1, "수아레", "데일리 헨리넥 니트 - 5 COLOR", "29,900원"),
                        WishlistItemUi(2, "에이카화이트", "EVERYDAY AECA CLOVER HOODIE", "63,200원"),
                        WishlistItemUi(3, "수아레", "데일리 라운드 니트 - 12 COLOR", "29,900원"),
                        WishlistItemUi(4, "무드인사이드", "노먼 브러쉬 노르딕 니트_6Color", "45,900원"),
                        WishlistItemUi(5, "테이크이지", "빈티지 오버 듀플린 체크 셔츠", "33,900원"),
                        WishlistItemUi(6, "도프제이슨", "[데일리룩 PICK] 솔리드 무톤 자켓", "169,000원")
                    )
                )
                
                WishlistScreen(
                    state = dummyWishlistState,
                    actions = WishlistScreenActions(
                        onBackClick = { 
                            val popped = nav.popBackStack()
                            if (!popped) {
                                nav.navigate(Route.MyPage.path) {
                                    popUpTo(Route.MainGraph.path) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        },
                        onBottomNavClick = { route ->
                            when (route) {
                                "home" -> nav.navigate(Route.Home.path) {
                                    popUpTo(Route.MainGraph.path) { inclusive = false }
                                    launchSingleTop = true
                                }
                                "top100" -> nav.navigate(Route.Top100.path) {
                                    popUpTo(Route.MainGraph.path) { inclusive = false }
                                    launchSingleTop = true
                                }
                                "order" -> nav.navigate(Route.Cart.path) {
                                    popUpTo(Route.MainGraph.path) { inclusive = false }
                                    launchSingleTop = true
                                }
                                "MyPage" -> nav.navigate(Route.MyPage.path) {
                                    popUpTo(Route.MainGraph.path) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        },
                        onItemClick = { item ->
                            // 위시리스트 아이템 클릭 시 상세 화면으로 이동
                            nav.navigate(Route.Detail.withId(item.id.toString()))
                        }
                    )
                )
            }
            
            // 구매내역 화면
            composable(Route.OrderHistory.path) {
                val dummyOrderHistory = OrderHistoryUiState(
                    orderGroups = listOf(
                        OrderGroupUi(
                            orderDateLabel = "2024.12.01",
                            items = listOf(
                                OrderSummaryUi(
                                    orderId = "1",
                                    brandName = "로지텍",
                                    productName = "게이밍 키보드",
                                    optionText = "청축 / RGB",
                                    priceText = "99,000원"
                                ),
                                OrderSummaryUi(
                                    orderId = "2",
                                    brandName = "로지텍",
                                    productName = "게이밍 마우스",
                                    optionText = "16000 DPI",
                                    priceText = "59,000원"
                                )
                            )
                        )
                    )
                )
                
                OrderHistoryScreen(
                    uiState = dummyOrderHistory,
                    onBack = { 
                        val popped = nav.popBackStack()
                        if (!popped) {
                            nav.navigate(Route.MyPage.path) {
                                popUpTo(Route.MainGraph.path) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    },
                    onBottomNavClick = { route ->
                        when (route) {
                            "home" -> nav.navigate(Route.Home.path) {
                                popUpTo(Route.MainGraph.path) { inclusive = false }
                                launchSingleTop = true
                            }
                            "top100" -> nav.navigate(Route.Top100.path) {
                                popUpTo(Route.MainGraph.path) { inclusive = false }
                                launchSingleTop = true
                            }
                            "order" -> nav.navigate(Route.Cart.path) {
                                popUpTo(Route.MainGraph.path) { inclusive = false }
                                launchSingleTop = true
                            }
                            "MyPage" -> nav.navigate(Route.MyPage.path) {
                                popUpTo(Route.MainGraph.path) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    },
                    onReorder = { /* TODO: 재주문 */ }
                )
            }
        }
    }
}
