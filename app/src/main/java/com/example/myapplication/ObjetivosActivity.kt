package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.myapplication.navigation.AppNavigation
import com.example.myapplication.navigation.DemoSession
import com.example.myapplication.navigation.PendingDestination

class ObjetivosActivity : AppCompatActivity() {

    private val selectedGoals = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_objetivos)

        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.objetivos_root)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val session = DemoSession(this)
        selectedGoals.addAll(session.objectives)
        if (selectedGoals.isEmpty()) {
            selectedGoals.add("Emagrecimento")
        }

        val options = mapOf(
            R.id.optEmagrecimento to "Emagrecimento",
            R.id.optHipertrofia to "Hipertrofia Muscular",
            R.id.optCondicionamento to "Condicionamento",
            R.id.optTerapeutico to "Terapêutico",
            R.id.optConvivio to "Convívio Social",
            R.id.optOutros to "Outros",
            R.id.optLazer to "Lazer"
        )

        options.forEach { (viewId, label) ->
            val view = findViewById<TextView>(viewId)
            view.isSelected = selectedGoals.contains(label)
            view.setOnClickListener {
                if (selectedGoals.contains(label)) {
                    selectedGoals.remove(label)
                    view.isSelected = false
                } else {
                    selectedGoals.add(label)
                    view.isSelected = true
                }
            }
        }

        findViewById<View>(R.id.btnVoltar).setOnClickListener { finish() }
        findViewById<View>(R.id.btnAcaoVoltar).setOnClickListener { finish() }

        findViewById<View>(R.id.btnSalvarObjetivos).setOnClickListener {
            session.objectives = selectedGoals
            finish()
        }

        findViewById<View>(R.id.btnAcessibilidade).setOnClickListener {
            AppNavigation.pending(this, PendingDestination.FONT_SIZE)
        }
    }
}
