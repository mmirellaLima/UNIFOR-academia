package com.example.academiaunifor

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
import com.example.academiaunifor.ui.theme.AcademiaUniforTheme

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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AcademiaUniforTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Fundo
                ) {
                    QuestionarioApp()
                }
            }
        }
    }
}

@Composable
fun QuestionarioApp() {
    GerenciadorDeTelas()
}

@Composable
fun FundoDecorativo(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Fundo)
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_fundo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        content()
    }
}

@Composable
fun GerenciadorDeTelas() {
    var tela by remember { mutableIntStateOf(0) }
    var indiceSaude by remember { mutableIntStateOf(0) }

    var respostasSaude by remember { mutableStateOf(List(perguntasSaude.size) { "" }) }
    var concordouDeclaracao by remember { mutableStateOf(false) }

    FundoDecorativo {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (tela) {
                // TELA INTRODUÇÃO
                0 -> TelaIntroducao(onAvancar = { indiceSaude = 0; tela = 1 })

                // TELAS PERGUNTAS 1 A 10 (SAÚDE)
                1 -> TelaSaude(
                    indiceSaude = indiceSaude,
                    respostas = respostasSaude,
                    onResponder = { resp ->
                        respostasSaude = respostasSaude.toMutableList().also { it[indiceSaude] = resp }
                        if (indiceSaude < perguntasSaude.lastIndex) {
                            indiceSaude++
                        } else {
                            tela = 2
                        }
                    },
                    onVoltar = {
                        if (indiceSaude > 0) indiceSaude-- else tela = 0
                    }
                )

                // TELA PERGUNTA 11 (DECLARAÇÃO DE SAÚDE / TERMOS)
                2 -> TelaDeclaracao(
                    concordou = concordouDeclaracao,
                    onConcordouChange = { concordouDeclaracao = it },
                    onAvancar = { tela = 3 },
                    onVoltar = { tela = 1; indiceSaude = perguntasSaude.lastIndex }
                )
            }
        }
    }
}

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

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, backgroundColor = 0xFF191A1F)
@Composable
fun QuestionarioAppPreview() {
    AcademiaUniforTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Fundo) {
            QuestionarioApp()
        }
    }
}
