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
fun TelaDorArticular(
    temDor: String,
    ondeDor: String,
    onTemDorChange: (String) -> Unit,
    onOndeDorChange: (String) -> Unit,
    onAvancar: () -> Unit,
    onVoltar: () -> Unit
) {
    var erro by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth().height(40.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "‹", color = Branco, fontSize = 34.sp, modifier = Modifier.clickable { onVoltar() })
            Spacer(Modifier.weight(1f))
            Text(text = "9/12", color = Branco, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.width(20.dp))
        }

        Spacer(Modifier.height(20.dp))
        Text(text = "Apresenta alguma dor articular ou muscular?", color = Branco, fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            BotaoResposta(texto = "Não", selecionado = temDor == "Não", modifier = Modifier.weight(1f), claro = true) {
                onTemDorChange("Não")
                onOndeDorChange("")
                erro = false
            }
            BotaoResposta(texto = "Sim", selecionado = temDor == "Sim", modifier = Modifier.weight(1f)) {
                onTemDorChange("Sim")
                erro = false
            }
        }

        if (temDor == "Sim") {
            Spacer(Modifier.height(20.dp))
            OutlinedTextField(
                value = ondeDor,
                onValueChange = { onOndeDorChange(it); erro = false },
                label = { Text("Onde sente dor?", color = TextoClaro) },
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AzulClaro, unfocusedBorderColor = Branco, focusedTextColor = Branco, unfocusedTextColor = Branco),
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (erro) {
            Spacer(Modifier.height(12.dp))
            CaixaErro("Responda à questão e informe onde apresenta dor.")
        }

        Spacer(Modifier.weight(1f))
        Botao(texto = "Próximo") {
            if (temDor.isBlank() || (temDor == "Sim" && ondeDor.isBlank())) {
                erro = true
            } else {
                erro = false
                onAvancar()
            }
        }
    }
}