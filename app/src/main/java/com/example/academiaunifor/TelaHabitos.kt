package com.example.academiaunifor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaHabitos(
    fuma: String,
    bebe: String,
    onFumaChange: (String) -> Unit,
    onBebeChange: (String) -> Unit,
    onAvancar: () -> Unit,
    onVoltar: () -> Unit
) {
    var erro by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth().height(40.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "‹", color = Branco, fontSize = 34.sp, modifier = Modifier.clickable { onVoltar() })
            Spacer(Modifier.weight(1f))
            Text(text = "10/12", color = Branco, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.width(20.dp))
        }

        Spacer(Modifier.height(16.dp))
        Text(text = "Hábitos de Vida", color = Branco, fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))

        Text(text = "Você fuma?", color = Branco, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        Spacer(Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            BotaoResposta(texto = "Não", selecionado = fuma == "Não", modifier = Modifier.weight(1f), claro = true) {
                onFumaChange("Não"); erro = false
            }
            BotaoResposta(texto = "Sim", selecionado = fuma == "Sim", modifier = Modifier.weight(1f)) {
                onFumaChange("Sim"); erro = false
            }
        }

        Spacer(Modifier.height(24.dp))
        Text(text = "Ingere bebida alcoólica?", color = Branco, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        Spacer(Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            BotaoResposta(texto = "Não", selecionado = bebe == "Não", modifier = Modifier.weight(1f), claro = true) {
                onBebeChange("Não"); erro = false
            }
            BotaoResposta(texto = "Sim", selecionado = bebe == "Sim", modifier = Modifier.weight(1f)) {
                onBebeChange("Sim"); erro = false
            }
        }

        if (erro) {
            Spacer(Modifier.height(12.dp))
            CaixaErro("Responda a ambas as perguntas sobre fumo e álcool.")
        }

        Spacer(Modifier.weight(1f))
        Botao(texto = "Próximo") {
            if (fuma.isBlank() || bebe.isBlank()) {
                erro = true
            } else {
                erro = false
                onAvancar()
            }
        }
    }
}