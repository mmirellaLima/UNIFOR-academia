package com.example.myapplication.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.myapplication.ObjetivosActivity
import com.example.myapplication.R
import com.example.myapplication.navigation.*

class PerfilActivity : com.example.myapplication.BaseActivity() {

    private lateinit var layoutVazio: View
    private lateinit var layoutLista: View
    private lateinit var containerCards: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.ob_activity_perfil)

        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.flow_profile_root)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, 0, bars.right, bars.bottom)
            findViewById<View>(R.id.ob_profile_header).setPadding(0, bars.top, 0, 0)
            insets
        }

        val session = DemoSession(this)
        findViewById<TextView>(R.id.ob_user_name).text = session.displayName
        findViewById<TextView>(R.id.ob_user_birthdate).text = session.displayRegistration
        findViewById<TextView>(R.id.ob_text_peso_value).text = getString(R.string.flow_weight, session.displayWeight)
        findViewById<TextView>(R.id.ob_text_altura_value).text = getString(
            R.string.ob_altura_selected_value, AlturaActivity.formatHeight(session.displayHeight)
        )

        layoutVazio = findViewById(R.id.layout_objetivo_vazio)
        layoutLista = findViewById(R.id.layout_objetivos_lista)
        containerCards = findViewById(R.id.container_cards_objetivos)

        findViewById<View>(R.id.ob_back_button).setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        findViewById<View>(R.id.ob_button_settings).setOnClickListener { AppNavigation.settings(this) }

        val openGoals = View.OnClickListener {
            startActivity(Intent(this, ObjetivosActivity::class.java))
        }
        findViewById<View>(R.id.ob_button_add_objetivo).setOnClickListener(openGoals)
        findViewById<View>(R.id.flow_all_goals).setOnClickListener(openGoals)
        findViewById<View>(R.id.btn_add_mais_objetivo).setOnClickListener(openGoals)

        listOf(R.id.ob_button_add_resultado, R.id.flow_all_results).forEach {
            findViewById<View>(it).setOnClickListener { AppNavigation.progress(this) }
        }
        listOf(R.id.ob_workout_card_thumbnail, R.id.flow_all_workouts).forEach {
            findViewById<View>(it).setOnClickListener { AppNavigation.workouts(this) }
        }
        findViewById<View>(R.id.ob_button_add_avatar).setOnClickListener {
            AppNavigation.pending(this, PendingDestination.PERSONAL_DATA)
        }
        findViewById<View>(R.id.ob_button_salvar).setOnClickListener { AppNavigation.home(this) }

        LegacyNavigation.bind(this, 4)
    }

    override fun onResume() {
        super.onResume()
        renderObjectives()
    }

    private fun renderObjectives() {
        val session = DemoSession(this)
        val goals = session.objectives

        if (goals.isEmpty()) {
            layoutVazio.visibility = View.VISIBLE
            layoutLista.visibility = View.GONE
        } else {
            layoutVazio.visibility = View.GONE
            layoutLista.visibility = View.VISIBLE
            containerCards.removeAllViews()

            val inflater = LayoutInflater.from(this)
            goals.forEach { goalName ->
                val card = inflater.inflate(R.layout.item_perfil_objetivo, containerCards, false)
                card.findViewById<TextView>(R.id.tv_nome_objetivo).text = goalName
                card.setOnClickListener {
                    startActivity(Intent(this, ObjetivosActivity::class.java))
                }
                containerCards.addView(card)
            }
        }
    }
}
