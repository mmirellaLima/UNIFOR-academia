package com.example.myapplication.onboarding

import com.example.myapplication.R

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/** Decorative geometry uses the proportions of the supplied reference. */
class RegistrationBackgroundView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = androidx.core.content.ContextCompat.getColor(context, R.color.ob_geometry)
        strokeWidth = 0.8f
        style = Paint.Style.STROKE
    }
    private val path = Path()
    private fun line(canvas: Canvas, vararg points: Float) {
        path.apply {
            reset()
            moveTo(points[0], points[1])
            for (i in 2 until points.size step 2) lineTo(points[i], points[i + 1])
        }
        canvas.drawPath(path, paint)
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.scale(width / 350f, height / 756f)
        line(canvas, 223f, 19f, 250f, 0f, 289f, 0f, 315f, 19f, 223f, 19f)
        canvas.drawArc(222f, 31f, 316f, 117f, 180f, 180f, false, paint)
        line(canvas, 222f, 74f, 316f, 74f)
        line(canvas, 0f, 266f, 19f, 253f, 69f, 290f, 25f, 290f, 69f, 324f, 0f, 324f)
        line(canvas, 0f, 290f, 12f, 290f, 0f, 300f)
        line(canvas, 142f, 502f, 191f, 465f, 240f, 502f, 142f, 502f)
        canvas.drawOval(152f, 491f, 230f, 571f, paint)
        line(canvas, 142f, 542f, 240f, 542f, 191f, 579f, 142f, 542f)
        canvas.drawOval(-59f, 563f, 117f, 738f, paint)
        line(canvas, 0f, 642f, 117f, 642f)
        line(canvas, 0f, 660f, 117f, 660f)
        line(canvas, 310f, 546f, 341f, 589f, 341f, 546f, 350f, 559f)
        line(canvas, 310f, 546f, 310f, 642f, 341f, 599f, 341f, 642f, 350f, 629f)
        canvas.restore()
    }
}
