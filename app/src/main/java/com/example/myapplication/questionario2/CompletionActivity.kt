package com.example.myapplication.questionario2

import com.example.myapplication.R

import android.content.Intent
import com.example.myapplication.ObjetivosActivity

class CompletionActivity : CadastroActivity() {
    override val screenLayout = R.layout.q2_activity_completion
    override val optionIds = emptyList<Int>()

    override fun onAnswerConfirmed() {
        startActivity(Intent(this, ObjetivosActivity::class.java))
    }
}
