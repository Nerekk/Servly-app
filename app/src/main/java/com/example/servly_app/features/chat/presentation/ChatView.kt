package com.example.servly_app.features.chat.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.servly_app.R
import com.example.servly_app.core.components.BasicScreenLayout
import com.example.servly_app.core.components.LoadingScreen
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features.chat.data.ChatMessageInfo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Preview(
    showBackground = true,
    locale = "en",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "DefaultPreviewLightEN"
)
@Preview(
    showBackground = true,
    locale = "pl",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDarkPL"
)
@Composable
fun PreviewChatView() {
    val navController = rememberNavController()
    AppTheme {
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatView(
    navController: NavController,
    jobRequestId: Long
) {
    val viewModel: ChatViewModel = hiltViewModel<ChatViewModel, ChatViewModel.ChatViewModelFactory> { factory ->
        factory.create(jobRequestId)
    }

    val chatInfoState = viewModel.chatInfo.collectAsState()

    if (chatInfoState.value == null) {
        LoadingScreen()
        return
    }

    val messagesState = viewModel.messages.collectAsState()
    val messageToSend = viewModel.messageToSend.collectAsState()
    val fullyLoaded = viewModel.fullyLoaded.collectAsState()

    val listState = rememberLazyListState()

    var isFirstLoad by remember { mutableStateOf(true) }

    LaunchedEffect(messagesState.value) {
        if (messagesState.value.isNotEmpty()) {
            val lastIndex = messagesState.value.lastIndex
            val visibleItems = listState.layoutInfo.visibleItemsInfo
            val lastVisibleIndex = visibleItems.lastOrNull()?.index ?: 0

            val isAtBottom = lastVisibleIndex >= lastIndex - 1

            if (isAtBottom || isFirstLoad) {
                listState.animateScrollToItem(lastIndex)
                isFirstLoad = false
            }
        }
    }

    LaunchedEffect(listState.canScrollBackward, fullyLoaded) {
        if (!listState.canScrollBackward && !fullyLoaded.value) {
            viewModel.loadHistory()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "pers"
                        )
                        Text(chatInfoState.value!!.secondName)

                    }
                },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(vertical = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = messageToSend.value,
                        onValueChange = { viewModel.updateMessageToSend(it) },
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(50)),
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        ),
                        placeholder = { Text(stringResource(R.string.message)) }
                    )
                    if (messageToSend.value.isNotBlank()) {
                        IconButton(
                            onClick = { viewModel.sendMessage() }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send"
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        BasicScreenLayout(padding, isListInContent = true) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                state = listState
            ) {
                items(messagesState.value) { messageInfo ->
                    MessageBox(messageInfo, chatInfoState.value!!.myUid)
                }
            }
        }
    }
}

@Composable
private fun MessageBox(
    message: ChatMessageInfo,
    myUid: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                if (message.isFromMe(myUid)) {
                    PaddingValues(start = 40.dp, top = 4.dp, bottom = 4.dp)
                } else {
                    PaddingValues(end = 40.dp, top = 4.dp, bottom = 4.dp)
                }
            ),
        horizontalArrangement = if (message.isFromMe(myUid)) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (message.isFromMe(myUid)) {
            Text(
                text = formatChatTime(message.createdAt),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(end = 4.dp)
            )
        }
        Card(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isFromMe(myUid)) 16.dp else 0.dp,
                bottomEnd = if (message.isFromMe(myUid)) 0.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isFromMe(myUid)) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceContainer
                }
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Text(
                text = message.content,
                color = if (message.isFromMe(myUid)) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                modifier = Modifier.padding(12.dp)
            )
        }
        if (!message.isFromMe(myUid)) {
            Text(
                text = formatChatTime(message.createdAt),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

fun formatChatTime(dateTime: LocalDateTime): String {
    val now = LocalDate.now()

    return if (dateTime.toLocalDate() == now) {
        dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    } else {
        dateTime.format(DateTimeFormatter.ofPattern("dd.MM HH:mm"))
    }
}
