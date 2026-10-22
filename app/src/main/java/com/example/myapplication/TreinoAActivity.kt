package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

        findViewById<View>(R.id.btnVoltar).setOnClickListener { finish() }

        findViewById<View>(R.id.cardExercicio1).setOnClickListener { abrirExercicio("Supino Máquina") }
        findViewById<View>(R.id.cardExercicio2).setOnClickListener { abrirExercicio("Rosca bíceps com halteres") }
        findViewById<View>(R.id.cardExercicio3).setOnClickListener { abrirExercicio("Desenvolvimento de ombros") }
        findViewById<View>(R.id.cardConcluido1).setOnClickListener { abrirExercicio("Desenvolvimento de ombros") }

        findViewById<View>(R.id.btnChat).setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }
    }

    private fun abrirExercicio(nome: String) {
        val intent = Intent(this, ExercicioSeletActivity::class.java)
        intent.putExtra("nome", nome)
        startActivity(intent)
    }
}