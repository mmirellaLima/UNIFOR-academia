package com.example.myapplication.questionario2

import com.example.myapplication.R

import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import com.google.android.material.button.MaterialButton

class ExperienceActivity : CadastroActivity() {
    override val screenLayout = R.layout.q2_activity_experience
    override val optionIds = emptyList<Int>()
    private var selectedChoice = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedChoice = savedInstanceState?.getInt("experience_choice", -1) ?: -1
        val buttons = listOf(R.id.q2_experience_no, R.id.q2_experience_yes)
        buttons.forEach { id ->
            findViewById<MaterialButton>(id).isChecked = id == selectedChoice
            findViewById<MaterialButton>(id).setOnClickListener {
                selectedChoice = id
                buttons.forEach { choiceId ->
                    findViewById<MaterialButton>(choiceId).isChecked = choiceId == id
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("experience_choice", selectedChoice)
        super.onSaveInstanceState(outState)
    }

    override fun onAnswerConfirmed() {
        if (selectedChoice == -1) {
            Toast.makeText(this, R.string.q2_select_experience, Toast.LENGTH_SHORT).show()
        } else {
            startActivity(Intent(this, InjuryActivity::class.java))
        }
    }
}
