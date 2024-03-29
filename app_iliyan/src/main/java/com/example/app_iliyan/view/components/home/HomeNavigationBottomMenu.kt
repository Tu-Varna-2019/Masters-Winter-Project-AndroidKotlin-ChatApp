package com.example.app_iliyan.view.components.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.state.UserOptions
import com.example.app_iliyan.view.components.dialog_box.DialogAddGroupChat
import com.example.app_iliyan.view.components.dialog_box.DialogSendFriendRequest
import com.example.app_iliyan.view.components.dialog_box.SnackbarManager
import com.example.app_iliyan.view_model.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeNavigationBottomMenu(groupchatList: List<GroupChat>, userOptions: UserOptions) {

  val homeViewModel: HomeViewModel = viewModel()
  var showMenu by remember { mutableStateOf(false) }
  val showAddFriendDialog = remember { mutableStateOf(false) }
  val showAddChatDialog = remember { mutableStateOf(false) }

  Scaffold(
    bottomBar = {
      NavigationBar {
        NavigationBarItem(
          icon = { Icon(Icons.Filled.CheckCircle, contentDescription = null) },
          label = { Text("Chat") },
          selected = userOptions.selectedTab == "Chat",
          onClick = {
            userOptions.selectedTab = "Chat"
            HomeViewModel.handleSelectedTabClick(userOptions)
          }
        )
        NavigationBarItem(
          icon = { Icon(Icons.Filled.AccountBox, contentDescription = null) },
          label = { Text("Friends") },
          selected = userOptions.selectedTab == "Friends",
          onClick = {
            userOptions.selectedTab = "Friends"
            HomeViewModel.handleSelectedTabClick(userOptions)
          }
        )
        NavigationBarItem(
          icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
          label = { Text("Settings") },
          selected = userOptions.selectedTab == "Settings",
          onClick = {
            userOptions.selectedTab = "Settings"

            HomeViewModel.handleSelectedTabClick(userOptions)
          }
        )
      }
    },
    floatingActionButton = {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        // Hide + button if user is in Settings tab
        if (userOptions.selectedTab != "Settings") {
          FloatingActionButton(
            onClick = { showMenu = !showMenu },
            modifier = Modifier.padding(16.dp)
          ) {
            Icon(Icons.Filled.Add, contentDescription = "Add")
          }
        }
        MaterialTheme(
          colorScheme =
            MaterialTheme.colorScheme.copy(surface = Color(red = 234, green = 221, blue = 255))
        ) {
          DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
            modifier = Modifier.align(Alignment.TopEnd)
          ) {
            DropdownMenuItem(
              onClick = { showAddChatDialog.value = true },
              text = { Text("Create new chat") }
            )
            DropdownMenuItem(
              onClick = { showAddFriendDialog.value = true },
              text = { Text("Add friend") }
            )
          }
          // Dialog box for sending friend request
          if (showAddFriendDialog.value) {
            DialogSendFriendRequest(
              onConfirm = { recipientEmail ->
                CoroutineScope(Dispatchers.Main).launch {
                  homeViewModel.handleSendFriendRequestClick(recipientEmail) { resultMessage ->
                    CoroutineScope(Dispatchers.Main).launch {
                      SnackbarManager.showSnackbar(resultMessage)
                    }
                  }
                  showAddFriendDialog.value = false
                }
              },
              onDismiss = { showAddFriendDialog.value = false }
            )
          }

          // Dialog box for creating new chat
          if (showAddChatDialog.value) {
            DialogAddGroupChat(
              onConfirm = { groupchatName ->
                CoroutineScope(Dispatchers.Main).launch {
                  homeViewModel.handleCreateGroupChatClick(groupchatName) { resultMessage ->
                    CoroutineScope(Dispatchers.Main).launch {
                      SnackbarManager.showSnackbar(resultMessage)
                    }
                  }
                  showAddChatDialog.value = false
                }
              },
              onDismiss = { showAddChatDialog.value = false }
            )
          }
        }
      }
    }
  ) {
    HomeSearchField(groupchatList = groupchatList, userOptions = userOptions)
  }
}
