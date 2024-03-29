package com.example.app_iliyan.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_iliyan.dataclass.GroupChatData
import com.example.app_iliyan.helpers.Utils
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.Message
import com.example.app_iliyan.repository.ChatInterface
import com.example.app_iliyan.repository.ChatRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel(), ChatInterface {
  override val chatRepo: ChatRepo = ChatRepo()
  val backHomeEvent = MutableLiveData<Boolean>()

  private val _selectedGroupChat = MutableStateFlow<GroupChat>(GroupChat(0, "", emptyList()))
  val selectedGroupChat: StateFlow<GroupChat> = _selectedGroupChat

  val pickFileEvent = MutableLiveData<Boolean>()
  val encodedURI = MutableStateFlow<String>("")

  fun fetchMessagesByGroupChat(groupChatDataArg: GroupChatData) {
    viewModelScope.launch(Dispatchers.Main) {
      while (isActive) {
        try {
          // Fetch messages
          val fetchMessages = chatRepo.messageRepo.getAllMessages(groupChatDataArg)
          _selectedGroupChat.value = fetchMessages
        } catch (e: Exception) {
          Utils.logger.error("Error fetching messages!")
        }
        delay(Utils.SERVER_REQUEST_DELAY)
      }
    }
  }

  suspend fun handleSendMessageClick(
    groupChat: GroupChat,
    typedMessage: String,
  ) {

    val updatedMessages =
      chatRepo.messageRepo.sendMessage(groupChat.id.toString(), typedMessage, encodedURI.value)
    if (!updatedMessages.isEmpty()) _selectedGroupChat.value.messages = updatedMessages
  }

  suspend fun handleDeleteMessageClick(deletedMessage: Message) {

    val isMessageDeleted = chatRepo.messageRepo.deleteMessage(deletedMessage.id)

    if (isMessageDeleted)
      _selectedGroupChat.value.messages =
        _selectedGroupChat.value.messages.filterNot { it.id == deletedMessage.id }
  }

  fun handleAddAttachmentClick() {
    pickFileEvent.value = true
  }

  fun handleAddAttachmentClick(encodedUri: String) {
    encodedURI.value = encodedUri
  }
}
