package com.example.devmart

sealed class Route(val path: String) {
    data object Splash : Route("splash")
    data object AuthGraph : Route("auth")
    data object MainGraph : Route("main")
    data object Login : Route("login")
    data object Home : Route("home")
    data object Detail : Route("detail/{id}") { fun withId(id: String) = "detail/$id" }
    data object Top100 : Route("top100")
    data object Cart : Route("order")
    data object MyPage : Route("mypage")
    data object OrderHistory : Route("orderHistory")
    data object Payment : Route("payment")
    data object AddressSearch : Route("addressSearch")
    data object Wishlist : Route("wishlist")
}
