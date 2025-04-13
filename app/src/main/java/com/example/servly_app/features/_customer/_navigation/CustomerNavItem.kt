package com.example.servly_app.features._customer._navigation

sealed class CustomerNavItem(
    val route: String
) {
    object JobFormView: CustomerNavItem("jobformview")
    object OrderDetailsView: CustomerNavItem("orderdetailsview")
    object ProfilePreview: CustomerNavItem("profile_preview")
    object Chat: CustomerNavItem("chat")
}