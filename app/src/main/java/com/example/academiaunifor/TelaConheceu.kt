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
fun TelaConheceu(
    meios: Set<String>,
    outro: String,
    onMeiosChange: (Set<String>) -> Unit,
    onOutroChange: (String) -> Unit,
    onAvancar: () -> Unit,
    onVoltar: () -> Unit
) {
    var erro by remember { mutableStateOf(false) }
    val opcoes = listOf("Instagram", "Facebook", "YouTube", "Indicação de aluno/professor", "Google", "Outro")

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth().height(40.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "‹", color = Branco, fontSize = 34.sp, modifier = Modifier.clickable { onVoltar() })
            Spacer(Modifier.weight(1f))
            Text(text = "3/12", color = Branco, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.width(20.dp))
        }

        Spacer(Modifier.height(16.dp))
        Text(text = "Como nos conheceu?", color = Branco, fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))

        opcoes.forEach { opcao ->
            Row(
                modifier = Modifier.fillMaxWidth().clickable {
                    val novoSet = if (meios.contains(opcao)) meios - opcao else meios + opcao
                    onMeiosChange(novoSet)
                    erro = false
                }.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = meios.contains(opcao),
                    onCheckedChange = { checked ->
                        val novoSet = if (checked) meios + opcao else meios - opcao
                        onMeiosChange(novoSet)
                        erro = false
                    },
                    colors = CheckboxDefaults.colors(checkedColor = AzulClaro, uncheckedColor = Branco, checkmarkColor = Fundo)
                )
                Text(text = opcao, color = Branco, fontSize = 15.sp, modifier = Modifier.padding(start = 8.dp))
            }
        }

        if (meios.contains("Outro")) {
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = outro,
                onValueChange = { onOutroChange(it); erro = false },
                label = { Text("Especifique o outro meio", color = TextoClaro) },
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AzulClaro, unfocusedBorderColor = Branco, focusedTextColor = Branco, unfocusedTextColor = Branco),
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (erro) {
            Spacer(Modifier.height(12.dp))
            CaixaErro("Selecione ao menos uma opção e especifique caso escolha 'Outro'.")
        }

        Spacer(Modifier.weight(1f))
        Botao(texto = "Próximo") {
            if (meios.isEmpty() || (meios.contains("Outro") && outro.isBlank())) {
                erro = true
            } else {
                erro = false
                onAvancar()
            }
        }
    }
}
