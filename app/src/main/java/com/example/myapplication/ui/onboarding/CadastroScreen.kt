package com.example.myapplication.ui.onboarding

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.components.AppBackground
import com.example.myapplication.ui.components.AppTextField
import com.example.myapplication.ui.components.LogoHeader
import com.example.myapplication.ui.components.PrimaryButton

@Composable
fun CadastroScreen(vm: OnboardingViewModel, onConcluir: () -> Unit) {
    var nome by rememberSaveable { mutableStateOf(vm.nome) }
    var email by rememberSaveable { mutableStateOf("") }
    var matricula by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var enviado by rememberSaveable { mutableStateOf(false) }

    AppBackground(scroll = true) {
        LogoHeader("Olá, cadastre-se aqui")

        AppTextField(
            label = "Nome Completo",
            value = nome,
            onValueChange = { nome = it },
            placeholder = "Digite seu nome",
            icon = Icons.Default.Person,
            error = enviado && nome.isBlank()
        )
        Spacer(Modifier.height(12.dp))

        AppTextField(
            label = "E-mail",
            value = email,
            onValueChange = { email = it },
            placeholder = "Digite seu e-mail",
            icon = Icons.Default.Email,
            error = enviado && email.isBlank(),
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(12.dp))

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
        Spacer(Modifier.height(24.dp))

        PrimaryButton("Entrar") {
            enviado = true
            if (listOf(nome, email, matricula, senha).none { it.isBlank() }) {
                vm.register(nome.trim(), matricula.trim())
                onConcluir()
            }
        }
    }
}
