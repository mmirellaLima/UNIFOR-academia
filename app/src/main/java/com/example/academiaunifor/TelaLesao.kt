package com.example.academiaunifor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaLesao(
    temLesao: String,
    qualLesao: String,
    onTemLesaoChange: (String) -> Unit,
    onQualLesaoChange: (String) -> Unit,
    onAvancar: () -> Unit,
    onVoltar: () -> Unit
) {
    var erro by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth().height(40.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "‹", color = Branco, fontSize = 34.sp, modifier = Modifier.clickable { onVoltar() })
            Spacer(Modifier.weight(1f))
            Text(text = "8/12", color = Branco, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.width(20.dp))
        }

        Spacer(Modifier.height(20.dp))
        Text(text = "Já sofreu alguma lesão muscular?", color = Branco, fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            BotaoResposta(texto = "Não", selecionado = temLesao == "Não", modifier = Modifier.weight(1f), claro = true) {
                onTemLesaoChange("Não")
                onQualLesaoChange("")
                erro = false
            }
            BotaoResposta(texto = "Sim", selecionado = temLesao == "Sim", modifier = Modifier.weight(1f)) {
                onTemLesaoChange("Sim")
                erro = false
            }
        }

        if (temLesao == "Sim") {
            Spacer(Modifier.height(20.dp))
            OutlinedTextField(
                value = qualLesao,
                onValueChange = { onQualLesaoChange(it); erro = false },
                label = { Text("Qual lesão?", color = TextoClaro) },
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AzulClaro, unfocusedBorderColor = Branco, focusedTextColor = Branco, unfocusedTextColor = Branco),
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (erro) {
            Spacer(Modifier.height(12.dp))
            CaixaErro("Responda à questão e informe qual lesão caso tenha sofrido.")
        }

        Spacer(Modifier.weight(1f))
        Botao(texto = "Próximo") {
            if (temLesao.isBlank() || (temLesao == "Sim" && qualLesao.isBlank())) {
                erro = true
            } else {
                erro = false
                onAvancar()
            }
        }
    }
}