package com.example.myapplication.onboarding

import com.example.myapplication.R

import com.example.myapplication.navigation.*
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class AlturaActivity : com.example.myapplication.BaseActivity() {

    companion object {
        const val EXTRA_HEIGHT_CM = "com.example.myapplication.onboarding.HEIGHT_CM"
        private const val STATE_HEIGHT_CM = "height_cm"
        private const val DEFAULT_HEIGHT_CM = 170
        private const val MIN_HEIGHT_CM = 100
        private const val MAX_HEIGHT_CM = 250

        fun formatHeight(cm: Int): String =
            "${cm / 100},${(cm % 100).toString().padStart(2, '0')}"
    }

    private var selectedHeightCm = DEFAULT_HEIGHT_CM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.ob_activity_altura)
        findViewById<android.widget.TextView>(R.id.ob_altura_title).text = getString(
            R.string.flow_height_question, DemoSession(this).name.substringBefore(" "))

        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ob_main)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        selectedHeightCm = (savedInstanceState?.getInt(STATE_HEIGHT_CM) ?: DemoSession(this).height)
            .coerceIn(MIN_HEIGHT_CM, MAX_HEIGHT_CM)
        setupHeightWheel()

        findViewById<ImageButton>(R.id.ob_back_button).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        findViewById<AppCompatButton>(R.id.ob_button_voltar).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        findViewById<AppCompatButton>(R.id.ob_button_avancar).setOnClickListener {
            DemoSession(this).height = selectedHeightCm
            AppNavigation.setup(this, SetupStep.INTRO)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(STATE_HEIGHT_CM, selectedHeightCm)
        super.onSaveInstanceState(outState)
    }

    private fun setupHeightWheel() {
        findViewById<HeightWheelView>(R.id.ob_height_picker_container).apply {
            onHeightChanged = { this@AlturaActivity.selectedHeightCm = it; DemoSession(this@AlturaActivity).height = it }
            setHeightCm(this@AlturaActivity.selectedHeightCm)
        }
    }
    override fun onResume() {
        super.onResume()
        DemoSession(this).step = SetupStep.HEIGHT
    }

}
