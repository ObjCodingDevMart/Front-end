package com.example.devmart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.*
import com.example.devmart.ui.auth.LoginScreen
import com.example.devmart.ui.home.HomeScreen
import com.example.devmart.ui.session.AuthState
import com.example.devmart.ui.session.SessionViewModel

@Composable
fun AppNav() {
    val nav = rememberNavController()
    val session: SessionViewModel = hiltViewModel()
    val auth by session.auth.collectAsState()

    LaunchedEffect(auth) {
        when (auth) {
            is AuthState.Authenticated ->
                nav.navigate(Route.MainGraph.path) { popUpTo(0) { inclusive = true } }
            AuthState.Unauthenticated ->
                nav.navigate(Route.AuthGraph.path) { popUpTo(0) { inclusive = true } }
            AuthState.Loading -> Unit
        }
    }

    if (auth is AuthState.Loading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }

    NavHost(
        navController = nav,
        startDestination = if (auth is AuthState.Authenticated) Route.MainGraph.path else Route.AuthGraph.path
    ) {
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
            composable(Route.Detail.path) { back ->
                val id = back.arguments?.getString("id").orEmpty()
                HomeScreen(
                    detailId = id,
                    onNavigateToRoute = { route -> 
                        when (route) {
                            "home" -> nav.navigate(Route.Home.path) { 
                                popUpTo(Route.MainGraph.path) { inclusive = false }
                            }
                            "top100" -> { /* TODO */ }
                            "order" -> { /* TODO */ }
                            "login" -> { /* TODO */ }
                        }
                    }
                )
            }
        }
    }
}
