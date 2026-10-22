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
fun TelaCirurgia(
    teveCirurgia: String,
    qualCirurgia: String,
    onTeveCirurgiaChange: (String) -> Unit,
    onQualCirurgiaChange: (String) -> Unit,
    onAvancar: () -> Unit,
    onVoltar: () -> Unit
) {
    var erro by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth().height(40.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "‹", color = Branco, fontSize = 34.sp, modifier = Modifier.clickable { onVoltar() })
            Spacer(Modifier.weight(1f))
            Text(text = "11/12", color = Branco, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.width(20.dp))
        }

        Spacer(Modifier.height(20.dp))
        Text(text = "Realizou alguma cirurgia nos últimos meses?", color = Branco, fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            BotaoResposta(texto = "Não", selecionado = teveCirurgia == "Não", modifier = Modifier.weight(1f), claro = true) {
                onTeveCirurgiaChange("Não")
                onQualCirurgiaChange("")
                erro = false
            }
            BotaoResposta(texto = "Sim", selecionado = teveCirurgia == "Sim", modifier = Modifier.weight(1f)) {
                onTeveCirurgiaChange("Sim")
                erro = false
            }
        }

        if (teveCirurgia == "Sim") {
            Spacer(Modifier.height(20.dp))
            OutlinedTextField(
                value = qualCirurgia,
                onValueChange = { onQualCirurgiaChange(it); erro = false },
                label = { Text("Qual cirurgia?", color = TextoClaro) },
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AzulClaro, unfocusedBorderColor = Branco, focusedTextColor = Branco, unfocusedTextColor = Branco),
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (erro) {
            Spacer(Modifier.height(12.dp))
            CaixaErro("Responda à questão e informe qual cirurgia caso tenha realizado.")
        }

        Spacer(Modifier.weight(1f))
        Botao(texto = "Concluir") {
            if (teveCirurgia.isBlank() || (teveCirurgia == "Sim" && qualCirurgia.isBlank())) {
                erro = true
            } else {
                erro = false
                onAvancar()
            }
        }
    }
}