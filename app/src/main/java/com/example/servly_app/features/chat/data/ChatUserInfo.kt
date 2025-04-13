package com.example.servly_app.features.chat.data

import com.example.servly_app.core.data.util.Role

data class ChatUserInfo(
    val uid: String,
    val role: Role,
)