package com.example.myapplication.feed

import com.example.myapplication.navigation.*
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.myapplication.R
import com.example.myapplication.BaseActivity

class FeedActivity : BaseActivity() {
    private val classRenderers = mutableListOf<() -> Unit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_feed)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.feed_root)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
        findViewById<View>(R.id.feed_back).setOnClickListener { onBackPressedDispatcher.onBackPressed() }
       findViewById<View>(R.id.feed_start).setOnClickListener { AppNavigation.workout(this, "Treino A") }
        LegacyNavigation.bind(this, 0, R.id.feed_nav)
        listOf(R.id.feed_bench_card to R.string.feed_bench,
            R.id.feed_curl_card to R.string.feed_curl,
            R.id.feed_shoulder_card to R.string.feed_shoulder).forEach { (id, title) ->
            findViewById<View>(id).setOnClickListener {
                AppNavigation.exercise(this, getString(title))
            }
        }
        bindClass("jiu", R.id.feed_jiu_action, R.id.feed_jiu_seats, 5, false)
        bindClass("pilates", R.id.feed_pilates_action, R.id.feed_pilates_seats, 4, true)
        findViewById<View>(R.id.feed_font).setOnClickListener {
            AppNavigation.pending(this, PendingDestination.FONT_SIZE)
        }
        AppNavigation.dashboardBack(this)
    }

    private fun bindClass(key: String, buttonId: Int, seatsId: Int, capacity: Int, initial: Boolean) {
        val preferences = getSharedPreferences("feed_demo_classes", MODE_PRIVATE)
        val button = findViewById<AppCompatButton>(buttonId)
        val seats = findViewById<TextView>(seatsId)
        fun render() {
            val enrolled = preferences.getBoolean(key, initial)
            button.setText(if (enrolled) R.string.feed_leave else R.string.feed_join)
            val color = android.graphics.Color.parseColor(if (enrolled) "#B93630" else "#3E46F4")
            button.backgroundTintList = android.content.res.ColorStateList.valueOf(color)
            seats.text = getString(R.string.feed_vacancies, capacity - if (enrolled) 1 else 0)
            seats.setTextColor(android.graphics.Color.parseColor(if (enrolled) "#90FF63" else "#4A52FF"))
        }
        classRenderers.add { render() }
        render()
        button.setOnClickListener {
            if (supportFragmentManager.findFragmentByTag(ClassEnrollmentDialog.TAG) == null) {
                ClassEnrollmentDialog.forClass(key, leaving = preferences.getBoolean(key, initial)).showNow(supportFragmentManager, ClassEnrollmentDialog.TAG)
            }
        }
    }

    fun onClassEnrollmentChanged() {
        classRenderers.forEach { it() }
    }

}
