package com.example.appcalculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username = intent.getStringExtra("username") ?: ""
        setContent {
            AppTheme {  // Cambia MyApplicationComposeTheme por AppTheme
                CalculatorScreen(username) {
                    finish()
                }
            }
        }
    }
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(),  // O usa darkColorScheme() para modo oscuro
        content = content
    )
}

@Composable
fun CalculatorScreen(username: String, onExit: () -> Unit) {
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    // Paleta de colores
    val primaryColor = Color(0xFF6200EE)
    val secondaryColor = Color(0xFF03DAC6)
    val errorColor = Color(0xFFB00020)
    val surfaceColor = Color(0xFFFFFBFE)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Título
        Text(
            text = "Calculadora Científica",
            style = MaterialTheme.typography.headlineSmall.copy(color = primaryColor),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Usuario
        Text(
            text = "Usuario: $username",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF757575)),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Campos de entrada
        OutlinedTextField(
            value = num1,
            onValueChange = { num1 = it },
            label = { Text("Primer número", color = primaryColor.copy(alpha = 0.6f)) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFFAFAFA),
                focusedTextColor = Color.Black,
                focusedLabelColor = primaryColor,
                focusedIndicatorColor = primaryColor
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = num2,
            onValueChange = { num2 = it },
            label = { Text("Segundo número", color = primaryColor.copy(alpha = 0.6f)) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFFAFAFA),
                focusedTextColor = Color.Black,
                focusedLabelColor = primaryColor,
                focusedIndicatorColor = primaryColor
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botones de operaciones
        OperationButtonsRow(
            listOf("+" to primaryColor, "-" to primaryColor),
            num1, num2, ::operate
        ) { newResult -> result = newResult }

        Spacer(modifier = Modifier.height(12.dp))

        OperationButtonsRow(
            listOf("×" to secondaryColor, "÷" to secondaryColor),
            num1, num2, ::operate
        ) { newResult -> result = newResult }

        Spacer(modifier = Modifier.height(24.dp))

        // Resultado
        OutlinedTextField(
            value = result,
            onValueChange = {},
            label = { Text("Resultado") },
            readOnly = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFFAFAFA),
                focusedTextColor = if (result.startsWith("Error")) errorColor else primaryColor,
                unfocusedTextColor = if (result.startsWith("Error")) errorColor else primaryColor,
                focusedLabelColor = primaryColor
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón de salida
        Button(
            onClick = onExit,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = errorColor,
                contentColor = Color.White
            )
        ) {
            Text("Cerrar Sesión")
        }
    }
}

@Composable
fun OperationButtonsRow(
    operations: List<Pair<String, Color>>,
    num1: String,
    num2: String,
    operationFunc: (String, String, String) -> String,
    onResult: (String) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        operations.forEach { (symbol, color) ->
            Button(
                onClick = { onResult(operationFunc(num1, num2, symbol)) },
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = color,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = symbol,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}

fun operate(n1: String, n2: String, op: String): String {
    val cleanOp = when(op) {
        "×" -> "*"
        "÷" -> "/"
        else -> op
    }

    return try {
        val a = n1.toDouble()
        val b = n2.toDouble()
        when(cleanOp) {
            "+" -> "%.2f".format(a + b)
            "-" -> "%.2f".format(a - b)
            "*" -> "%.2f".format(a * b)
            "/" -> if (b != 0.0) "%.2f".format(a / b) else "Error: División por cero"
            else -> "Operación inválida"
        }
    } catch(e: Exception) {
        "Error: Entrada inválida"
    }
}