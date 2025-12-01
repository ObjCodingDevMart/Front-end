package com.example.devmart.ui.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.devmart.ui.theme.DevWhite

@Composable
fun AddressSearchScreen(
    viewModel: AddressSearchViewModel = hiltViewModel(),
    onSelect: (Address) -> Unit,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // StateFlow 아님 → 바로 value 사용
        OutlinedTextField(
            value = viewModel.keyword,
            onValueChange = { viewModel.search(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("주소 검색") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {

            // StateFlow 아님 → 바로 value 사용
            items(viewModel.results) { item ->

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(DevWhite, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                        .clickable { onSelect(item) }
                ) {
                    Text(item.roadAddress)
                    if (item.jibunAddress.isNotEmpty()) {
                        Text(
                            item.jibunAddress,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

