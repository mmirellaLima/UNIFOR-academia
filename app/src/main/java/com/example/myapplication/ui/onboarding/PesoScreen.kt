package com.example.myapplication.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.components.AppBackground
import com.example.myapplication.ui.components.PrimaryButton
import com.example.myapplication.ui.components.SecondaryButton

@Composable
fun PesoScreen(vm: OnboardingViewModel, onVoltar: () -> Unit, onAvancar: () -> Unit) {
    val nome = if (vm.nome.isBlank()) "" else ", ${vm.nome.substringBefore(" ")}"

    AppBackground(scroll = androidx.compose.ui.platform.LocalConfiguration.current.screenHeightDp < 600) {
        IconButton(onClick = onVoltar) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = Color.White
            )
        }

        Text(
            text = "E qual é o seu peso atual$nome?",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.weight(1f))

        WheelPicker(
            values = (30..250).toList(),
            initialValue = vm.peso,
            onSelected = { vm.peso = it },
            horizontal = true,
            selectedLabel = { "$it kg" },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.weight(1f))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SecondaryButton("Voltar", Modifier.weight(1f), onClick = onVoltar)
            PrimaryButton("Avançar", Modifier.weight(1f), onClick = onAvancar)
        }
    }
}
