package com.example.myapplication.questionario2

import com.example.myapplication.R

import android.content.Intent

/** Reuses the registration form behavior with the reasons for choosing the gym. */
class ReasonActivity : CadastroActivity() {
    override val screenLayout = R.layout.q2_activity_reason
    override val optionIds = listOf(R.id.q2_option_price, R.id.q2_option_recommendation,
        R.id.q2_option_professional, R.id.q2_option_service, R.id.q2_option_mobility,
        R.id.q2_option_student_employee, R.id.q2_option_other)
    override val otherError = R.string.q2_describe_reason

    override fun onAnswerConfirmed() {
        startActivity(Intent(this, FrequencyActivity::class.java))
    }
}
