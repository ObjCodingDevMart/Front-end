package com.example.devmart.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(vm: LoginViewModel = hiltViewModel()) {
    var email by remember { mutableStateOf("") }
    var pw by remember { mutableStateOf("") }
    var err by remember { mutableStateOf<String?>(null) }

    Scaffold(topBar = { TopAppBar(title = { Text("로그인") }) }) { pad ->
        Column(Modifier.padding(pad).padding(16.dp)) {
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, singleLine = true)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = pw, onValueChange = { pw = it }, label = { Text("Password") }, singleLine = true)
            Spacer(Modifier.height(16.dp))
            Button(onClick = { vm.login(email, pw) { err = it } }, enabled = email.isNotBlank() && pw.isNotBlank()) {
                Text("로그인")
            }
            err?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }
    }
}
