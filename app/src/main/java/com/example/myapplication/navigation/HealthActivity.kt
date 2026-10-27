package com.example.myapplication.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.myapplication.ui.components.AppBackground
import com.example.myapplication.ui.saude.TelaSaude
import com.example.myapplication.ui.theme.AcademiaUniforTheme

class HealthActivity : ComponentActivity() {
    private val reviewing get() = intent.getBooleanExtra(AppNavigation.EXTRA_RETAKE, false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        com.example.myapplication.navigation.AppNavigation.styleSystemBars(this)
        val session = DemoSession(this)
        setContent {
            AcademiaUniforTheme {
                var index by rememberSaveable { mutableIntStateOf(if (reviewing) 0 else session.healthIndex) }
                var answers by rememberSaveable { mutableStateOf(List(10) { session.answer(it, reviewing) }) }
                fun back() {
                    if (index > 0) index-- else finish()
                }
                BackHandler { back() }
                SideEffect { if (!reviewing) session.healthIndex = index }
                AppBackground(scroll = androidx.compose.ui.platform.LocalConfiguration.current.screenHeightDp < 600) {
                    TelaSaude(index, answers, onResponder = { answer ->
                        session.saveAnswer(index, answer, reviewing)
                        answers = answers.toMutableList().also { it[index] = answer }
                        if (index < 9) index++ else AppNavigation.terms(this@HealthActivity, reviewing)
                    }, onVoltar = { back() })
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        if (!reviewing) DemoSession(this).step = SetupStep.HEALTH
    }
}
