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
fun TelaTempoSemExercicios(
    selecionado: String,
    onSelecionado: (String) -> Unit,
    onAvancar: () -> Unit,
    onVoltar: () -> Unit
) {
    var erro by remember { mutableStateOf(false) }
    val tempos = listOf("Nunca pratiquei", "Menos de 6 meses", "De 6 meses a 1 ano", "Mais de 1 ano", "Pratico atualmente")

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth().height(40.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "‹", color = Branco, fontSize = 34.sp, modifier = Modifier.clickable { onVoltar() })
            Spacer(Modifier.weight(1f))
            Text(text = "6/12", color = Branco, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.width(20.dp))
        }

        Spacer(Modifier.height(20.dp))
        Text(text = "Há quanto tempo não pratica exercícios?", color = Branco, fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(Modifier.height(20.dp))

        tempos.forEach { tempo ->
            BotaoResposta(
                texto = tempo,
                selecionado = selecionado == tempo,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                onSelecionado(tempo)
                erro = false
            }
        }

        if (erro) {
            Spacer(Modifier.height(12.dp))
            CaixaErro("Informe há quanto tempo não pratica exercícios.")
        }

        Spacer(Modifier.weight(1f))
        Botao(texto = "Próximo") {
            if (selecionado.isBlank()) {
                erro = true
            } else {
                erro = false
                onAvancar()
            }
        }
    }
}