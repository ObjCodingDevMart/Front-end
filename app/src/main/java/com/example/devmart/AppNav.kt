package com.example.devmart

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.*
import com.example.devmart.ui.auth.LoginScreen
import com.example.devmart.ui.auth.SplashScreen
import com.example.devmart.ui.home.HomeScreen
import com.example.devmart.ui.home.ProductDetailScreen
import com.example.devmart.ui.session.AuthState
import com.example.devmart.ui.session.SessionViewModel
import com.example.devmart.ui.payment.PaymentScreen
import com.example.devmart.ui.payment.PaymentViewModel
import com.example.devmart.ui.payment.AddressSearchScreen
import com.example.devmart.ui.payment.AddressSearchViewModel
import com.example.devmart.ui.payment.OrderProduct

@Composable
fun AppNav() {
    val nav = rememberNavController()
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
            composable(Route.Login.path) { LoginScreen() }
        }
        navigation(startDestination = Route.Home.path, route = Route.MainGraph.path) {
            composable(Route.Home.path) { 
                HomeScreen(
                    openDetail = { id -> nav.navigate(Route.Detail.withId(id)) },
                    onNavigateToRoute = { route -> 
                        // 하단 네비게이션 바 라우팅 처리
                        when (route) {
                            "home" -> nav.navigate(Route.Home.path) { 
                                popUpTo(Route.MainGraph.path) { inclusive = false }
                            }
                            "top100" -> { /* TODO: TOP 100 화면으로 이동 */ }
                            "order" -> { /* TODO: ORDER 화면으로 이동 */ }
                            "login" -> { /* TODO: 로그인 화면으로 이동 */ }
                        }
                    }
                ) 
            }
            composable(Route.Detail.path) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id").orEmpty()
                ProductDetailScreen(
                    product = null, // TODO: Repository에서 id로 상품 정보 가져오기
                    onBackClick = { nav.popBackStack() },
                    onSearchClick = { /* TODO: 검색 화면으로 이동 */ },
                    onLikeClick = { /* TODO: 좋아요 기능 구현 */ },
                    onAddToCart = { /* TODO: 장바구니 추가 기능 구현 */ },
                    onBuyNow = { /* TODO: 바로 구매 기능 구현 */ }
                )
            }
            composable(Route.Payment.path) { backStackEntry ->
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
                
                // 더미 상품 (나중에 장바구니에서 전달받도록 수정)
                val products = listOf(
                    OrderProduct("1", "게이밍 키보드", "청축 스위치 / RGB", 99000, 1),
                    OrderProduct("2", "게이밍 마우스", "16000 DPI / 블랙", 59000, 2)
                )
                
                PaymentScreen(
                    address = addressState ?: com.example.devmart.ui.payment.Address(),
                    products = products,
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
                val addressSearchViewModel: AddressSearchViewModel = hiltViewModel()
                
                AddressSearchScreen(
                    keyword = addressSearchViewModel.keyword,
                    results = addressSearchViewModel.results,
                    onKeywordChange = { addressSearchViewModel.updateKeyword(it) },
                    onSearch = { addressSearchViewModel.search() },
                    onSelect = { address ->
                        // 개별 필드로 저장 (Parcelable 없이 전달)
                        nav.previousBackStackEntry?.savedStateHandle?.apply {
                            set("selectedRoadAddress", address.roadAddress)
                            set("selectedPostalCode", address.postalCode)
                            set("selectedJibunAddress", address.jibunAddress)
                        }
                        nav.popBackStack()
                    },
                    onBack = { nav.popBackStack() }
                )
            }


        }
    }
}
