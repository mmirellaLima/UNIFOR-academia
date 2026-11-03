package com.example.myapplication.navigation

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.myapplication.R
import com.example.myapplication.AcessibilidadePrefs
import kotlin.math.roundToInt

/** Um único ajuste de fonte para todos os botões de acessibilidade em telas XML. */
class FontAccessibility private constructor(
    private val activity: AppCompatActivity,
    private val root: ViewGroup,
    private val anchor: View
) : DefaultLifecycleObserver {
    private var fontProgress = 0
    private var fontPopup: android.widget.PopupWindow? = null
    private val originalTextSizes = mutableMapOf<TextView, Float>()

    private fun refresh() {
        collectTextSizes(root)
        fontProgress = (AcessibilidadePrefs.getEscala(activity).coerceIn(0.8f, 1.6f) * 100).roundToInt() - 80
        applyTextSize()
    }

    override fun onResume(owner: LifecycleOwner) { refresh() }
    override fun onStop(owner: LifecycleOwner) { dismiss() }
    override fun onDestroy(owner: LifecycleOwner) {
        dismiss()
        originalTextSizes.clear()
        root.setTag(R.id.font_accessibility_controller, null)
    }

    fun dismiss() { fontPopup?.dismiss() }
    private fun collectTextSizes(view: View) {
        if (view is TextView) originalTextSizes.putIfAbsent(view, view.textSize)
        if (view is ViewGroup) for (index in 0 until view.childCount) collectTextSizes(view.getChildAt(index))
    }

    private fun applyTextSize() {
        originalTextSizes.forEach { (view, size) ->
            view.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, size * ((80 + fontProgress) / 100f))
        }
    }

    fun showFontPanel() {
        if (fontPopup?.isShowing == true) return
        val content = activity.layoutInflater.inflate(R.layout.feed_font_popup, null)
        val margin = (12 * activity.resources.displayMetrics.density).toInt()
        val panelWidth = minOf((300 * activity.resources.displayMetrics.density).toInt(),
            root.width - root.paddingLeft - root.paddingRight - 2 * margin)
        val popup = android.widget.PopupWindow(content, panelWidth,
            ViewGroup.LayoutParams.WRAP_CONTENT, true).apply {
            setBackgroundDrawable(android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT))
            isOutsideTouchable = true
            elevation = 12 * activity.resources.displayMetrics.density
            inputMethodMode = android.widget.PopupWindow.INPUT_METHOD_NOT_NEEDED
        }
        fontPopup = popup
        val slider = content.findViewById<android.widget.SeekBar>(R.id.feed_font_slider)
        slider.max = 80
        slider.progress = fontProgress
        ViewCompat.setStateDescription(slider, activity.getString(R.string.feed_font_percentage, 80 + fontProgress))
        slider.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                if (!fromUser) return
                fontProgress = progress
                applyTextSize()
                AcessibilidadePrefs.setEscala(activity, (80 + progress) / 100f)
                ViewCompat.setStateDescription(slider,
                    activity.getString(R.string.feed_font_percentage, 80 + progress))
            }
            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) = Unit
        })
        content.findViewById<View>(R.id.feed_font_close).setOnClickListener { popup.dismiss() }
        popup.setOnDismissListener {
            fontPopup = null
            anchor.requestFocus()
        }
        popup.showAtLocation(root, android.view.Gravity.END or android.view.Gravity.CENTER_VERTICAL,
            root.paddingRight + margin, 0)
        content.findViewById<View>(R.id.feed_font_close).requestFocus()
    }


    companion object {
        private val buttons = intArrayOf(R.id.feed_font, R.id.btnFonte, R.id.btnAcessibilidade,
            R.id.admin_accessibility_badge, R.id.accessibility_action)

        fun bind(activity: AppCompatActivity, anchorId: Int? = null): FontAccessibility {
            val root = activity.findViewById<ViewGroup>(android.R.id.content)
            val anchors = buttons.toList().mapNotNull { activity.findViewById<View>(it) }
            val controller = (root.getTag(R.id.font_accessibility_controller) as? FontAccessibility)
                ?: FontAccessibility(activity, root,
                    anchorId?.let { activity.findViewById<View>(it) } ?: anchors.firstOrNull() ?: root).also {
                    root.setTag(R.id.font_accessibility_controller, it)
                    it.refresh()
                    activity.lifecycle.addObserver(it)
                }
            anchors.forEach { anchor -> anchor.setOnClickListener { controller.showFontPanel() } }
            return controller
        }

        fun show(activity: AppCompatActivity) { bind(activity).showFontPanel() }
        fun dismiss(activity: AppCompatActivity) {
            val root = activity.findViewById<View>(android.R.id.content)
            (root.getTag(R.id.font_accessibility_controller) as? FontAccessibility)?.dismiss()
        }
    }
}
