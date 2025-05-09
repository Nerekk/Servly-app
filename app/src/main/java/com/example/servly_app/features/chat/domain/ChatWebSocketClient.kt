package com.example.servly_app.features.chat.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class ChatWebSocketClient(
    private val jobRequestId: Long,
    private val token: String
) {
    private var webSocket: WebSocket? = null
    private val _incomingMessages = MutableStateFlow<String?>(null)
    val incomingMessages = _incomingMessages.asStateFlow()

    fun connect() {
        val request = Request.Builder()
            .url("ws://10.0.2.2:8080/ws/chat?jobRequestId=$jobRequestId")
            .addHeader("Authorization", "Bearer $token")
            .build()

        val client = OkHttpClient()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                _incomingMessages.value = text
            }
        })
    }

    fun sendMessage(content: String) {
        val jsonMessage = """{"content": "${content.replace("\n", "\\n")}"}"""
        webSocket?.send(jsonMessage)
    }

    fun disconnect() {
        webSocket?.close(1000, "Goodbye")
    }
}