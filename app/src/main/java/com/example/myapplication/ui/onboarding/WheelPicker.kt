package com.example.myapplication.ui.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.example.myapplication.ui.components.AppColors
import kotlin.math.abs

private enum class DirecaoSeta { DIREITA, CIMA }

@Composable
fun WheelPicker(
    values: List<Int>,
    initialValue: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    horizontal: Boolean = false,
    selectedLabel: (Int) -> String = { "$it" },
    arrowOffset: Dp = 64.dp   // distância da setinha até o centro (vertical)
) {
    val itemSize = if (horizontal) 68.dp else 52.dp
    val state = rememberLazyListState(
        initialFirstVisibleItemIndex = values.indexOf(initialValue).coerceAtLeast(0)
    )
    val fling = rememberSnapFlingBehavior(state)
    val selected by remember { derivedStateOf { state.firstVisibleItemIndex } }

    LaunchedEffect(state) {
        snapshotFlow { state.firstVisibleItemIndex }
            .collect { values.getOrNull(it)?.let(onSelected) }
    }

    if (horizontal) {
        Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            LazyRow(
                state = state,
                flingBehavior = fling,
                contentPadding = PaddingValues(horizontal = itemSize * 2),
                modifier = Modifier.width(itemSize * 5),
                verticalAlignment = Alignment.CenterVertically
            ) {
                itemsIndexed(values) { i, v ->
                    WheelCell(
                        text = if (i == selected) selectedLabel(v) else "$v",
                        distancia = abs(i - selected),
                        modifier = Modifier.width(itemSize)
                    )
                }
            }
            Spacer(Modifier.height(6.dp))
            Seta(DirecaoSeta.CIMA)
        }
    } else {
        Box(modifier.width(160.dp).height(itemSize * 5)) {
            LazyColumn(
                state = state,
                flingBehavior = fling,
                contentPadding = PaddingValues(vertical = itemSize * 2),
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(values) { i, v ->
                    WheelCell(
                        text = if (i == selected) selectedLabel(v) else "$v",
                        distancia = abs(i - selected),
                        modifier = Modifier.height(itemSize)
                    )
                }
            }

            // Duas linhas azuis em volta do número selecionado
            Box(
                Modifier
                    .align(Alignment.Center)
                    .width(80.dp)
                    .height(itemSize)
            ) {
                Box(
                    Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .height(1.5.dp)
                        .background(AppColors.Primary)
                )
                Box(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(1.5.dp)
                        .background(AppColors.Primary)
                )
            }

            // Setinha azul à esquerda do número selecionado
            Seta(
                direcao = DirecaoSeta.DIREITA,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = -arrowOffset)
            )
        }
    }
}

@Composable
private fun Seta(direcao: DirecaoSeta, modifier: Modifier = Modifier) {
    Canvas(modifier.size(14.dp)) {
        val path = Path().apply {
            when (direcao) {
                DirecaoSeta.DIREITA -> {
                    moveTo(0f, 0f)
                    lineTo(size.width, size.height / 2f)
                    lineTo(0f, size.height)
                }
                DirecaoSeta.CIMA -> {
                    moveTo(0f, size.height)
                    lineTo(size.width / 2f, 0f)
                    lineTo(size.width, size.height)
                }
            }
            close()
        }
        drawPath(path, AppColors.Primary)
    }
}

@Composable
private fun WheelCell(text: String, distancia: Int, modifier: Modifier) {
    // "67 kg" -> número "67" + unidade " kg" (menor)
    val numero = text.substringBefore(" ")
    val unidade = text.substringAfter(" ", "")

    Box(modifier, contentAlignment = Alignment.Center) {
        Text(
            text = buildAnnotatedString {
                append(numero)
                if (unidade.isNotEmpty()) {
                    withStyle(SpanStyle(fontSize = 28.sp)) { append(" $unidade") }
                }
            },
            color = if (distancia == 0) AppColors.Primary else Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = when (distancia) { 0 -> 44.sp; 1 -> 30.sp; else -> 22.sp },
            softWrap = false,
            modifier = Modifier.wrapContentWidth(unbounded = true)
        )
    }
}
