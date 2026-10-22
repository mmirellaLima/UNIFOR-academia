package com.example.myapplication.onboarding

import com.example.myapplication.R

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class TermosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.ob_activity_termos)

        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ob_main)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val statementView = findViewById<TextView>(R.id.ob_text_termos_statement)
        statementView.text = Html.fromHtml(getString(R.string.ob_termos_checkbox_text), Html.FROM_HTML_MODE_LEGACY)

        val checkbox = findViewById<AppCompatCheckBox>(R.id.ob_checkbox_termos)
        findViewById<LinearLayout>(R.id.ob_terms_layout).setOnClickListener {
            checkbox.performClick()
        }

        findViewById<ImageButton>(R.id.ob_back_button).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Only a user click advances the preview; restoring the checkbox must not open another screen.
        checkbox.setOnClickListener {
            if (checkbox.isChecked) {
                val intent = Intent(this, AlturaActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
