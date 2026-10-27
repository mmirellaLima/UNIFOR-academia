package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MeusTreinosActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_meustreinos)

        findViewById<ImageView>(R.id.btnTamanhoFonte).setOnClickListener {
            MenuAcessibilidadeDialog().show(supportFragmentManager, "acessibilidade")
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.meusTreinos)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<View>(R.id.btnTreinoA).setOnClickListener {
            startActivity(Intent(this, TreinoAActivity::class.java))
        }

        findViewById<View>(R.id.btnVoltar).setOnClickListener {
            finish()
        }

        findViewById<View>(R.id.btnChat).setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }

        findViewById<View>(R.id.btnPerfil).setOnClickListener {
            startActivity(
                Intent(this, ConfiguracoesActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            )
        }
    }
}