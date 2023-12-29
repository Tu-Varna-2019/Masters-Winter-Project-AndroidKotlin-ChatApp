package com.example.app_iliyan.view.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_iliyan.R
import com.example.app_iliyan.model.FriendRequest
import com.example.app_iliyan.view.components.isChatLoadedIndicator

@Composable
fun FriendRequestCardList(items: List<FriendRequest>) {
  if (items.isEmpty()) {
    isChatLoadedIndicator(
      messageContent = "You don't have any invited friends yet!",
      isChatLoaded = items.isEmpty(),
      image = R.drawable.no_contact
    )
  } else {
    LazyColumn { items(items) { item -> FriendRequestCard(item) } }
  }
}

@Composable
fun FriendRequestCard(item: FriendRequest) {
  var statusColor: Color
  var imageStatus: Int

  if (item.status == "Pending") {
    statusColor = Color.Black
    imageStatus = R.drawable.pending
  } else if (item.status == "Accepted") {
    statusColor = Color.Green
    imageStatus = R.drawable.accepted
  } else {
    statusColor = Color.Red
    imageStatus = R.drawable.rejected
  }

  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Card(
      modifier =
        Modifier.padding(0.dp)
          // .width(250.dp)
          // .clickable {  }
          .fillMaxSize()
          .padding(8.dp),
      elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
      Row(modifier = Modifier.padding(18.dp)) {
        Image(
          painter =
            painterResource(
              // Display the correct icon based on the status of the friend request
              id = imageStatus
            ),
          contentDescription = item.status,
          modifier = Modifier.size(40.dp, 40.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))
        Column {
          Text(
            text = item.status,
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = statusColor)
          )
          Spacer(modifier = Modifier.height(8.dp))

          Text("Recipient: " + item.recipient.email, style = TextStyle(fontSize = 14.sp))
        }
      }
    }
  }
}
