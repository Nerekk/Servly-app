package com.example.servly_app.features.chat.domain.usecase

import com.example.servly_app.core.data.util.PagedResponse
import com.example.servly_app.core.data.util.SortType
import com.example.servly_app.features.chat.data.ChatMessageInfo
import com.example.servly_app.features.chat.domain.ChatRepository
import java.time.LocalDateTime

class GetChatHistory(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(
        jobRequestId: Long,
        size: Long,
        sortType: SortType,
        lastTimestamp: LocalDateTime?): Result<PagedResponse<ChatMessageInfo>> {
        return chatRepository.getChatHistory(jobRequestId, size, sortType, lastTimestamp)
    }
}