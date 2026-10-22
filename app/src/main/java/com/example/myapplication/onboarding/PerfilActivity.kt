package com.example.myapplication.onboarding

import com.example.myapplication.R
import android.content.Intent
import com.example.myapplication.ConfiguracoesActivity
import com.example.myapplication.MeusTreinosActivity
import com.example.myapplication.ObjetivosActivity

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class PerfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.ob_activity_perfil)

        if (intent.hasExtra(AlturaActivity.EXTRA_HEIGHT_CM)) {
            val heightCm = intent.getIntExtra(AlturaActivity.EXTRA_HEIGHT_CM, 170)
            findViewById<TextView>(R.id.ob_text_altura_value).text = getString(
                R.string.ob_altura_selected_value,
                AlturaActivity.formatHeight(heightCm)
            )
        }

        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ob_main_scroll)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, 0, bars.right, bars.bottom)
            findViewById<android.view.View>(R.id.ob_profile_header).setPadding(0, bars.top, 0, 0)
            insets
        }

        findViewById<ImageButton>(R.id.ob_back_button).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        findViewById<ImageButton>(R.id.ob_button_settings).setOnClickListener {
            startActivity(Intent(this, ConfiguracoesActivity::class.java))
        }

        findViewById<ImageView>(R.id.ob_button_add_objetivo).setOnClickListener {
            startActivity(Intent(this, ObjetivosActivity::class.java))
        }

        findViewById<android.view.View>(R.id.ob_workout_card_thumbnail).setOnClickListener {
            startActivity(Intent(this, MeusTreinosActivity::class.java))
        }

        findViewById<AppCompatButton>(R.id.ob_button_salvar).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
