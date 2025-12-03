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
        }
    }
}
