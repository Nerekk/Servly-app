package com.example.servly_app.features._provider._navigation

sealed class ProviderNavItem(
    val route: String
) {
    object JobPostingDetails: ProviderNavItem("jobpostingdetails")
    object JobDetailsView: ProviderNavItem("jobdetails")
    object ProfilePreview: ProviderNavItem("profile_preview")
    object Chat: ProviderNavItem("chat")
}