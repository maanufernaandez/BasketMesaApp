package com.example.basketmesaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.basketmesaapp.ui.screens.AuthScreen
import com.example.basketmesaapp.ui.screens.MainScreen
import com.example.basketmesaapp.ui.theme.BasketMesaAppTheme
import com.example.basketmesaapp.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasketMesaAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentUser by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

                    if (currentUser == null) {
                        AuthScreen(
                            onAuthSuccess = { currentUser = FirebaseAuth.getInstance().currentUser }
                        )
                    } else {
                        val viewModel: MainViewModel = viewModel()
                        MainScreen(
                            viewModel = viewModel,
                            onLogout = {
                                FirebaseAuth.getInstance().signOut()
                                currentUser = null
                            }
                        )
                    }
                }
            }
        }
    }
}