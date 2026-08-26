package com.example.basketmesaapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
    var confirmVisible by remember { mutableStateOf(false) }

    val isPasswordValid = password.length >= 8 && password.any { it.isUpperCase() } && password.any { it.isLowerCase() } && password.any { it.isDigit() }
    val passwordError = password.isNotEmpty() && !isPasswordValid
    val confirmError = confirmPassword.isNotEmpty() && confirmPassword != password

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(id = R.drawable.icono),
            contentDescription = "Logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Contenedor que agrupa los campos con una separación estricta de 8.dp
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (currentStep) {
                "LOGIN" -> {
                    Text("Iniciar Sesión", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(bottom = 8.dp))
                    OutlinedTextField(value = email, onValueChange = { email = it.replace(" ", "") }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it.replace(" ", "") },
                        label = { Text("Contraseña") },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = { IconButton(onClick = { passwordVisible = !passwordVisible }) { Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null) } },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                                if (it.isSuccessful) onAuthSuccess() else Toast.makeText(context, getFirebaseErrorMessage(it.exception), Toast.LENGTH_LONG).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Entrar", fontSize = 16.sp, fontWeight = FontWeight.Bold) }

                    TextButton(onClick = { currentStep = "FORGOT" }) { Text("¿Has olvidado tu contraseña?") }
                    TextButton(onClick = { currentStep = "REGISTER" }) { Text("¿No tienes cuenta? Regístrate") }
                }

                "REGISTER" -> {
                    Text("Crear Cuenta", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(bottom = 4.dp))

                    OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                    OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellidos") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                    OutlinedTextField(value = email, onValueChange = { email = it.replace(" ", "") }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it.replace(" ", "") },
                        label = { Text("Contraseña") },
                        isError = passwordError,
                        // Solución al espacio extra: es null si no hay error
                        supportingText = if (passwordError) { { Text("Mínimo 8 caracteres, 1 mayús, 1 minús, 1 número", color = Color.Red) } } else null,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = { IconButton(onClick = { passwordVisible = !passwordVisible }) { Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null) } },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it.replace(" ", "") },
                        label = { Text("Repetir Contraseña") },
                        isError = confirmError,
                        // Solución al espacio extra: es null si no hay error
                        supportingText = if (confirmError) { { Text("Las contraseñas no coinciden", color = Color.Red) } } else null,
                        visualTransformation = if (confirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = { IconButton(onClick = { confirmVisible = !confirmVisible }) { Icon(if (confirmVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null) } },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Bloque de selección de rol
                    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Selecciona tu rol:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            RadioButton(selected = rol == "Oficial de Mesa", onClick = { rol = "Oficial de Mesa" })
                            Text("Oficial de Mesa", modifier = Modifier.clickable { rol = "Oficial de Mesa" })
                            Spacer(modifier = Modifier.width(16.dp))
                            RadioButton(selected = rol == "Árbitro", onClick = { rol = "Árbitro" })
                            Text("Árbitro", modifier = Modifier.clickable { rol = "Árbitro" })
                        }
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
                    }

                    // Botones modernizados y reducidos
                    Button(
                        onClick = {
                            if (rol.isEmpty()) Toast.makeText(context, "Selecciona un rol", Toast.LENGTH_SHORT).show()
                            else if (passwordError || confirmError) Toast.makeText(context, "Revisa los errores", Toast.LENGTH_SHORT).show()
                            else {
                                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val userMap = mapOf("nombre" to nombre, "apellido" to apellido, "email" to email, "rol" to rol, "autorizado3Vistas" to (if (rol == "Oficial de Mesa") autorizado3Vistas else false))
                                        db.collection("usuarios").document(auth.currentUser!!.uid).set(userMap)
                                        onAuthSuccess()
                                    } else Toast.makeText(context, getFirebaseErrorMessage(task.exception), Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Registrarse", fontSize = 16.sp, fontWeight = FontWeight.Bold) }

                    OutlinedButton(
                        onClick = { currentStep = "LOGIN" },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) { Text("Volver atrás", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) }
                }

                "FORGOT" -> {
                    Text("Recuperar Contraseña", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(bottom = 8.dp))
                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Introduce tu correo") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { auth.sendPasswordResetEmail(email).addOnCompleteListener { if (it.isSuccessful) currentStep = "LOGIN" } },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Enviar enlace", fontSize = 16.sp, fontWeight = FontWeight.Bold) }

                    OutlinedButton(
                        onClick = { currentStep = "LOGIN" },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) { Text("Volver atrás", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

fun getFirebaseErrorMessage(exception: Exception?): String {
    return when (exception) {
        is com.google.firebase.auth.FirebaseAuthUserCollisionException -> "Este correo ya está registrado."
        is com.google.firebase.auth.FirebaseAuthWeakPasswordException -> "La contraseña es demasiado débil."
        is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrectos."
        is com.google.firebase.auth.FirebaseAuthInvalidUserException -> "Usuario no encontrado."
        else -> exception?.localizedMessage ?: "Ha ocurrido un error inesperado."
    }
}