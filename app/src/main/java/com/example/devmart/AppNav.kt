package com.example.devmart

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.devmart.ui.auth.LoginScreen
import com.example.devmart.ui.auth.LoginViewModel
import com.example.devmart.ui.auth.SplashScreen
import com.example.devmart.ui.auth.Top100SearchScreen
import com.example.devmart.ui.auth.Top100ViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.example.devmart.ui.cart.CartScreen
import com.example.devmart.ui.cart.CartScreenState
import com.example.devmart.ui.cart.CartScreenActions
import com.example.devmart.ui.cart.CartPriceSummaryUiState
import com.example.devmart.ui.cart.CartOrderInfoUiState
import com.example.devmart.ui.component.ProductCard
import com.example.devmart.ui.home.HomeScreen
import com.example.devmart.ui.home.HomeViewModel
import com.example.devmart.ui.home.ProductDetailScreen
import com.example.devmart.ui.home.ProductDetailViewModel
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
import com.example.devmart.ui.wishlist.WishlistScreenActions
import com.example.devmart.ui.wishlist.WishlistViewModel

@Suppress("DEPRECATION")
@Composable
fun AppNav() {
    val nav = rememberNavController()
    @Suppress("DEPRECATION")
    val session: SessionViewModel = hiltViewModel()
    val auth by session.auth.collectAsState()

    // 인증 상태가 Unauthenticated로 변경되면 로그인 화면으로 이동 (토큰 만료 등)
    LaunchedEffect(auth) {
        if (auth is AuthState.Unauthenticated) {
            val currentRoute = nav.currentDestination?.route
            // 이미 로그인 화면이 아니고, 스플래시 화면도 아닌 경우에만 이동
            if (currentRoute != null && 
                currentRoute != Route.Login.path && 
                currentRoute != Route.AuthGraph.path &&
                currentRoute != Route.Splash.path) {
                // 현재 경로를 저장하고 로그인 화면으로 이동
                nav.currentBackStackEntry?.savedStateHandle?.set("previousRoute", currentRoute)
                nav.navigate(Route.AuthGraph.path) {
                    popUpTo(Route.Splash.path) { inclusive = false }
                }
            }
        }
    }

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
            composable(Route.Login.path) { backStackEntry ->
                val loginViewModel: LoginViewModel = hiltViewModel()
                val loginState by loginViewModel.uiState.collectAsState()
                val context = LocalContext.current
                
                // 로그인 전 경로 가져오기
                val savedStateHandle = backStackEntry.savedStateHandle
                val previousRoute = savedStateHandle.get<String>("previousRoute")
                
                // 로그인 상태 관찰 - 인증되면 원래 페이지로 이동
                LaunchedEffect(auth) {
                    if (auth is AuthState.Authenticated) {
                        // 저장된 경로가 있으면 해당 경로로, 없으면 Home으로 이동
                        val targetRoute = previousRoute?.takeIf { 
                            it.isNotEmpty() && 
                            it != Route.Login.path && 
                            it != Route.AuthGraph.path &&
                            it != Route.Splash.path
                        } ?: Route.Home.path
                        
                        try {
                            // 저장된 경로로 이동 시도
                            nav.navigate(targetRoute) {
                                popUpTo(Route.AuthGraph.path) { inclusive = true }
                            }
                        } catch (e: Exception) {
                            // 경로가 유효하지 않으면 Home으로 이동
                            Log.w("AppNav", "Invalid route: $targetRoute, navigating to Home", e)
                            nav.navigate(Route.Home.path) {
                                popUpTo(Route.AuthGraph.path) { inclusive = true }
                            }
                        }
                        
                        // 사용 후 초기화
                        savedStateHandle.remove<String>("previousRoute")
                    }
                }
                
                // 카카오 로그인 콜백
                val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                    if (error != null) {
                        Log.e("KakaoLogin", "카카오 로그인 실패", error)
                    } else if (token != null) {
                        // 카카오 액세스 토큰을 백엔드에 전달
                        loginViewModel.loginWithKakao(token.accessToken) { errorMessage ->
                            Log.e("KakaoLogin", "백엔드 로그인 실패: $errorMessage")
                        }
                    }
                }
                
                LoginScreen(
                    state = loginState,
                    onClickKakao = {
                        // 카카오 로그인 시작
                        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                            // 카카오톡 로그인
                            UserApiClient.instance.loginWithKakaoTalk(context, callback = kakaoLoginCallback)
                        } else {
                            // 카카오계정 로그인
                            UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoLoginCallback)
                        }
                    }
                )
            }
        }
        navigation(startDestination = Route.Home.path, route = Route.MainGraph.path) {
            composable(Route.Home.path) {
                @Suppress("DEPRECATION")
                val homeViewModel: HomeViewModel = hiltViewModel()
                val homeState by homeViewModel.uiState.collectAsState()
                
                HomeScreen(
                    products = homeState.products,
                    categories = homeState.categories.map { it.categoryName },
                    isLoading = homeState.isLoading,
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
            composable(Route.Detail.path) {
                @Suppress("DEPRECATION")
                val detailViewModel: ProductDetailViewModel = hiltViewModel()
                val detailState by detailViewModel.uiState.collectAsState()
                
                val currentProduct = detailState.product
                
                ProductDetailScreen(
                    product = currentProduct,
                    reviews = detailState.reviews,
                    isReviewLoading = detailState.isReviewLoading,
                    isLiked = detailState.isLiked,
                    likeMessage = detailState.likeMessage,
                    onBackClick = { nav.popBackStack() },
                    onSearchClick = { nav.navigate(Route.Top100.path) },
                    onLikeClick = { detailViewModel.toggleLike() },
                    onClearLikeMessage = { detailViewModel.clearLikeMessage() },
                    onAddToCart = { /* TODO: 장바구니 추가 기능 구현 */ },
                    onBuyNow = {
                        // 바로 구매: 현재 상품 정보를 Payment로 전달
                        currentProduct?.let { product ->
                            nav.currentBackStackEntry?.savedStateHandle?.apply {
                                set("buyNowProductId", product.id)
                                set("buyNowProductName", product.title)
                                set("buyNowProductBrand", product.brand)
                                set("buyNowProductPrice", product.price)
                            }
                            nav.navigate(Route.Payment.path)
                        }
                    }
                )
            }
            composable(Route.Payment.path) { backStackEntry ->
                @Suppress("DEPRECATION")
                val paymentViewModel: PaymentViewModel = hiltViewModel()
                val addressState by paymentViewModel.address.collectAsState()
                val paymentState by paymentViewModel.paymentState.collectAsState()
                val userMileage by paymentViewModel.mileage.collectAsState()
                
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
                
                // 바로 구매 여부 확인
                val isBuyNow = buyNowProductId != null && buyNowProductName != null && buyNowProductPrice != null
                
                // 바로 구매 상품이 있으면 해당 상품만, 없으면 장바구니 상품
                val products = if (isBuyNow) {
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
                    availableMileage = userMileage, // 실제 유저 마일리지 (API에서 조회)
                    paymentState = paymentState,
                    isBuyNow = isBuyNow,
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
                    onClickPayment = { mileageToUse ->
                        // 첫 번째 상품으로 주문 생성 (여러 상품은 추후 확장)
                        val firstProduct = products.firstOrNull()
                        if (firstProduct != null) {
                            paymentViewModel.createOrder(
                                itemId = firstProduct.id.toLongOrNull() ?: 0L,
                                quantity = firstProduct.qty,
                                mileageToUse = mileageToUse
                            )
                        }
                    },
                    onPaymentComplete = {
                        // 바로 구매 데이터 정리
                        prevSavedStateHandle?.remove<String>("buyNowProductId")
                        prevSavedStateHandle?.remove<String>("buyNowProductName")
                        prevSavedStateHandle?.remove<String>("buyNowProductBrand")
                        prevSavedStateHandle?.remove<Long>("buyNowProductPrice")
                        
                        // 홈으로 이동
                        nav.navigate(Route.Home.path) {
                            popUpTo(Route.MainGraph.path) { inclusive = false }
                        }
                    },
                    onResetPaymentState = {
                        paymentViewModel.resetPaymentState()
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
                val userViewModel: com.example.devmart.ui.user.UserViewModel = hiltViewModel()
                val userState by userViewModel.uiState.collectAsState()
                
                UserScreen(
                    nickname = userState.nickname.ifEmpty { "사용자" },
                    emailLocal = userState.emailLocal.ifEmpty { "user" },
                    emailDomain = userState.emailDomain.ifEmpty { "devmart.com" },
                    point = userState.point,
                    shippingCount = userState.shippingCount,
                    likedCount = userState.likedCount,
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
                @Suppress("DEPRECATION")
                val wishlistViewModel: WishlistViewModel = hiltViewModel()
                val wishlistState by wishlistViewModel.uiState.collectAsState()
                
                WishlistScreen(
                    state = wishlistState,
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
