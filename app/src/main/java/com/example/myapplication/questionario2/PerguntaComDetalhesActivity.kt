package com.example.myapplication.questionario2

import com.example.myapplication.R

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.button.MaterialButton

// Comportamento das perguntas que pedem detalhes apenas quando a resposta é Sim.
abstract class PerguntaComDetalhesActivity : CadastroActivity() {
    protected abstract val noButtonId: Int
    protected abstract val yesButtonId: Int
    protected abstract val detailInputId: Int
    protected abstract val detailError: Int
    protected abstract fun abrirProximaTela()
    private var selectedChoice = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedChoice = savedInstanceState?.getInt("selected_choice", -1) ?: draftChoice("selected_choice")
        atualizarResposta()
        listOf(noButtonId, yesButtonId).forEach { id ->
            findViewById<MaterialButton>(id).setOnClickListener {
                selectedChoice = id
                atualizarResposta()
            }
        }
    }

    private fun atualizarResposta() {
        listOf(noButtonId, yesButtonId).forEach { id ->
            findViewById<MaterialButton>(id).isChecked = id == selectedChoice
        }
        findViewById<EditText>(detailInputId).apply {
            isEnabled = selectedChoice == yesButtonId
            if (!isEnabled) {
                error = null
                clearFocus()
                WindowInsetsControllerCompat(window, this).hide(WindowInsetsCompat.Type.ime())
            }
        }
    }

    override fun onPause() {
        saveChoice("selected_choice", selectedChoice)
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("selected_choice", selectedChoice)
        super.onSaveInstanceState(outState)
    }

    override fun onAnswerConfirmed() {
        val input = findViewById<EditText>(detailInputId)
        if (selectedChoice == -1) {
            Toast.makeText(this, R.string.q2_select_experience, Toast.LENGTH_SHORT).show()
        } else if (selectedChoice == yesButtonId && input.text.isNullOrBlank()) {
            input.error = getString(detailError)
            input.requestFocus()
        } else {
            abrirProximaTela()
        }
    }
}
