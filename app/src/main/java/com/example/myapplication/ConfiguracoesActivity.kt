package com.example.myapplication

import android.content.Intent
import com.example.myapplication.questionario2.Questionario2Activity
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ConfiguracoesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_configuracoes)

        val cabecalho = findViewById<View>(R.id.cabecalho)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.configuracoes)) { v, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(barras.left, 0, barras.right, barras.bottom)
            cabecalho.setPadding(
                cabecalho.paddingLeft, barras.top, cabecalho.paddingRight, cabecalho.paddingBottom
            )
            insets
        }

        findViewById<View>(R.id.btnVoltar).setOnClickListener { finish() }

        findViewById<View>(R.id.btnQuestionario).setOnClickListener {
            startActivity(Intent(this, Questionario2Activity::class.java))
        }

        findViewById<View>(R.id.btnObjetivos).setOnClickListener {
            startActivity(Intent(this, ObjetivosActivity::class.java))
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
