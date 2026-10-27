package com.example.myapplication

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.navigation.AppNavigation
import com.example.myapplication.navigation.PendingDestination

class EditarEmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editar_email)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.editarEmail)) { v, insets ->
            val b = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(b.left, b.top, b.right, b.bottom)
            insets
        }

        // TODO backend: preencher tvEmailAtual com o e-mail real do usuário
        val novo = findViewById<EditText>(R.id.etEmailNovo)

        findViewById<View>(R.id.btnVoltar).setOnClickListener { finish() }
        findViewById<View>(R.id.btnConfirmar).setOnClickListener {
            val email = novo.text.toString().trim()
            when {
                email.isEmpty() ->
                    Toast.makeText(this, "Digite o novo e-mail", Toast.LENGTH_SHORT).show()
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                    Toast.makeText(this, "E-mail inválido", Toast.LENGTH_SHORT).show()
                else -> {
                    // TODO backend: salvar o novo e-mail no servidor
                    Toast.makeText(this, "E-mail atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        findViewById<View>(R.id.btnFonte).setOnClickListener {
            AppNavigation.pending(this, PendingDestination.FONT_SIZE)
        }

        val barra = findViewById<LinearLayout>(R.id.barraInferior)
        barra.getChildAt(0).setOnClickListener { AppNavigation.home(this) }
        barra.getChildAt(1).setOnClickListener { AppNavigation.progress(this) }
        findViewById<View>(R.id.btnChat).setOnClickListener { AppNavigation.chat(this) }
        barra.getChildAt(3).setOnClickListener { AppNavigation.workouts(this) }
        barra.getChildAt(4).setOnClickListener { AppNavigation.profile(this) }
    }
}