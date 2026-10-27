package com.example.myapplication.ui.saude

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaSaude(
    indiceSaude: Int,
    respostas: List<String>,
    onResponder: (String) -> Unit,
    onVoltar: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "‹",
                color = Branco,
                fontSize = 34.sp,
                modifier = Modifier.clickable { onVoltar() }
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "1/2",
                color = Branco,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.width(34.sp.value.dp))
        }

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CaixaIcone()
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = perguntasSaude[indiceSaude],
            color = Branco,
            fontSize = 17.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
        )

        // Spacing to position buttons slightly below the middle of the screen
        Spacer(Modifier.height(36.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BotaoResposta(
                texto = "Não",
                selecionado = respostas[indiceSaude] == "Não",
                modifier = Modifier.weight(1f),
                claro = true
            ) { onResponder("Não") }

            BotaoResposta(
                texto = "Sim",
                selecionado = respostas[indiceSaude] == "Sim",
                modifier = Modifier.weight(1f),
                icon = {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = if (respostas[indiceSaude] == "Sim") Fundo else Branco
                    )
                }
            ) { onResponder("Sim") }
        }

        Spacer(Modifier.weight(1f))

        val porcentagem = (indiceSaude + 1) * 5
        val progressoFloat = (indiceSaude + 1) * 0.05f
        Progresso(porcentagem = porcentagem, progresso = progressoFloat)

        Spacer(Modifier.height(10.dp))
    }
}
