package com.example.servly_app.features.chat.domain

import com.example.servly_app.core.data.ChatService
import com.example.servly_app.core.data.util.PagedResponse
import com.example.servly_app.core.data.util.SortType
import com.example.servly_app.core.util.ErrorStore
import com.example.servly_app.features.chat.data.ChatInfo
import com.example.servly_app.features.chat.data.ChatMessageInfo
import java.time.LocalDateTime

class ChatRepository(private val chatService: ChatService) {
    suspend fun getChatHistory(jobRequestId: Long, size: Long, sortType: SortType, lastTimestamp: LocalDateTime?): Result<PagedResponse<ChatMessageInfo>> {
        return try {
            val response = chatService.getChatHistory(jobRequestId, size, sortType, lastTimestamp)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ErrorStore.addError(e.message.toString())
            Result.failure(e)
        }
    }

    suspend fun getChatDetails(jobRequestId: Long): Result<ChatInfo> {
        return try {
            val response = chatService.getChatDetails(jobRequestId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ErrorStore.addError(e.message.toString())
            Result.failure(e)
        }
    }
}