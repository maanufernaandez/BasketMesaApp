package com.example.basketmesaapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                OutlinedTextField(value = email, onValueChange = { email = it.replace(" ", "") }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it.replace(" ", "") },
                    label = { Text("Contraseña") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = { IconButton(onClick = { passwordVisible = !passwordVisible }) { Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, "Ver") } },
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
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellidos") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = email, onValueChange = { email = it.replace(" ", "") }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it.replace(" ", "") },
                    label = { Text("Contraseña") },
                    isError = passwordError,
                    supportingText = { if (passwordError) Text("Mínimo 8 caracteres, 1 mayúscula, 1 minúscula y 1 número.") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = { IconButton(onClick = { passwordVisible = !passwordVisible }) { Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, "Ver") } },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it.replace(" ", "") },
                    label = { Text("Repetir Contraseña") },
                    isError = confirmError,
                    supportingText = { if (confirmError) Text("Las contraseñas no coinciden.") },
                    visualTransformation = if (confirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = { IconButton(onClick = { confirmVisible = !confirmVisible }) { Icon(if (confirmVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, "Ver") } },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text("Selecciona tu rol:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.align(Alignment.Start))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    RadioButton(selected = rol == "Oficial de Mesa", onClick = { rol = "Oficial de Mesa" })
                    Text("Oficial de Mesa", modifier = Modifier.clickable { rol = "Oficial de Mesa" })
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(selected = rol == "Árbitro", onClick = { rol = "Árbitro" })
                    Text("Árbitro", modifier = Modifier.clickable { rol = "Árbitro" })
                }

                if (rol == "Oficial de Mesa") {
                    Surface(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth().clickable { autorizado3Vistas = !autorizado3Vistas }) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                            Checkbox(checked = autorizado3Vistas, onCheckedChange = { autorizado3Vistas = it })
                            Text("Autorizado 3 Funciones Vistas", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(onClick = {
                    if (rol.isEmpty()) Toast.makeText(context, "Por favor, selecciona tu rol", Toast.LENGTH_SHORT).show()
                    else if (passwordError || confirmError) Toast.makeText(context, "Revisa los errores en pantalla", Toast.LENGTH_SHORT).show()
                    else {
                        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userMap = mapOf("nombre" to nombre, "apellido" to apellido, "email" to email, "rol" to rol, "autorizado3Vistas" to (if (rol == "Oficial de Mesa") autorizado3Vistas else false))
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
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Introduce tu correo") }, modifier = Modifier.fillMaxWidth())
                Button(onClick = { auth.sendPasswordResetEmail(email).addOnCompleteListener { if (it.isSuccessful) { Toast.makeText(context, "Enviado", Toast.LENGTH_SHORT).show(); currentStep = "LOGIN" } else Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show() } }, modifier = Modifier.fillMaxWidth().height(50.dp)) { Text("Enviar enlace") }
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