package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged

class RecuperarSenhaActivity : AppCompatActivity() {

    private var enviado = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar_senha)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.RecuperarSenha)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvTitulo = findViewById<TextView>(R.id.tvTitulo)
        val tvDescricao = findViewById<TextView>(R.id.tvDescricao)
        val tvLabel = findViewById<TextView>(R.id.tvLabel)
        val campo = findViewById<View>(R.id.campoMatricula)
        val etMatricula = findViewById<EditText>(R.id.etMatricula)
        val tvErroCampo = findViewById<TextView>(R.id.tvErroCampo)
        val btnContinuar = findViewById<AppCompatButton>(R.id.btnContinuar)

        findViewById<View>(R.id.btnVoltar).setOnClickListener { finish() }

        etMatricula.doAfterTextChanged { tvErroCampo.visibility = View.GONE }

        btnContinuar.setOnClickListener {
            if (enviado) {
                finish()
                return@setOnClickListener
            }
            val matricula = etMatricula.text.toString().trim()
            when {
                // Estado: campo vazio
                matricula.isEmpty() -> tvErroCampo.visibility = View.VISIBLE

                // Estado: matrícula não encontrada
                // TODO: trocar este mock pela consulta real da matrícula (back-end)
                matricula.length < 6 -> {
                    tvTitulo.text = "Matrícula não encontrada!"
                    tvDescricao.text = "Ops, não foi possível identificar uma conta correspondente. " +
                            "Verifique as informações e tente novamente"
                }

                // Estado: e-mail enviado
                else -> {
                    enviado = true
                    val email = "luca*******@gmail.com" // TODO: vir do back-end
                    tvTitulo.text = "E-mail enviado com sucesso!"
                    tvDescricao.text = "E-mail enviado para $email com as informações para alteração de senha"
                    tvLabel.visibility = View.GONE
                    campo.visibility = View.GONE
                    tvErroCampo.visibility = View.GONE
                    btnContinuar.text = "Voltar"
                }
            }
        }
    }
}
