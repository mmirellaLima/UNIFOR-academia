package com.example.academiaunifor

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.academiaunifor.ui.theme.AcademiaUniforTheme

@Composable
fun TelaIntroducao(
    onAvancar: () -> Unit,
    onVoltar: () -> Unit = {}
) {
    val context = LocalContext.current
    val actionVoltar: () -> Unit = {
        if (onVoltar != {}) {
            onVoltar()
        } else {
            (context as? Activity)?.finish()
        }
    }

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
                modifier = Modifier.clickable { actionVoltar() }
            )
            Spacer(Modifier.weight(1f))
        }

        Spacer(Modifier.height(30.dp))

        Text(
            text = buildAnnotatedString {
                append("Antes de começar, " +
                        "precisamos conhecer " +
                        "um pouco mais sobre ")
                withStyle(style = SpanStyle(color = Azul)) {
                    append("você")
                }
                append(".")
            },
            color = Branco,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 32.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(150.dp))

        Text(
            text = "Responda algumas perguntas rápidas sobre sua saúde e rotina. Suas respostas são importantes para ajudarmos a tornar seus treinos mais seguros e adequados para você.",
            color = TextoClaro,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
        )

        Spacer(Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BotaoResposta(
                texto = "Voltar",
                selecionado = false,
                modifier = Modifier.weight(1f),
                claro = true
            ) {
                actionVoltar()
            }

            BotaoResposta(
                texto = "Avançar",
                selecionado = false,
                modifier = Modifier.weight(1f),
                claro = false
            ) {
                onAvancar()
            }
        }
        Spacer(Modifier.height(20.dp))
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, backgroundColor = 0xFF191A1F)
@Composable
fun TelaIntroducaoPreview() {
    AcademiaUniforTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Fundo) {
            TelaIntroducao(onAvancar = {})
        }
    }
}