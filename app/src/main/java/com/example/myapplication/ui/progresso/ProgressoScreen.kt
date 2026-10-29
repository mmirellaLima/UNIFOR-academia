package com.example.myapplication.ui.progresso

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.ui.components.AppColors
import androidx.annotation.DrawableRes
import androidx.compose.ui.draw.clip

private val AzulClaro = Color(0xFF85B2FF)

private data class Resultados(
    val treinos: String, val varTreinos: String,
    val peso: String, val varPeso: String,
    val kcal: String, val varKcal: String,
    val massa: String, val varMassa: String,
    val gordura: String, val varGordura: String,
    val pesos: List<Float>, val datas: List<String>
)

private val periodos = listOf("7 dias", "1 mês", "3 mês", "6 mês")

private val dados = mapOf(
    0 to Resultados("6", "+ 2 em relação ao período anterior", "78 kg", "- 0,5 kg em relação ao início", "3.120 kcal", "+ 1 em relação ao período anterior",
        "36,5 kg", "+ 0,2 kg", "14,2%", "- 0,1 kg", listOf(78.5f, 78.3f, 78f), listOf("16/10", "19/10", "22/10")),
    1 to Resultados("12", "+ 4 em relação ao período anterior", "78 kg", "- 1 kg em relação ao início", "6.240 kcal", "+ 3 em relação ao período anterior",
        "36,5 kg", "+ 0,5 kg", "14,2%", "- 0,4 kg", listOf(79f, 78.6f, 78.3f, 78f), listOf("22/09", "01/10", "10/10", "22/10")),
    2 to Resultados("24", "+ 8 em relação ao período anterior", "78 kg", "- 3 kg em relação ao início do período", "12.480 kcal", "+ 8 em relação ao período anterior",
        "36,5 kg", "+ 1,8 kg", "14,2%", "- 1,8 kg", listOf(90f, 88.5f, 79f, 76f, 75f, 75f), listOf("20/09", "20/10", "20/11")),
    3 to Resultados("48", "+ 10 em relação ao período anterior", "78 kg", "- 6 kg em relação ao início", "24.960 kcal", "+ 12 em relação ao período anterior",
        "36,5 kg", "+ 3 kg", "14,2%", "- 3 kg", listOf(84f, 82f, 80f, 79f, 78f), listOf("20/04", "20/06", "20/08", "22/10"))
)

