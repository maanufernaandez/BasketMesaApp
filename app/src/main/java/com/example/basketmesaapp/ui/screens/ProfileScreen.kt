package com.example.basketmesaapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onBack: () -> Unit, onLogout: () -> Unit) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val user = auth.currentUser
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current

    var nombre by remember { mutableStateOf("Cargando...") }
    var apellido by remember { mutableStateOf("Cargando...") }
    var rol by remember { mutableStateOf("") }
    var autorizado3Vistas by remember { mutableStateOf(false) }

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var currentVisible by remember { mutableStateOf(false) }
    var newVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }

    // Estados de validación
    val isPasswordValid = newPassword.length >= 8 &&
            newPassword.any { it.isUpperCase() } &&
            newPassword.any { it.isLowerCase() } &&
            newPassword.any { it.isDigit() }

    val passwordError = newPassword.isNotEmpty() && !isPasswordValid
    val confirmError = confirmPassword.isNotEmpty() && confirmPassword != newPassword

    LaunchedEffect(Unit) {
        user?.uid?.let { uid ->
            db.collection("usuarios").document(uid).get().addOnSuccessListener { doc ->
                if (doc != null && doc.exists()) {
                    nombre = doc.getString("nombre") ?: ""
                    apellido = doc.getString("apellido") ?: ""
                    rol = doc.getString("rol") ?: ""
                    autorizado3Vistas = doc.getBoolean("autorizado3Vistas") ?: false
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Datos Personales", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.align(Alignment.Start), color = MaterialTheme.colorScheme.onSurface)

            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellidos") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))

            if (rol == "Oficial de Mesa") {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().clickable { autorizado3Vistas = !autorizado3Vistas }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                        Checkbox(checked = autorizado3Vistas, onCheckedChange = { autorizado3Vistas = it })
                        Text("Autorizado 3 Funciones Vistas", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Button(
                onClick = {
                    user?.uid?.let { uid ->
                        db.collection("usuarios").document(uid).update(
                            mapOf("nombre" to nombre, "apellido" to apellido, "autorizado3Vistas" to autorizado3Vistas)
                        ).addOnSuccessListener { Toast.makeText(context, "Datos guardados", Toast.LENGTH_SHORT).show(); focusManager.clearFocus() }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Guardar Datos") }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            Text("Cambiar Contraseña", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.align(Alignment.Start), color = MaterialTheme.colorScheme.onSurface)

            OutlinedTextField(
                value = currentPassword, onValueChange = { currentPassword = it },
                label = { Text("Contraseña Actual") },
                visualTransformation = if (currentVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = { IconButton(onClick = { currentVisible = !currentVisible }) { Icon(if (currentVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, "Ver") } },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = newPassword, onValueChange = { newPassword = it },
                label = { Text("Nueva Contraseña") },
                isError = passwordError,
                supportingText = { if (passwordError) Text("Mínimo 8 caracteres, 1 mayúscula, 1 minúscula y 1 número.") },
                visualTransformation = if (newVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = { IconButton(onClick = { newVisible = !newVisible }) { Icon(if (newVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, "Ver") } },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = confirmPassword, onValueChange = { confirmPassword = it },
                label = { Text("Repetir Nueva") },
                isError = confirmError,
                supportingText = { if (confirmError) Text("Las contraseñas no coinciden.") },
                visualTransformation = if (confirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = { IconButton(onClick = { confirmVisible = !confirmVisible }) { Icon(if (confirmVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, "Ver") } },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
            )

            Button(
                onClick = {
                    if (passwordError || confirmError) {
                        Toast.makeText(context, "Revisa los errores en pantalla", Toast.LENGTH_SHORT).show()
                    } else if (newPassword == currentPassword) {
                        Toast.makeText(context, "La contraseña es la misma que la actual", Toast.LENGTH_SHORT).show()
                    } else {
                        val credential = EmailAuthProvider.getCredential(user?.email!!, currentPassword)
                        user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                            if (reauthTask.isSuccessful) {
                                user.updatePassword(newPassword).addOnCompleteListener { updateTask ->
                                    if (updateTask.isSuccessful) {
                                        Toast.makeText(context, "Contraseña cambiada", Toast.LENGTH_SHORT).show()
                                        currentPassword = ""; newPassword = ""; confirmPassword = ""
                                        focusManager.clearFocus()
                                    } else {
                                        Toast.makeText(context, "Error: ${updateTask.exception?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Contraseña actual incorrecta", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Cambiar Contraseña") }

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
            ) { Text("Cerrar Sesión", fontWeight = FontWeight.Bold) }
        }
    }
}