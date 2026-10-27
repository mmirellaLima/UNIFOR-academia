package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.navigation.AppNavigation

class TreinoAActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_treinoa)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.TreinoA)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Titulo vem do Meus treinos / Feed ("Treino A", "Treino B"...)
        intent.getStringExtra("treino")?.let { findViewById<TextView>(R.id.tvTitulo).text = it }

        findViewById<View>(R.id.btnVoltar).setOnClickListener { finish() }

        findViewById<View>(R.id.cardExercicio1).setOnClickListener { abrirExercicio("Supino Máquina") }
        findViewById<View>(R.id.cardExercicio2).setOnClickListener { abrirExercicio("Rosca bíceps com halteres") }
        findViewById<View>(R.id.cardExercicio3).setOnClickListener { abrirExercicio("Desenvolvimento de ombros") }
        findViewById<View>(R.id.cardConcluido1).setOnClickListener { abrirExercicio("Desenvolvimento de ombros") }

        // Barra inferior: 0 Inicio, 1 Progresso, 2 logo (chat), 3 Treinos, 4 Perfil
        val barra = findViewById<ViewGroup>(R.id.barraInferior)
        barra.getChildAt(0).setOnClickListener { AppNavigation.home(this) }
        barra.getChildAt(1).setOnClickListener { AppNavigation.progress(this) }
        barra.getChildAt(2).setOnClickListener { AppNavigation.chat(this) }
        barra.getChildAt(3).setOnClickListener { AppNavigation.workouts(this) }
        barra.getChildAt(4).setOnClickListener { AppNavigation.profile(this) }
    }

    private fun abrirExercicio(nome: String) = AppNavigation.exercise(this, nome)
}