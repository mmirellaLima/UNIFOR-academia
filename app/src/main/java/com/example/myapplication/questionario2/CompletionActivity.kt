package com.example.myapplication.questionario2

import com.example.myapplication.R


class CompletionActivity : CadastroActivity() {
    override val screenLayout = R.layout.q2_activity_completion
    override val optionIds = emptyList<Int>()

    override fun onAnswerConfirmed() {
        com.example.myapplication.navigation.AppNavigation.setup(this, com.example.myapplication.navigation.SetupStep.GOALS)
    }
}
