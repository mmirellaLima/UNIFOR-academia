package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.navigation.AppNavigation

class ExercicioSeletActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exercicioselet)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ExSelet)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nome = intent.getStringExtra("nome")
        if (nome != null) {
            findViewById<TextView>(R.id.tvTituloExercicio).text = nome
        }

        val foto = when {
            nome?.contains("rosca", ignoreCase = true) == true -> R.drawable.ex_rosca_biceps
            nome?.contains("ombro", ignoreCase = true) == true ||
                    nome?.contains("desenvolvimento", ignoreCase = true) == true ->
                R.drawable.ex_desenvolvimento_ombros
            else -> R.drawable.ex_supino_maquina
        }
        findViewById<ImageView>(R.id.imgExercicio).setImageResource(foto)

        findViewById<View>(R.id.btnVoltar).setOnClickListener { finish() }
        findViewById<View>(R.id.button2).setOnClickListener { finish() }

        val barra = findViewById<LinearLayout>(R.id.barraInferior)
        barra.getChildAt(0).setOnClickListener { AppNavigation.home(this) }
        barra.getChildAt(1).setOnClickListener { AppNavigation.progress(this) }
        findViewById<View>(R.id.btnChat).setOnClickListener { AppNavigation.chat(this) }
        barra.getChildAt(3).setOnClickListener { AppNavigation.workouts(this) }
        findViewById<View>(R.id.btnPerfil).setOnClickListener { AppNavigation.profile(this) }
    }
}