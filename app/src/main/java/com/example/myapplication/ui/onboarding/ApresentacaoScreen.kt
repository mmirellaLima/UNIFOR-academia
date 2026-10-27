package com.example.myapplication.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.ui.components.AppBackground
import com.example.myapplication.ui.components.AppColors
import com.example.myapplication.ui.components.PrimaryButton

private val AzulClaro = Color(0xFF85B2FF) // retângulo preenchido atrás da foto

@Composable
fun ApresentacaoScreen(onLogin: () -> Unit, onCadastro: () -> Unit) {
    AppBackground(fundo = R.drawable.bg_shapes) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.logo_unifor),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Academia UNIFOR",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Bem-vindo ao aplicativo da Academia da Universidade de Fortaleza",
            color = AppColors.Primary,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(Modifier.weight(1f))

        // Foto quadrada com os dois retângulos de moldura atrás
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.78f)
                .aspectRatio(1f)
        ) {
            // contorno, deslocado para cima e para a esquerda
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(x = (-16).dp, y = (-16).dp)
                    .border(1.dp, AppColors.Primary)
            )
            // preenchido, deslocado para baixo e para a direita
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(x = 16.dp, y = 16.dp)
                    .background(AzulClaro)
            )
            // foto por cima, sem cantos arredondados
            Image(
                painter = painterResource(R.drawable.foto_academia),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(Modifier.weight(1f))
        PrimaryButton("Login", onClick = onLogin)
        Spacer(Modifier.height(12.dp))
        PrimaryButton("Cadastre-se", onClick = onCadastro)
    }
}
