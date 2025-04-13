package com.example.servly_app.core.data

import com.example.servly_app.core.data.util.PagedResponse
import com.example.servly_app.core.data.util.SortType
import com.example.servly_app.features.chat.data.ChatInfo
import com.example.servly_app.features.chat.data.ChatMessageInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDateTime

interface ChatService {
    @GET("api/chat/history/{jobRequestId}")
    suspend fun getChatHistory(
        @Path("jobRequestId") jobRequestId: Long,
        @Query("size") size: Long,
        @Query("sortType") sortType: SortType,
        @Query("lastMessageTimestamp") lastMessageTimestamp: LocalDateTime?
    ): Response<PagedResponse<ChatMessageInfo>>

    @GET("api/chat/{jobRequestId}")
    suspend fun getChatDetails(@Path("jobRequestId") jobRequestId: Long): Response<ChatInfo>
}