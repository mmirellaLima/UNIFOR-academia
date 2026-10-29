package com.example.myapplication

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.navigation.AppNavigation
import com.example.myapplication.navigation.PendingDestination

class AlterarSenhaActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_alterar_senha)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.alterarSenha)) { v, insets ->
            val b = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(b.left, b.top, b.right, b.bottom)
            insets
        }

        val atual = findViewById<EditText>(R.id.etSenhaAtual)
        val nova = findViewById<EditText>(R.id.etSenhaNova)

        findViewById<ImageView>(R.id.olhoAtual).setOnClickListener { alternar(atual, it as ImageView) }
        findViewById<ImageView>(R.id.olhoNova).setOnClickListener { alternar(nova, it as ImageView) }
        findViewById<View>(R.id.btnVoltar).setOnClickListener { finish() }

        findViewById<View>(R.id.btnConfirmar).setOnClickListener {
            val a = atual.text.toString()
            val n = nova.text.toString()
            when {
                a.isBlank() || n.isBlank() ->
                    Toast.makeText(this, "Preencha os dois campos", Toast.LENGTH_SHORT).show()
                n.length < 6 ->
                    Toast.makeText(this, "A nova senha precisa ter ao menos 6 caracteres", Toast.LENGTH_SHORT).show()
                n == a ->
                    Toast.makeText(this, "A nova senha deve ser diferente da atual", Toast.LENGTH_SHORT).show()
                else -> {
                    // TODO backend: validar senha atual e salvar a nova no servidor
                    Toast.makeText(this, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        findViewById<View>(R.id.btnFonte).setOnClickListener { abrirMenuAcessibilidade() }

        val barra = findViewById<LinearLayout>(R.id.barraInferior)
        barra.getChildAt(0).setOnClickListener { AppNavigation.home(this) }
        barra.getChildAt(1).setOnClickListener { AppNavigation.progress(this) }
        findViewById<View>(R.id.btnChat).setOnClickListener { AppNavigation.chat(this) }
        barra.getChildAt(3).setOnClickListener { AppNavigation.workouts(this) }
        barra.getChildAt(4).setOnClickListener { AppNavigation.profile(this) }
    }

    private fun alternar(campo: EditText, olho: ImageView) {
        val oculto = campo.transformationMethod is PasswordTransformationMethod
        campo.transformationMethod = if (oculto) null else PasswordTransformationMethod.getInstance()
        olho.alpha = if (oculto) 0.5f else 1f
        campo.setSelection(campo.text.length)
    }
}