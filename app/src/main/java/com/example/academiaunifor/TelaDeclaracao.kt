package com.example.academiaunifor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaDeclaracao(
    concordou: Boolean,
    onConcordouChange: (Boolean) -> Unit,
    onAvancar: () -> Unit,
    onVoltar: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth().height(40.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "‹", color = Branco, fontSize = 34.sp, modifier = Modifier.clickable { onVoltar() })
            Spacer(Modifier.weight(1f))
            Text(text = "2/2", color = Branco, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.width(20.dp))
        }

        Spacer(Modifier.height(16.dp))
        Text(text = "Para prosseguir, concorde com \nnossos termos", color = Branco, fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(Modifier.height(20.dp))
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth().clickable {
                onConcordouChange(!concordou)
            },
            verticalAlignment = Alignment.Top
        ) {
            Checkbox(
                checked = concordou,
                onCheckedChange = {
                    onConcordouChange(it)
                },
                colors = CheckboxDefaults.colors(checkedColor = AzulClaro, uncheckedColor = Branco, checkmarkColor = Fundo)
            )
            Text(
                text = "Declaro que li e compreendi as perguntas acima e respondi com total veracidade. Entendo que, se respondi \"SIM\" a qualquer questão, devo procurar um médico antes de iniciar qualquer atividade física.",
                color = Branco, fontSize = 13.sp, lineHeight = 19.sp,
                modifier = Modifier.weight(1f).padding(top = 12.dp)
            )
        }
        Spacer(Modifier.weight(1f))

        // O botão 'Próximo' só aparece se a pessoa clicar no concordo com os termos
        if (concordou) {
            Botao(texto = "Próximo") {
                onAvancar()
            }
        }
    }
}
