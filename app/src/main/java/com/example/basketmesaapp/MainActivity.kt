package com.example.basketmesaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.basketmesaapp.repository.FirestoreRepository
import com.example.basketmesaapp.ui.screens.AuthScreen
import com.example.basketmesaapp.ui.screens.MainScreen
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private val repository = FirestoreRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFFF8A65),
                    onPrimary = Color(0xFF212121),
                    secondary = Color(0xFF64B5F6),
                    background = Color(0xFF0F172A),
                    surface = Color(0xFF1E293B),
                    surfaceVariant = Color(0xFF334155),
                    onSurface = Color(0xFFF8FAFC)
                ),
                shapes = Shapes(
                    small = RoundedCornerShape(8.dp),
                    medium = RoundedCornerShape(16.dp),
                    large = RoundedCornerShape(24.dp)
                )
            ) {
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
                        MainScreen(
                            repository = repository,
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