package com.example.myapplication.questionario2

import com.example.myapplication.R

import android.content.Intent

class InactivityActivity : CadastroActivity() {
    override val screenLayout = R.layout.q2_activity_inactivity
    override val optionIds = emptyList<Int>()
    override val answerInputId = R.id.q2_inactivity_input

    override fun onAnswerConfirmed() {
        startActivity(Intent(this, ExperienceActivity::class.java))
    }
}
