package com.example.myapplication.ui.saude

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

val Fundo = Color(0xFF191A1F)
val Azul = Color(0xFF142191)      // Cor dos botões "Sim" e "Próximo" (142191)
val AzulClaro = Color(0xFF80B5FF) // Cor do círculo do ícone do coração
val Branco = Color.White          // Cor do coração (branco)
val TextoClaro = Color(0xFFE4F4FC)// Cor dos botões "Não" (E4F4FC)
val ErroCor = Color(0xFFB3261E)

// Lista de perguntas de saúde (RF05.6 a RF05.15 - Perguntas 1 a 10)
val perguntasSaude = listOf(
    "Algum médico já lhe disse que você possui alguma condição cardíaca e recomendou que você só fizesse atividade física sob supervisão médica?", // RF05.6
    "Você sente dor no peito ao realizar atividades físicas?", // RF05.7
    "Nos últimos meses, você teve dor no peito mesmo sem realizar esforço físico?", // RF05.8
    "Você tem episódios frequentes de tontura ou perda de equilíbrio?", // RF05.9
    "Você tem algum problema ósseo ou articular que pode ser agravado pela prática de exercícios físicos?", // RF05.10
    "Algum médico já lhe disse que você tem pressão arterial elevada ou algum problema circulatório?", // RF05.11
    "Você tem ou já teve diabetes?", // RF05.12
    "Algum profissional de saúde já indicou que você deve evitar exercícios físicos por algum motivo de saúde?", // RF05.13
    "Você esteve gestante ou teve um bebê nos últimos seis meses?", // RF05.14
    "Você faz uso de alguma medicação que pode afetar sua resposta ao exercício físico?" // RF05.15
)

// Componentes visuais auxiliares reutilizáveis

@Composable
fun Botao(texto: String, modifier: Modifier = Modifier, claro: Boolean = false, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (claro) TextoClaro else Azul,
            contentColor = if (claro) Fundo else Branco
        )
    ) {
        Text(text = texto, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun BotaoResposta(
    texto: String,
    selecionado: Boolean,
    modifier: Modifier = Modifier,
    claro: Boolean = false,
    icon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = when {
                selecionado -> AzulClaro
                claro -> TextoClaro
                else -> Azul
            },
            contentColor = when {
                selecionado -> Fundo
                claro -> Fundo
                else -> Branco
            }
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                icon()
                Spacer(Modifier.width(6.dp))
            }
            Text(text = texto, fontWeight = FontWeight.Bold, fontSize = 14.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun CaixaIcone() {
    Box(
        modifier = Modifier
            .size(56.dp)
            .background(AzulClaro, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = Branco,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
fun CaixaErro(mensagem: String) {
    Text(
        text = mensagem,
        color = Branco,
        fontSize = 13.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().background(ErroCor, RoundedCornerShape(8.dp)).padding(12.dp)
    )
}

@Composable
fun Progresso(porcentagem: Int, progresso: Float) {
    Column(Modifier.fillMaxWidth()) {
        Text(text = "$porcentagem%", color = Branco, fontSize = 12.sp, textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(5.dp))
        Box(modifier = Modifier.fillMaxWidth().height(6.dp).background(Branco, RoundedCornerShape(50))) {
            Box(modifier = Modifier.fillMaxWidth(progresso.coerceIn(0f, 1f)).fillMaxHeight().background(Azul, RoundedCornerShape(50)))
        }
    }
}
