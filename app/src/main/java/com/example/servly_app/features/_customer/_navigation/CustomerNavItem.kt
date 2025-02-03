package com.example.servly_app.features._customer._navigation

sealed class CustomerNavItem(
    val route: String
) {
    object JobFormView: CustomerNavItem("jobformview")
}