package com.example.devmart

sealed class Route(val path: String) {
    data object AuthGraph : Route("auth")
    data object MainGraph : Route("main")
    data object Login : Route("login")
    data object Home : Route("home")
    data object Detail : Route("detail/{id}") { fun withId(id: String) = "detail/$id" }
    data object Payment : Route("payment")
    data object AddressSearch : Route("addressSearch")
}
