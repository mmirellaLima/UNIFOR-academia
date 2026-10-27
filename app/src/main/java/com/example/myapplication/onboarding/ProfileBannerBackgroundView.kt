package com.example.myapplication.onboarding

import com.example.myapplication.R

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import kotlin.math.max
import kotlin.math.roundToInt

/** Draws the onboarding geometry clipped by the profile header. */
class ProfileBannerBackgroundView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val background = AppCompatResources.getDrawable(context, R.drawable.ob_profile_geometry)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val drawable = background ?: return
        val screenHeight = rootView.height.takeIf { it > height } ?: resources.displayMetrics.heightPixels
        val scale = max(width / 402f, screenHeight / 846f)
        val renderedWidth = (402f * scale).roundToInt()
        val renderedHeight = (846f * scale).roundToInt()
        val left = (width - renderedWidth) / 2
        drawable.setBounds(left, 0, left + renderedWidth, renderedHeight)
        drawable.draw(canvas)
    }
}
