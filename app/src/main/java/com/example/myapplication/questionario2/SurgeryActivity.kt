package com.example.myapplication.questionario2

import com.example.myapplication.R

import android.content.Intent

class SurgeryActivity : PerguntaComDetalhesActivity() {
    override val screenLayout = R.layout.q2_activity_surgery
    override val noButtonId = R.id.q2_surgery_no
    override val yesButtonId = R.id.q2_surgery_yes
    override val detailInputId = R.id.q2_surgery_input
    override val detailError = R.string.q2_surgery_error

    override fun abrirProximaTela() {
        startActivity(Intent(this, CompletionActivity::class.java))
    }
}
