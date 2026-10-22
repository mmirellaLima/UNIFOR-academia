package com.example.myapplication.ui.onboarding

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.components.AppBackground
import com.example.myapplication.ui.components.AppTextField
import com.example.myapplication.ui.components.LogoHeader
import com.example.myapplication.ui.components.PrimaryButton

@Composable
fun LoginScreen(onEntrar: () -> Unit, onEsqueceuSenha: () -> Unit) {
    var matricula by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var enviado by rememberSaveable { mutableStateOf(false) }

    AppBackground(scroll = true) {
        LogoHeader("Olá, acesse sua conta")

        AppTextField(
            label = "Matrícula",
            value = matricula,
            onValueChange = { matricula = it },
            placeholder = "Digite sua matrícula",
            icon = Icons.Default.Person,
            error = enviado && matricula.isBlank(),
            keyboardType = KeyboardType.Number
        )
        Spacer(Modifier.height(12.dp))

        AppTextField(
            label = "Senha",
            value = senha,
            onValueChange = { senha = it },
            placeholder = "Digite sua senha",
            icon = Icons.Default.Lock,
            isPassword = true,
            error = enviado && senha.isBlank()
        )

        TextButton(onClick = onEsqueceuSenha) {
            Text(
                text = "Esqueceu sua senha?",
                color = Color.White,
                fontSize = 12.sp,
                textDecoration = TextDecoration.Underline
            )
        }
        Spacer(Modifier.height(24.dp))

        PrimaryButton("Entrar") {
            enviado = true
            if (matricula.isNotBlank() && senha.isNotBlank()) {
                onEntrar()
            }
        }
    }
}