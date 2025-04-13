package com.example.servly_app.features.chat.domain.usecase

import com.example.servly_app.features.chat.data.ChatInfo
import com.example.servly_app.features.chat.domain.ChatRepository

class GetChatDetails(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(jobRequestId: Long): Result<ChatInfo> {
        return chatRepository.getChatDetails(jobRequestId)
    }
}