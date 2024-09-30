package com.example.concurrencyeducation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.concurrencyeducation.ui.theme.ConcurrencyEducationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val flakyCounterViewModel = CounterViewModel()

        super.onCreate(savedInstanceState)
        setContent {
            ConcurrencyEducationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CounterScreen(
                        viewModel = flakyCounterViewModel
                    )
                }
            }
        }
    }
}


