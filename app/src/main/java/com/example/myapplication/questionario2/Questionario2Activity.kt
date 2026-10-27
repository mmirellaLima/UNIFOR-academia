package com.example.myapplication.questionario2

import com.example.myapplication.R

import android.content.Intent

class Questionario2Activity : CadastroActivity() {
    override val screenLayout = R.layout.q2_activity_main
    override val optionIds = listOf(R.id.q2_option_instagram, R.id.q2_option_facebook,
        R.id.q2_option_youtube, R.id.q2_option_referral, R.id.q2_option_google, R.id.q2_option_other)

    override fun onAnswerConfirmed() {
        startActivity(Intent(this, ReasonActivity::class.java))
    }
}