@Composable
fun ProgressoScreen(
    onVoltar: () -> Unit = {},
    onInicio: () -> Unit = {},
    onTreinos: () -> Unit = {},
    onPerfil: () -> Unit = {},
    onChat: () -> Unit = {},
    onExercicio: (String) -> Unit = {}
) {
    var periodo by rememberSaveable { mutableIntStateOf(2) }
    val d = dados.getValue(periodo)

    Box(Modifier.fillMaxSize().background(AppColors.Background)) {
        Image(
            painter = painterResource(R.drawable.bg_shapes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(Modifier.fillMaxSize().statusBarsPadding()) {
            Column(
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp)
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = Color.White, modifier = Modifier.clickable { onVoltar() })
                    Icon(Icons.Default.AccountCircle, "Perfil", tint = Color.LightGray, modifier = Modifier.size(36.dp).clickable { onPerfil() })
                }
                Text("Meus Resultados", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Text("Acompanhe seus resultados e veja o quanto você evoluiu.", color = Color.White, fontSize = 12.sp)

                Spacer(Modifier.height(20.dp))
                Titulo("Histórico")
                Seletor(periodo) { periodo = it }
                Spacer(Modifier.height(12.dp))

                Row(Modifier.fillMaxWidth().cartao().padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Indicador(Icons.Default.FitnessCenter, "Treinos realizados", d.treinos, d.varTreinos, Modifier.weight(1f))
                    Indicador(Icons.Default.MonitorWeight, "Peso atual", d.peso, d.varPeso, Modifier.weight(1f))
                    Indicador(Icons.Default.LocalFireDepartment, "Calorias gastas", d.kcal, d.varKcal, Modifier.weight(1f))
                }

                Spacer(Modifier.height(20.dp))
                Titulo("Evolução do peso")
                Column(Modifier.fillMaxWidth().cartao().padding(12.dp)) {
                    GraficoPeso(d.pesos)
                    Spacer(Modifier.height(4.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        d.datas.forEach { Text(it, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                    }
                }

                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Metrica(Icons.Default.FitnessCenter, "Massa muscular", d.massa, d.varMassa, Modifier.weight(1f))
                    Metrica(Icons.Default.Percent, "Percentual de gordura", d.gordura, d.varGordura, Modifier.weight(1f))
                }

                Spacer(Modifier.height(20.dp))
                Titulo("Treinos anteriores")
                TreinoAnterior("Supino Máquina", R.drawable.ex_supino_maquina) { onExercicio("Supino Máquina") }
                Spacer(Modifier.height(12.dp))
                TreinoAnterior("Rosca bíceps com halteres", R.drawable.ex_rosca_biceps) { onExercicio("Rosca bíceps com halteres") }
                Spacer(Modifier.height(12.dp))
                TreinoAnterior("Desenvolvimento de om...", R.drawable.ex_desenvolvimento_ombros) { onExercicio("Desenvolvimento de ombros") }
                Spacer(Modifier.height(12.dp))
            }

            BarraInferior(onInicio = onInicio, onTreinos = onTreinos, onPerfil = onPerfil, onChat = onChat)
        }
    }
}

private fun Modifier.cartao() = this
    .background(AppColors.Background, RoundedCornerShape(16.dp))
    .border(1.dp, AppColors.Primary, RoundedCornerShape(16.dp))

@Composable
private fun Titulo(texto: String) {
    Text(texto, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun Seletor(selecionado: Int, onSelect: (Int) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(32.dp)
            .background(AppColors.Background, RoundedCornerShape(50))
            .border(1.dp, AppColors.Primary, RoundedCornerShape(50))
    ) {
        periodos.forEachIndexed { i, nome ->
            Box(
                Modifier.weight(1f).fillMaxHeight()
                    .background(if (i == selecionado) AppColors.Primary else Color.Transparent, RoundedCornerShape(50))
                    .semantics { selected = i == selecionado }
                    .clickable { onSelect(i) },
                contentAlignment = Alignment.Center
            ) {
                Text(nome, color = if (i == selecionado) Color.White else Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun Indicador(icone: ImageVector, rotulo: String, valor: String, variacao: String, modifier: Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icone, null, tint = Color.White, modifier = Modifier.size(22.dp))
        Text(rotulo, color = Color.White, fontSize = 10.sp, textAlign = TextAlign.Center)
        Text(valor, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(variacao, color = AzulClaro, fontSize = 8.sp, textAlign = TextAlign.Center, lineHeight = 10.sp)
    }
}

@Composable
private fun Metrica(icone: ImageVector, rotulo: String, valor: String, variacao: String, modifier: Modifier) {
    Column(modifier.cartao().padding(12.dp)) {
        Icon(icone, null, tint = Color.White, modifier = Modifier.size(26.dp))
        Spacer(Modifier.height(4.dp))
        Text(rotulo, color = Color.White, fontSize = 10.sp)
        Text(valor, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(variacao, color = AzulClaro, fontSize = 9.sp)
    }
}

@Composable
private fun GraficoPeso(pontos: List<Float>) {
    val min = pontos.min() - 2f
    val max = pontos.max() + 2f
    Row(Modifier.fillMaxWidth().height(150.dp)) {
        Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
            Text("%.0f".format(max), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text("%.0f".format((max + min) / 2), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text("%.0f".format(min), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(8.dp))
        Canvas(Modifier.fillMaxSize()) {
            val passo = if (pontos.size > 1) size.width / (pontos.size - 1) else size.width
            fun y(v: Float) = size.height - (v - min) / (max - min) * size.height
            drawLine(Color.White, Offset(0f, 0f), Offset(0f, size.height), 2f)
            drawLine(Color.White, Offset(0f, size.height), Offset(size.width, size.height), 2f)
            for (i in 0 until pontos.size - 1) {
                drawLine(AppColors.Primary, Offset(i * passo, y(pontos[i])), Offset((i + 1) * passo, y(pontos[i + 1])), 5f, StrokeCap.Round)
            }
        }
    }
}

@Composable
private fun TreinoAnterior(nome: String, @DrawableRes imagem: Int, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().cartao().clickable(onClick = onClick).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(imagem),
            contentDescription = nome,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White),
            contentScale = ContentScale.Fit
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(nome, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(Modifier.height(4.dp))
            Text("4 sets  >", color = Color.White, fontSize = 10.sp,
                modifier = Modifier.background(AppColors.Primary, RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 2.dp))
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.White, modifier = Modifier.size(28.dp))
    }
}

@Composable
private fun BarraInferior(onInicio: () -> Unit, onTreinos: () -> Unit, onPerfil: () -> Unit, onChat: () -> Unit) {
    androidx.compose.ui.viewinterop.AndroidView(
        modifier = Modifier.fillMaxWidth().navigationBarsPadding(),
        factory = { context ->
            android.view.LayoutInflater.from(context).inflate(R.layout.flow_bottom_navigation, null) as android.view.ViewGroup
        },
        update = { bar ->
            com.example.myapplication.navigation.LegacyNavigation.bindBar(
                bar, 1, listOf(onInicio, {}, onChat, onTreinos, onPerfil))
        }
    )
}
