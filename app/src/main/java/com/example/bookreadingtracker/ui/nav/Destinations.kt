package com.example.bookreadingtracker.ui.nav
object Destinations {
    const val HOME = "home"
    const val DISCOVER = "discover"
    const val SHELF = "shelf"
    const val ANALYTICS = "analytics"
    const val SETTINGS = "settings"
    const val DETAIL = "detail/{bookId}"
    fun detailRoute(bookId: String) = "detail/$bookId"
}
