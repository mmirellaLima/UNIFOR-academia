package com.example.myapplication.onboarding

import com.example.myapplication.R

import com.example.myapplication.navigation.*
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class IntroActivity : com.example.myapplication.BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.ob_activity_intro)

        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ob_main)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val titleView = findViewById<TextView>(R.id.ob_intro_title_view)
        titleView.text = Html.fromHtml(getString(R.string.ob_intro_title), Html.FROM_HTML_MODE_LEGACY)

        findViewById<ImageButton>(R.id.ob_back_button).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        findViewById<AppCompatButton>(R.id.ob_button_voltar).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        findViewById<AppCompatButton>(R.id.ob_button_avancar).setOnClickListener {
            AppNavigation.setup(this, SetupStep.HEALTH)
        }
    }
    override fun onResume() {
        super.onResume()
        DemoSession(this).step = SetupStep.INTRO
    }

}
