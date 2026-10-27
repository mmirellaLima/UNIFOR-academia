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
fun TelaMotivo(
    motivos: Set<String>,
    outro: String,
    onMotivosChange: (Set<String>) -> Unit,
    onOutroChange: (String) -> Unit,
    onAvancar: () -> Unit,
    onVoltar: () -> Unit
) {
    var erro by remember { mutableStateOf(false) }
    val opcoes = listOf("Bom Preço", "Indicação", "Indicação Profissional", "Serviço Diferenciado", "Mobilidade Urbana", "Aluno/Funcionário", "Outro")

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth().height(40.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "‹", color = Branco, fontSize = 34.sp, modifier = Modifier.clickable { onVoltar() })
            Spacer(Modifier.weight(1f))
            Text(text = "4/12", color = Branco, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.width(20.dp))
        }

        Spacer(Modifier.height(16.dp))
        Text(text = "Por que escolheu a Academia UNIFOR?", color = Branco, fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(Modifier.height(12.dp))

        opcoes.forEach { opcao ->
            Row(
                modifier = Modifier.fillMaxWidth().clickable {
                    val novoSet = if (motivos.contains(opcao)) motivos - opcao else motivos + opcao
                    onMotivosChange(novoSet)
                    erro = false
                }.padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = motivos.contains(opcao),
                    onCheckedChange = { checked ->
                        val novoSet = if (checked) motivos + opcao else motivos - opcao
                        onMotivosChange(novoSet)
                        erro = false
                    },
                    colors = CheckboxDefaults.colors(checkedColor = AzulClaro, uncheckedColor = Branco, checkmarkColor = Fundo)
                )
                Text(text = opcao, color = Branco, fontSize = 15.sp, modifier = Modifier.padding(start = 8.dp))
            }
        }

        if (motivos.contains("Outro")) {
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = outro,
                onValueChange = { onOutroChange(it); erro = false },
                label = { Text("Especifique o outro motivo", color = TextoClaro) },
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
            if (motivos.isEmpty() || (motivos.contains("Outro") && outro.isBlank())) {
                erro = true
            } else {
                erro = false
                onAvancar()
            }
        }
    }
}