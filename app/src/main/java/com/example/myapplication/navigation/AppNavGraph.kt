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

@Composable
fun AppNavGraph(nav: NavHostController = rememberNavController()) {
    val vm: OnboardingViewModel = viewModel() // compartilhado entre todas as telas

    NavHost(navController = nav, startDestination = Routes.APRESENTACAO) {

        // Tela 01
        composable(Routes.APRESENTACAO) {
            ApresentacaoScreen(
                onLogin = { nav.navigate(Routes.LOGIN) },
                onCadastro = { nav.navigate(Routes.CADASTRO) }
            )
        }

        // Tela 02
        composable(Routes.CADASTRO) {
            CadastroScreen(
                vm = vm,
                onConcluir = { nav.navigate(Routes.IDADE) }
            )
        }

        // Tela 03
        composable(Routes.LOGIN) {
            LoginScreen(
                onEntrar = { nav.navigate(Routes.IDADE) }, // TODO: trocar pela Home quando existir
                onEsqueceuSenha = { nav.navigate(Routes.RECUPERAR_SENHA) }
            )
        }

        // Tela 04
        composable(Routes.IDADE) {
            IdadeScreen(
                vm = vm,
                onVoltar = { nav.popBackStack() },
                onAvancar = { nav.navigate(Routes.PESO) }
            )
        }

        // Tela 05
        composable(Routes.PESO) {
            PesoScreen(
                vm = vm,
                onVoltar = { nav.popBackStack() },
                onAvancar = { nav.navigate(Routes.ALTURA) }
            )
        }

        composable(Routes.ALTURA) { Placeholder("Tela 06 - Altura") }
        composable(Routes.RECUPERAR_SENHA) { Placeholder("Recuperar senha") }
    }
}

@Composable
private fun Placeholder(nome: String) {
    AppBackground { Text("$nome (em desenvolvimento)", color = Color.White) }
}