package com.example.myapplication.onboarding

import com.example.myapplication.R

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import kotlin.math.max
import kotlin.math.roundToInt

/** Renders the shared onboarding background at the same proportions as the other flows. */
class RegistrationBackgroundView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {
    private val background = AppCompatResources.getDrawable(context, R.drawable.bg_shapes_completo)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val drawable = background ?: return
        val referenceWidth = 402f
        val referenceHeight = 846f
        val scale = max(width / referenceWidth, height / referenceHeight)
        val renderedWidth = (referenceWidth * scale).roundToInt()
        val renderedHeight = (referenceHeight * scale).roundToInt()
        val left = (width - renderedWidth) / 2
        val top = (height - renderedHeight) / 2
        drawable.setBounds(left, top, left + renderedWidth, top + renderedHeight)
        drawable.draw(canvas)
    }
}
