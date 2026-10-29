package com.example.myapplication

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.navigation.AppNavigation
import com.example.myapplication.navigation.PendingDestination

class NotificacoesActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notificacoes)
        val header = findViewById<View>(R.id.cabecalho)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.notificacoes)) { v, insets ->
            val b = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(b.left, 0, b.right, b.bottom)
            header.setPadding(header.paddingLeft, b.top, header.paddingRight, header.paddingBottom)
            insets
        }

        // Preferência salva só no aparelho. TODO backend: sincronizar com o servidor.
        val prefs = getSharedPreferences("notificacoes", MODE_PRIVATE)
        configurarSwitch(findViewById(R.id.swAtualizacoes), "atualizacoes", true, prefs)
        configurarSwitch(findViewById(R.id.swComunicados), "comunicados", false, prefs)

        findViewById<View>(R.id.btnVoltar).setOnClickListener { finish() }
        findViewById<View>(R.id.btnFonte).setOnClickListener { abrirMenuAcessibilidade() }

        val barra = findViewById<LinearLayout>(R.id.barraInferior)
        barra.getChildAt(0).setOnClickListener { AppNavigation.home(this) }
        barra.getChildAt(1).setOnClickListener { AppNavigation.progress(this) }
        findViewById<View>(R.id.btnChat).setOnClickListener { AppNavigation.chat(this) }
        barra.getChildAt(3).setOnClickListener { AppNavigation.workouts(this) }
        barra.getChildAt(4).setOnClickListener { AppNavigation.profile(this) }
    }

    private fun configurarSwitch(
        sw: SwitchCompat,
        chave: String,
        padrao: Boolean,
        prefs: android.content.SharedPreferences
    ) {
        val estados = arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf())
        sw.trackTintList = ColorStateList(
            estados,
            intArrayOf(Color.parseColor("#8AB4FF"), Color.parseColor("#4A4A4F"))
        )
        sw.thumbTintList = ColorStateList.valueOf(Color.WHITE)
        sw.isChecked = prefs.getBoolean(chave, padrao)
        sw.setOnCheckedChangeListener { _, marcado ->
            prefs.edit().putBoolean(chave, marcado).apply()
        }
    }
}