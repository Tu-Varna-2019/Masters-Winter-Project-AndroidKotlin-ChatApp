package com.example.app_iliyan.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.app_iliyan.helpers.MaskData

class Message {
  var id by mutableStateOf(0)
  var content by mutableStateOf("")
  var attachmentURL by mutableStateOf("")
  var timestamp by mutableStateOf("")
  var sender by mutableStateOf(User(0, "", "", ""))

  constructor(id: Int, content: String, attachmentURL: String, timestamp: String, sender: User) {
    this.id = id
    this.content = content
    this.attachmentURL = attachmentURL
    this.timestamp = timestamp
    this.sender = sender
  }

  fun base64EncodeMessage(): Array<String> {
    return arrayOf(
      MaskData.base64Encode(id.toString()),
      MaskData.base64Encode(content),
      // Don't encode the attachmentURL, since it's already done via filePicker
      attachmentURL,
      MaskData.base64Encode(timestamp),
    )
  }

  override fun toString(): String {
    return "Message(id=$id, content='$content', attachmentURL='$attachmentURL', timestamp='$timestamp', sender=$sender)"
  }
}