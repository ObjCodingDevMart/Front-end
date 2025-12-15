package com.example.devmart.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home : BottomNavItem("home", "HOME", Icons.Default.Home)
    data object Top100 : BottomNavItem("top100", "TOP 100", Icons.Default.Favorite)
    data object Order : BottomNavItem("order", "ORDER", Icons.Default.ShoppingCart)
    data object MyPage : BottomNavItem("MyPage", "My Page", Icons.Default.Person)
}

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Top100,
        BottomNavItem.Order,
        BottomNavItem.MyPage
    )

    NavigationBar(
        modifier = modifier,
        containerColor = Color(0xFFEFF5FB),
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 12.sp
                    )
                },
                selected = currentRoute == item.route,
                onClick = { onItemClick(item.route) }
            )
        }
    }
}

