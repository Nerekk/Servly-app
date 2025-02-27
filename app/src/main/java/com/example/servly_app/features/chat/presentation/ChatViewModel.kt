package com.example.servly_app.features.chat.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.core.data.util.SortType
import com.example.servly_app.core.domain.usecase.job_request.JobRequestUseCases
import com.example.servly_app.features.chat.data.ChatInfo
import com.example.servly_app.features.chat.data.ChatMessageInfo
import com.example.servly_app.features.chat.data.util.LocalDateTimeAdapter
import com.example.servly_app.features.chat.domain.ChatWebSocketClient
import com.example.servly_app.features.chat.domain.usecase.ChatUseCases
import com.example.servly_app.features.role_selection.data.CustomerInfo
import com.example.servly_app.features.role_selection.data.ProviderInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime

data class ChatState(
    val chatInfo: ChatInfo? = null,
    val messages: List<ChatMessageInfo> = emptyList()
)

@HiltViewModel(assistedFactory = ChatViewModel.ChatViewModelFactory::class)
class ChatViewModel @AssistedInject constructor(
    private val jobRequestUseCases: JobRequestUseCases,
    private val chatUseCases: ChatUseCases,
    @Assisted val jobRequestId: Long
): ViewModel() {
    @AssistedFactory
    interface ChatViewModelFactory {
        fun create(jobRequestId: Long): ChatViewModel
    }

    private lateinit var webSocketClient: ChatWebSocketClient

    private val _chatInfo = MutableStateFlow<ChatInfo?>(null)
    val chatInfo = _chatInfo.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessageInfo>>(emptyList())
    val messages: StateFlow<List<ChatMessageInfo>> = _messages

    private var isLoading = false
    private var lastLoadedTimestamp: LocalDateTime? = null

    private val _fullyLoaded = MutableStateFlow(false)
    val fullyLoaded = _fullyLoaded.asStateFlow()

    private val _messageToSend = MutableStateFlow<String>("")
    val messageToSend = _messageToSend.asStateFlow()

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    init {
        viewModelScope.launch {
            val token = fetchFirebaseToken()
            webSocketClient = ChatWebSocketClient(jobRequestId, token)
            webSocketClient.connect()
            listenForIncomingMessages()
            loadHistory()

            loadChatDetails()
        }
    }

    fun updateMessageToSend(text: String) {
        _messageToSend.value = text
    }

    private fun listenForIncomingMessages() {
        viewModelScope.launch {
            webSocketClient.incomingMessages.collect { newMessageJson ->
                newMessageJson?.let {
                    Log.i("NEW_MESSAGE_JSON", newMessageJson)
                    val newMessage = parseJson(it)
                    Log.i("NEW_MESSAGE", newMessage.toString())
                    _messages.value += listOf(newMessage)
                }
            }
        }
    }

    private fun parseJson(json: String): ChatMessageInfo {
        return gson.fromJson(json, ChatMessageInfo::class.java)
    }

    fun sendMessage() {
        viewModelScope.launch {
            webSocketClient.sendMessage(_messageToSend.value)
            _messageToSend.value = ""
        }
    }

    fun loadHistory() {
        if (isLoading || _fullyLoaded.value) return
        isLoading = true
        Log.i("Chat_history", "Loading!")

        viewModelScope.launch {
            val result = chatUseCases.getChatHistory(jobRequestId, 20, SortType.DESCENDING, lastLoadedTimestamp)
            result.fold(
                onSuccess = { response ->
                    if (response.content.isEmpty()) {
                        _fullyLoaded.value = true
                    } else {
                        val list = response.content.reversed()

                        _messages.value = list + _messages.value
                        lastLoadedTimestamp = list.first().createdAt
                    }
                },
                onFailure = { e ->
                    Log.i("CHAT_ERROR", e.message.toString())
                }
            )
            isLoading = false
        }
    }

    private fun loadChatDetails() {
        viewModelScope.launch {
            val result = chatUseCases.getChatDetails(jobRequestId)
            result.fold(
                onSuccess = { chatInfo ->
                    _chatInfo.value = chatInfo
                },
                onFailure = { e ->
                    Log.i("CHAT_ERROR", e.message.toString())
                }
            )
        }
    }

    private suspend fun fetchFirebaseToken(): String {
        val user = FirebaseAuth.getInstance().currentUser ?: throw IllegalStateException("Firebase user is null")
        return user.getIdToken(false).await().token ?: throw IllegalStateException("Failed to retrieve token")
    }

    override fun onCleared() {
        super.onCleared()
        webSocketClient.disconnect()
    }
}