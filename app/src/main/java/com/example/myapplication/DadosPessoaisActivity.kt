package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.navigation.AppNavigation
import com.example.myapplication.navigation.PendingDestination

class DadosPessoaisActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dados_pessoais)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dadosPessoais)) { v, insets ->
            val b = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(b.left, b.top, b.right, b.bottom)
            insets
        }

        // TODO backend: trocar nome e e-mail pelos dados reais do usuário
        findViewById<View>(R.id.btnVoltar).setOnClickListener { finish() }
        findViewById<View>(R.id.btnVerPerfil).setOnClickListener { AppNavigation.profile(this) }
        findViewById<View>(R.id.btnEditarEmail).setOnClickListener {
            startActivity(android.content.Intent(this, EditarEmailActivity::class.java))
        }
        findViewById<View>(R.id.btnAlterarSenha).setOnClickListener {
            startActivity(android.content.Intent(this, AlterarSenhaActivity::class.java))
        }

        val barra = findViewById<LinearLayout>(R.id.barraInferior)
        barra.getChildAt(0).setOnClickListener { AppNavigation.home(this) }
        barra.getChildAt(1).setOnClickListener { AppNavigation.progress(this) }
        findViewById<View>(R.id.btnChat).setOnClickListener { AppNavigation.chat(this) }
        barra.getChildAt(3).setOnClickListener { AppNavigation.workouts(this) }
        barra.getChildAt(4).setOnClickListener { AppNavigation.profile(this) }
    }
}