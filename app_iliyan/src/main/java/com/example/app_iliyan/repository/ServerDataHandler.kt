package com.example.app_iliyan.repository

import com.example.app_iliyan.dataclass.GroupChatData
import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.User
import kotlinx.serialization.json.Json

class ServerDataHandler {
  companion object {
    fun parseResponse(jsonString: String): ServerResponse {
      val json = Json { ignoreUnknownKeys = true }
      return json.decodeFromString(ServerResponse.serializer(), jsonString)
    }

    fun convertGroupChatDataToModel(groupChatData: GroupChatData): GroupChat {
      val users =
          groupChatData.users.map { userData ->
            User(username = userData.username, email = userData.email, password = userData.password)
          }
      return GroupChat(name = groupChatData.name, users = users)
    }
  }
}
