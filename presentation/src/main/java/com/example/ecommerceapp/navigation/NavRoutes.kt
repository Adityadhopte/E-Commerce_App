package com.example.ecommerceapp.navigation

import com.example.ecommerceapp.model.UiProductModel
import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes {
    @Serializable
    object HomeScreen

    @Serializable
    object CartScreen

    @Serializable
    object ProfileScreen

    @Serializable
    object NotificationScreen

    @Serializable
    data class ProductDetails(val product: UiProductModel)
}