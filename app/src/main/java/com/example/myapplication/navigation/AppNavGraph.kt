package com.example.myapplication.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.components.AppBackground
import com.example.myapplication.ui.onboarding.ApresentacaoScreen
import com.example.myapplication.ui.onboarding.CadastroScreen
import com.example.myapplication.ui.onboarding.IdadeScreen
import com.example.myapplication.ui.onboarding.LoginScreen
import com.example.myapplication.ui.onboarding.OnboardingViewModel
import com.example.myapplication.ui.onboarding.PesoScreen
import com.example.myapplication.ui.progresso.ProgressoScreen

@Composable
fun AppNavGraph(nav: NavHostController = rememberNavController()) {
    val vm: OnboardingViewModel = viewModel() // compartilhado entre todas as telas

    // Para testar a tela nova, deixe Routes.PROGRESSO. Antes de commitar, volte para Routes.APRESENTACAO.
    NavHost(navController = nav, startDestination = Routes.PROGRESSO) {

        composable(Routes.APRESENTACAO) {
            ApresentacaoScreen(
                onLogin = { nav.navigate(Routes.LOGIN) },
                onCadastro = { nav.navigate(Routes.CADASTRO) }
            )
        }

        composable(Routes.CADASTRO) {
            CadastroScreen(
                vm = vm,
                onConcluir = { nav.navigate(Routes.IDADE) }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onEntrar = { nav.navigate(Routes.IDADE) }, // TODO: trocar pela Home quando existir
                onEsqueceuSenha = { nav.navigate(Routes.RECUPERAR_SENHA) }
            )
        }

        composable(Routes.IDADE) {
            IdadeScreen(
                vm = vm,
                onVoltar = { nav.popBackStack() },
                onAvancar = { nav.navigate(Routes.PESO) }
            )
        }

        composable(Routes.PESO) {
            PesoScreen(
                vm = vm,
                onVoltar = { nav.popBackStack() },
                onAvancar = { nav.navigate(Routes.ALTURA) }
            )
        }

        composable(Routes.ALTURA) { Placeholder("Tela 06 - Altura") }
        composable(Routes.RECUPERAR_SENHA) { Placeholder("Recuperar senha") }

        composable(Routes.PROGRESSO) {
            ProgressoScreen(onVoltar = { nav.popBackStack() })
        }
    }
}

@Composable
private fun Placeholder(nome: String) {
    AppBackground { Text("$nome (em desenvolvimento)", color = Color.White) }
}