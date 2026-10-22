package com.example.myapplication.questionario2

import com.example.myapplication.R

import android.content.Intent

class FrequencyActivity : CadastroActivity() {
    override val screenLayout = R.layout.q2_activity_frequency
    override val optionIds = listOf(R.id.q2_option_frequency_1, R.id.q2_option_frequency_2,
        R.id.q2_option_frequency_3, R.id.q2_option_frequency_4, R.id.q2_option_frequency_5,
        R.id.q2_option_frequency_6, R.id.q2_option_frequency_7)
    override val singleSelection = true
    override val selectionError = R.string.q2_select_frequency

    override fun onAnswerConfirmed() {
        startActivity(Intent(this, InactivityActivity::class.java))
    }
}
