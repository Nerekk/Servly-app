package com.example.servly_app.features.chat.data

import java.time.LocalDateTime

data class ChatMessageInfo(
    val id: Long? = null,
    val jobRequestId: Long? = null,
    val userUid: String,
    val content: String,
    val createdAt: LocalDateTime
) {
    fun isFromMe(uid: String): Boolean {
        return uid == userUid
    }
}