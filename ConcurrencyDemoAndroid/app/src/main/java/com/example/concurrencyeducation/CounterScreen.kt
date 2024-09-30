package com.example.concurrencyeducation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CounterScreen(
    viewModel: CounterViewModel,
) {
    val counterType = viewModel.counterType.collectAsState().value
    val incrementAction = viewModel.increment.collectAsState().value
    val count = viewModel.count.collectAsState().value
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = count.toString(),
            fontSize = 96.sp
        )
        Spacer(modifier = Modifier.padding(32.dp))
        Button(
            onClick = { incrementAction() },
            modifier = Modifier.height(100.dp)
        ) {
            Text(
                text = "Increment!",
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Column {
            RadioOption(
                selected = counterType == CounterType.SYNCHRONOUS,
                onClick = { viewModel.setCounterType(CounterType.SYNCHRONOUS) },
                text = "Synchronous counter"
            )
            RadioOption(
                selected = counterType == CounterType.UNSAFE,
                onClick = { viewModel.setCounterType(CounterType.UNSAFE) },
                text = "Asynchronous unsafe counter"
            )
            RadioOption(
                selected = counterType == CounterType.ATOMIC,
                onClick = { viewModel.setCounterType(CounterType.ATOMIC) },
                text = "Counter with atomic operations"
            )
            RadioOption(
                selected = counterType == CounterType.MUTEX,
                onClick = { viewModel.setCounterType(CounterType.MUTEX) },
                text = "Counter with mutex"
            )
        }
    }
}

@Composable
private fun RadioOption(
    selected: Boolean,
    onClick: () -> Unit,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(text = text)
    }
}