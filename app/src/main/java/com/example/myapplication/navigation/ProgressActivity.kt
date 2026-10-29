package com.example.myapplication.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.myapplication.ui.progresso.ProgressoScreen
import com.example.myapplication.ui.theme.AcademiaUniforTheme

class ProgressActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        com.example.myapplication.navigation.AppNavigation.styleSystemBars(this)
        setContent {
            AcademiaUniforTheme {
                ProgressoScreen(
                    onVoltar = { onBackPressedDispatcher.onBackPressed() },
                    onInicio = { AppNavigation.home(this) },
                    onTreinos = { AppNavigation.workouts(this) },
                    onPerfil = { AppNavigation.profile(this) },
                    onChat = { AppNavigation.chat(this) },
                    onExercicio = { AppNavigation.exercise(this, it) }
                )
            }
        }
    }
}
