package com.example.myapplication.questionario2

import com.example.myapplication.R

import android.content.Intent

class PainActivity : PerguntaComDetalhesActivity() {
    override val screenLayout = R.layout.q2_activity_pain
    override val noButtonId = R.id.q2_pain_no
    override val yesButtonId = R.id.q2_pain_yes
    override val detailInputId = R.id.q2_pain_input
    override val detailError = R.string.q2_pain_error

    override fun abrirProximaTela() {
        startActivity(Intent(this, HabitsActivity::class.java))
    }
}
