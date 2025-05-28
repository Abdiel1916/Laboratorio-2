package com.example.appcalculadora

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Define tus esquemas de color (personaliza los colores según necesites)
            val darkColorScheme = darkColorScheme(
                primary = Color(0xFFBB86FC),
                secondary = Color(0xFF03DAC6),
                tertiary = Color(0xFF3700B3)
            )

            val lightColorScheme = lightColorScheme(
                primary = Color(0xFF6200EE),
                secondary = Color(0xFF03DAC6),
                tertiary = Color(0xFF018786)
            )
            // Aplica el tema dinámico (basado en el modo oscuro del sistema)
            MaterialTheme(
                colorScheme = if (isSystemInDarkTheme()) darkColorScheme else lightColorScheme,
                typography = MaterialTheme.typography,
                content = {
                    LoginScreen()
                }
            )
        }
    }
}

@Composable
fun LoginScreen() {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isButtonEnabled = username.isNotBlank() && password.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Título con color personalizado
        Text(
            text = "Inicio de Sesión",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color(0xFF6200EE) // Color morado
            ),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Campo de usuario
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = {
                Text(
                    "Usuario",
                    color = Color(0xFF757575) // Color gris
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFFAFAFA),
                focusedTextColor = Color.Black,
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(
                    "Contraseña",
                    color = Color(0xFF757575) // Color gris
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFFAFAFA),
                focusedTextColor = Color.Black,
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón con color dinámico
        Button(
            onClick = {
                if (validatePassword(password)) {
                    val intent = Intent(context, MainActivity2::class.java)
                    intent.putExtra("username", username)
                    context.startActivity(intent)
                } else {
                    Toast.makeText(
                        context,
                        "La contraseña debe tener:\n- 8+ caracteres\n- Mayúsculas\n- Números\n- Guión bajo (_)",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = isButtonEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6200EE), // Morado
                disabledContainerColor = Color(0xFFB39DDB) // Morado claro cuando está desactivado
            )
        ) {
            Text(
                "Ingresar",
                color = Color.White,
                style = MaterialTheme.typography.labelLarge
            )
        }

        // Texto de ayuda con color personalizado
        Text(
            text = "La contraseña debe contener:\n- Mínimo 8 caracteres\n- Mayúsculas y minúsculas\n- Números\n- Guión bajo (_)",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF757575), // Gris
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

fun validatePassword(password: String): Boolean {
    return password.length > 8 &&
            password.any { it.isUpperCase() } &&
            password.any { it.isLowerCase() } &&
            password.any { it.isDigit() } &&
            password.contains("_")
}

// Extensión para verificar si el sistema está en modo oscuro
@Composable
fun isSystemInDarkTheme(): Boolean {
    return MaterialTheme.colorScheme.background == darkColorScheme().background
}