package com.example.myapplication.questionario2

import com.example.myapplication.R

import android.content.Intent

class InjuryActivity : PerguntaComDetalhesActivity() {
    override val screenLayout = R.layout.q2_activity_injury
    override val noButtonId = R.id.q2_injury_no
    override val yesButtonId = R.id.q2_injury_yes
    override val detailInputId = R.id.q2_injury_input
    override val detailError = R.string.q2_injury_error

    override fun abrirProximaTela() {
        startActivity(Intent(this, PainActivity::class.java))
    }
}
