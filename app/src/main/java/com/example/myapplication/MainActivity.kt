package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.navigation.*
import com.example.myapplication.ui.onboarding.*
import com.example.myapplication.ui.theme.AcademiaUniforTheme

class MainActivity : com.example.myapplication.BaseActivity() {
    private var currentPage = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        com.example.myapplication.navigation.AppNavigation.styleSystemBars(this)
        val session = DemoSession(this)
        val forcedStep = intent.getStringExtra(AppNavigation.EXTRA_STEP)
        if (savedInstanceState == null && forcedStep == null && !intent.getBooleanExtra(AppNavigation.EXTRA_LOGIN, false)) {
            when (session.state().entry()) {
                EntryDestination.STUDENT_HOME, EntryDestination.ADMIN_HOME -> {
                    AppNavigation.home(this); finish(); return
                }
                EntryDestination.SETUP -> {
                    AppNavigation.resumeSetup(this, session.step); finish(); return
                }
                EntryDestination.WELCOME -> Unit
            }
        }
        setContent {
            AcademiaUniforTheme {
                val vm: OnboardingViewModel = viewModel()
                var page by rememberSaveable {
                    mutableStateOf(when (forcedStep) {
                        SetupStep.AGE.name -> "age"
                        SetupStep.WEIGHT.name -> "weight"
                        else -> if (intent.getBooleanExtra(AppNavigation.EXTRA_LOGIN, false)) "login" else "welcome"
                    })
                }
                var chooseRole by rememberSaveable { mutableStateOf(false) }
                val pages = rememberSaveableStateHolder()
                fun back() {
                    page = when (page) {
                        "weight" -> "age"
                        "age" -> "register"
                        else -> "welcome"
                    }
                }
                BackHandler(page != "welcome") { back() }
                SideEffect {
                    currentPage = page
                    if (lifecycle.currentState.isAtLeast(androidx.lifecycle.Lifecycle.State.RESUMED)
                        && session.active && !session.configured && page in listOf("age", "weight")) {
                        session.step = if (page == "age") SetupStep.AGE else SetupStep.WEIGHT
                    }
                }
                Box(Modifier.fillMaxSize()) {
                    pages.SaveableStateProvider(page) {
                        when (page) {
                            "welcome" -> ApresentacaoScreen(onLogin = { page = "login" }, onCadastro = { page = "register" })
                            "register" -> CadastroScreen(vm) { page = "age" }
                            "login" -> LoginScreen(onEntrar = { chooseRole = true },
                                onEsqueceuSenha = { AppNavigation.recovery(this@MainActivity) })
                            "age" -> IdadeScreen(vm, onVoltar = { back() }, onAvancar = { page = "weight" })
                            "weight" -> PesoScreen(vm, onVoltar = { back() },
                                onAvancar = { AppNavigation.setup(this@MainActivity, SetupStep.HEIGHT) })
                        }
                    }
                    if (page == "register" || page == "login") {
                        IconButton(onClick = { back() }, modifier = Modifier.align(Alignment.TopStart).systemBarsPadding()) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.flow_back), tint = Color.White)
                        }
                    }
                }
                if (chooseRole) {
                    AlertDialog(
                        onDismissRequest = { chooseRole = false },
                        title = { Text(stringResource(R.string.flow_demo_title)) },
                        text = {
                            androidx.compose.foundation.layout.Column {
                                TextButton(onClick = {
                                    chooseRole = false
                                    session.enterDemo(AccessRole.STUDENT)
                                    AppNavigation.home(this@MainActivity)
                                    finish()
                                }) { Text(stringResource(R.string.flow_demo_student)) }
                                TextButton(onClick = {
                                    chooseRole = false
                                    session.enterDemo(AccessRole.ADMIN)
                                    AppNavigation.home(this@MainActivity)
                                    finish()
                                }) { Text(stringResource(R.string.flow_demo_admin)) }
                                if (session.hasDraft()) TextButton(onClick = {
                                    chooseRole = false
                                    session.resumeRegistration()
                                    AppNavigation.resumeSetup(this@MainActivity, session.step)
                                    finish()
                                }) { Text(stringResource(R.string.flow_resume)) }
                            }
                        },
                        confirmButton = {},
                        dismissButton = { TextButton(onClick = { chooseRole = false }) { Text(stringResource(R.string.flow_cancel)) } }
                    )
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        val session = DemoSession(this)
        if (session.active && !session.configured) when (currentPage) {
            "age" -> session.step = SetupStep.AGE
            "weight" -> session.step = SetupStep.WEIGHT
        }
    }
}
