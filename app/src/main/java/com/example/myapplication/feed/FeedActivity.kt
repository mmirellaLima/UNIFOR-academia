package com.example.myapplication.feed

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.myapplication.ChatActivity
import com.example.myapplication.ConfiguracoesActivity
import com.example.myapplication.ExercicioSeletActivity
import com.example.myapplication.MeusTreinosActivity
import com.example.myapplication.R
import com.example.myapplication.TreinoAActivity

class FeedActivity : AppCompatActivity() {
    private var fontProgress = 0
    private var fontPopup: android.widget.PopupWindow? = null
    private val classRenderers = mutableListOf<() -> Unit>()
    private val originalTextSizes = mutableMapOf<TextView, Float>()

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
        findViewById<View>(R.id.feed_start).setOnClickListener { open(TreinoAActivity::class.java) }
        findViewById<View>(R.id.feed_nav_training).setOnClickListener { open(MeusTreinosActivity::class.java) }
        findViewById<View>(R.id.feed_nav_profile).setOnClickListener { open(ConfiguracoesActivity::class.java) }
        findViewById<View>(R.id.feed_nav_chat).setOnClickListener { open(ChatActivity::class.java) }
        findViewById<View>(R.id.feed_nav_home).setOnClickListener {
            findViewById<androidx.core.widget.NestedScrollView>(R.id.feed_scroll).smoothScrollTo(0, 0)
        }
        findViewById<View>(R.id.feed_nav_progress).setOnClickListener {
            Toast.makeText(this, R.string.feed_progress_pending, Toast.LENGTH_SHORT).show()
        }
        listOf(R.id.feed_bench_card to R.string.feed_bench,
            R.id.feed_curl_card to R.string.feed_curl,
            R.id.feed_shoulder_card to R.string.feed_shoulder).forEach { (id, title) ->
            findViewById<View>(id).setOnClickListener {
                startActivity(Intent(this, ExercicioSeletActivity::class.java).putExtra("nome", getString(title)))
            }
        }
        bindClass("jiu", R.id.feed_jiu_action, R.id.feed_jiu_seats, 5, false)
        bindClass("pilates", R.id.feed_pilates_action, R.id.feed_pilates_seats, 4, true)
        collectTextSizes(findViewById(R.id.feed_root))
        fontProgress = getSharedPreferences("feed_accessibility", MODE_PRIVATE)
            .getInt("font_progress", 0).coerceIn(0, 50)
        applyTextSize()
        findViewById<View>(R.id.feed_font).setOnClickListener { showFontPanel() }
        if (savedInstanceState?.getBoolean("font_panel_open") == true) {
            findViewById<View>(R.id.feed_font).post { showFontPanel() }
        }
    }

    private fun open(screen: Class<*>) {
        startActivity(Intent(this, screen).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
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
                fontPopup?.dismiss()
                ClassEnrollmentDialog.forClass(key, leaving = preferences.getBoolean(key, initial)).showNow(supportFragmentManager, ClassEnrollmentDialog.TAG)
            }
        }
    }

    fun onClassEnrollmentChanged() {
        classRenderers.forEach { it() }
    }

    private fun collectTextSizes(view: View) {
        if (view is TextView) originalTextSizes[view] = view.textSize
        if (view is ViewGroup) for (index in 0 until view.childCount) collectTextSizes(view.getChildAt(index))
    }

    private fun applyTextSize() {
        originalTextSizes.forEach { (view, size) ->
            view.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, size * (1f + fontProgress / 100f))
        }
    }

    private fun showFontPanel() {
        if (fontPopup?.isShowing == true) return
        val content = layoutInflater.inflate(R.layout.feed_font_popup, null)
        val margin = (12 * resources.displayMetrics.density).toInt()
        val root = findViewById<View>(R.id.feed_root)
        val panelWidth = minOf((300 * resources.displayMetrics.density).toInt(),
            root.width - root.paddingLeft - root.paddingRight - 2 * margin)
        val popup = android.widget.PopupWindow(content, panelWidth,
            ViewGroup.LayoutParams.WRAP_CONTENT, true).apply {
            setBackgroundDrawable(android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT))
            isOutsideTouchable = true
            elevation = 12 * resources.displayMetrics.density
            inputMethodMode = android.widget.PopupWindow.INPUT_METHOD_NOT_NEEDED
        }
        fontPopup = popup
        val slider = content.findViewById<android.widget.SeekBar>(R.id.feed_font_slider)
        slider.progress = fontProgress
        ViewCompat.setStateDescription(slider, getString(R.string.feed_font_percentage, 100 + fontProgress))
        slider.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                if (!fromUser) return
                fontProgress = progress
                applyTextSize()
                getSharedPreferences("feed_accessibility", MODE_PRIVATE).edit()
                    .putInt("font_progress", progress).apply()
                ViewCompat.setStateDescription(slider,
                    getString(R.string.feed_font_percentage, 100 + progress))
            }
            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) = Unit
        })
        content.findViewById<View>(R.id.feed_font_close).setOnClickListener { popup.dismiss() }
        popup.setOnDismissListener {
            fontPopup = null
            findViewById<View>(R.id.feed_font).requestFocus()
        }
        popup.showAtLocation(root, android.view.Gravity.END or android.view.Gravity.CENTER_VERTICAL,
            root.paddingRight + margin, 0)
        content.findViewById<View>(R.id.feed_font_close).requestFocus()
    }

    override fun onDestroy() {
        fontPopup?.dismiss()
        super.onDestroy()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("font_panel_open", fontPopup?.isShowing == true)
        super.onSaveInstanceState(outState)
    }
}
