package com.example.basketmesaapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.basketmesaapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AuthScreen(onAuthSuccess: () -> Unit) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var currentStep by remember { mutableStateOf("LOGIN") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }

    var rol by remember { mutableStateOf("") }
    var autorizado3Vistas by remember { mutableStateOf(false) }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

        Image(
            painter = painterResource(id = R.drawable.icono),
            contentDescription = "Logo de la app",
            modifier = Modifier.size(100.dp).padding(bottom = 16.dp)
        )

        when (currentStep) {
            "LOGIN" -> {
                Text("Iniciar Sesión", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it.replace(" ", "").replace("\n", "").replace("\r", "") },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it.replace(" ", "").replace("\n", "").replace("\r", "") },
                    label = { Text("Contraseña") },
                    visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, "Ver")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(onClick = {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) onAuthSuccess() else Toast.makeText(context, getFirebaseErrorMessage(it.exception), Toast.LENGTH_SHORT).show()
                    }
                }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) { Text("Entrar") }

                TextButton(onClick = { currentStep = "FORGOT" }) { Text("¿Has olvidado tu contraseña?") }
                TextButton(onClick = { currentStep = "REGISTER" }) { Text("¿No tienes cuenta? Regístrate") }
            }

            "REGISTER" -> {
                Text("Crear Cuenta", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it.replace("\n", "").replace("\r", "") },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = apellido,
                    onValueChange = { apellido = it.replace("\n", "").replace("\r", "") },
                    label = { Text("Apellidos") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it.replace(" ", "").replace("\n", "").replace("\r", "") },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it.replace(" ", "").replace("\n", "").replace("\r", "") },
                    label = { Text("Contraseña") },
                    visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = { IconButton(onClick = { passwordVisible = !passwordVisible }) { Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, "Ver") } },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it.replace(" ", "").replace("\n", "").replace("\r", "") },
                    label = { Text("Repetir Contraseña") },
                    visualTransformation = if (confirmPasswordVisible) androidx.compose.ui.text.input.VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = { IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) { Icon(if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, "Ver") } },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text("Selecciona tu rol:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.align(
                    Alignment.Start))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    RadioButton(selected = rol == "Oficial de Mesa", onClick = { rol = "Oficial de Mesa" })
                    Text("Oficial de Mesa", modifier = Modifier.clickable { rol = "Oficial de Mesa" })
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(selected = rol == "Árbitro", onClick = { rol = "Árbitro" })
                    Text("Árbitro", modifier = Modifier.clickable { rol = "Árbitro" })
                }

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
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(onClick = {
                    if (rol.isEmpty()) {
                        Toast.makeText(context, "Por favor, selecciona tu rol", Toast.LENGTH_SHORT).show()
                    } else if (password != confirmPassword) {
                        Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                    } else {
                        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userMap = mapOf(
                                    "nombre" to nombre,
                                    "apellido" to apellido,
                                    "email" to email,
                                    "rol" to rol,
                                    "autorizado3Vistas" to (if (rol == "Oficial de Mesa") autorizado3Vistas else false)
                                )
                                db.collection("usuarios").document(auth.currentUser!!.uid).set(userMap)
                                onAuthSuccess()
                            } else Toast.makeText(context, getFirebaseErrorMessage(task.exception), Toast.LENGTH_SHORT).show()
                        }
                    }
                }, modifier = Modifier.fillMaxWidth()) { Text("Completar Registro") }

                TextButton(onClick = { currentStep = "LOGIN" }) { Text("Volver atrás") }
            }

            "FORGOT" -> {
                Text("Recuperar Contraseña", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it.replace(" ", "").replace("\n", "").replace("\r", "") },
                    label = { Text("Introduce tu correo") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (email.isNotEmpty()) {
                            auth.sendPasswordResetEmail(email).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(context, "Se ha enviado un enlace a tu correo", Toast.LENGTH_LONG).show()
                                    currentStep = "LOGIN"
                                } else {
                                    Toast.makeText(context, "Error: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Introduce un correo válido", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) { Text("Enviar enlace de recuperación") }
                TextButton(onClick = { currentStep = "LOGIN" }) { Text("Volver atrás") }
            }
        }
    }
}

fun getFirebaseErrorMessage(exception: Exception?): String {
    return when (exception) {
        is com.google.firebase.auth.FirebaseAuthUserCollisionException -> "Este correo ya está registrado."
        is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrectos."
        is com.google.firebase.auth.FirebaseAuthWeakPasswordException -> "La contraseña es demasiado débil."
        is com.google.firebase.auth.FirebaseAuthInvalidUserException -> "Usuario no encontrado."
        else -> exception?.localizedMessage ?: "Ha ocurrido un error inesperado."
    }
}