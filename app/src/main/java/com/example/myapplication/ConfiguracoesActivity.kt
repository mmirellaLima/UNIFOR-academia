package com.example.myapplication

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.navigation.*

class ConfiguracoesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        com.example.myapplication.navigation.AppNavigation.styleSystemBars(this)
        setContentView(R.layout.activity_configuracoes)
        val header = findViewById<View>(R.id.cabecalho)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.configuracoes)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, 0, bars.right, bars.bottom)
            header.setPadding(header.paddingLeft, bars.top, header.paddingRight, header.paddingBottom)
            insets
        }
        findViewById<View>(R.id.btnVoltar).setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        listOf(
            R.id.btnObjetivos to PendingDestination.GOALS,
            R.id.btnDadosPessoais to PendingDestination.PERSONAL_DATA,
            R.id.btnNotificacoes to PendingDestination.NOTIFICATIONS
        ).forEach { (id, destination) ->
            findViewById<View>(id).setOnClickListener { AppNavigation.pending(this, destination) }
        }
        findViewById<View>(R.id.btnQuestionario).setOnClickListener { AppNavigation.reviewHealth(this) }
        findViewById<View>(R.id.btnSair).setOnClickListener { AppNavigation.confirmLogout(this) }
        LegacyNavigation.bind(this, 4)
    }
}
