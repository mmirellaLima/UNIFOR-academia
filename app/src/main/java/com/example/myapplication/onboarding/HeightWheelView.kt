package com.example.myapplication.onboarding

import com.example.myapplication.R

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.accessibility.AccessibilityNodeInfo
import android.view.animation.DecelerateInterpolator
import android.widget.OverScroller
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

/** A continuously moving, snapping height selector like the other onboarding wheels. */
class HeightWheelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private companion object {
        const val MIN_HEIGHT_CM = 100
        const val MAX_HEIGHT_CM = 250
        const val INITIAL_HEIGHT_CM = 170
    }

    var onHeightChanged: ((Int) -> Unit)? = null

    private val density = resources.displayMetrics.density
    private val scaledDensity = resources.displayMetrics.scaledDensity
    private val itemSpacing = 52f * density
    private val maxOffset = (MAX_HEIGHT_CM - MIN_HEIGHT_CM) * itemSpacing
    private val blue = androidx.core.content.ContextCompat.getColor(context, R.color.ob_accent)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
    }
    private val accentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = blue
        strokeWidth = 2.5f * density
        strokeCap = Paint.Cap.ROUND
    }
    private val arrowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = blue }
    private val arrowPath = Path()
    private val scroller = OverScroller(context)
    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private val minimumFlingVelocity = ViewConfiguration.get(context).scaledMinimumFlingVelocity
    private val maximumFlingVelocity = ViewConfiguration.get(context).scaledMaximumFlingVelocity

    private var offset = (INITIAL_HEIGHT_CM - MIN_HEIGHT_CM) * itemSpacing
    private var reportedHeight = INITIAL_HEIGHT_CM
    private var snapAnimator: ValueAnimator? = null
    private var velocityTracker: VelocityTracker? = null
    private var downY = 0f
    private var lastY = 0f
    private var dragging = false
    private var flinging = false

    val selectedHeightCm: Int
        get() = (MIN_HEIGHT_CM + offset / itemSpacing).roundToInt()
            .coerceIn(MIN_HEIGHT_CM, MAX_HEIGHT_CM)

    init {
        isClickable = true
        isFocusable = true
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_YES
        updateDescription()
    }

    fun setHeightCm(heightCm: Int) {
        stopMotion()
        offset = (heightCm.coerceIn(MIN_HEIGHT_CM, MAX_HEIGHT_CM) - MIN_HEIGHT_CM) * itemSpacing
        reportHeight()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            resolveSize((160 * density).roundToInt(), widthMeasureSpec),
            resolveSize((260 * density).roundToInt(), heightMeasureSpec)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val centerX = w / 2f
        val centerY = h / 2f
        arrowPath.reset()
        arrowPath.moveTo(centerX - 64f * density, centerY - 7f * density)
        arrowPath.lineTo(centerX - 64f * density, centerY + 7f * density)
        arrowPath.lineTo(centerX - 50f * density, centerY)
        arrowPath.close()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f
        val current = MIN_HEIGHT_CM + offset / itemSpacing
        val selected = current.roundToInt()

        val lineHalfWidth = 40f * density
        val lineDistance = 26f * density
        canvas.drawLine(centerX - lineHalfWidth, centerY - lineDistance,
            centerX + lineHalfWidth, centerY - lineDistance, accentPaint)
        canvas.drawLine(centerX - lineHalfWidth, centerY + lineDistance,
            centerX + lineHalfWidth, centerY + lineDistance, accentPaint)

        for (value in (floor(current).toInt() - 2)..(ceil(current).toInt() + 2)) {
            if (value !in MIN_HEIGHT_CM..MAX_HEIGHT_CM) continue
            val relative = value - current
            val distance = abs(relative)
            if (distance > 2.7f) continue

            val sizeSp = when {
                distance <= 1f -> 44f - 14f * distance
                else -> 30f - 8f * (distance - 1f)
            }
            textPaint.textSize = sizeSp * scaledDensity
            textPaint.color = if (value == selected) blue else Color.WHITE
            textPaint.alpha = when {
                distance <= 1f -> (255f - 64f * distance).roundToInt()
                else -> (191f - 89f * (distance - 1f)).roundToInt().coerceAtLeast(0)
            }
            val label = AlturaActivity.formatHeight(value) + if (value == selected) " m" else ""
            val baseline = centerY + relative * itemSpacing -
                (textPaint.ascent() + textPaint.descent()) / 2f
            canvas.drawText(label, centerX, baseline, textPaint)
        }
        canvas.drawPath(arrowPath, arrowPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                stopMotion()
                parent?.requestDisallowInterceptTouchEvent(true)
                velocityTracker = VelocityTracker.obtain().also { it.addMovement(event) }
                downY = event.y
                lastY = event.y
                dragging = false
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                velocityTracker?.addMovement(event)
                if (!dragging && abs(event.y - downY) > touchSlop) dragging = true
                if (dragging) setOffset(offset + lastY - event.y)
                lastY = event.y
                return true
            }
            MotionEvent.ACTION_UP -> {
                velocityTracker?.addMovement(event)
                velocityTracker?.computeCurrentVelocity(1000, maximumFlingVelocity.toFloat())
                val velocity = -(velocityTracker?.yVelocity ?: 0f)
                velocityTracker?.recycle()
                velocityTracker = null
                parent?.requestDisallowInterceptTouchEvent(false)
                if (dragging) {
                    if (abs(velocity) >= minimumFlingVelocity) {
                        flinging = true
                        scroller.fling(0, offset.roundToInt(), 0, velocity.roundToInt(),
                            0, 0, 0, maxOffset.roundToInt())
                        postInvalidateOnAnimation()
                    } else snapToNearest()
                } else {
                    val step = ((event.y - height / 2f) / itemSpacing).roundToInt()
                    animateTo(selectedHeightCm + step)
                    performClick()
                }
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                velocityTracker?.recycle()
                velocityTracker = null
                parent?.requestDisallowInterceptTouchEvent(false)
                snapToNearest()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            setOffset(scroller.currY.toFloat())
            postInvalidateOnAnimation()
        } else if (flinging) {
            flinging = false
            snapToNearest()
        }
    }

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_SCROLL) {
            val scroll = event.getAxisValue(MotionEvent.AXIS_VSCROLL)
            if (scroll != 0f) {
                animateTo(selectedHeightCm + if (scroll > 0f) -1 else 1)
                return true
            }
        }
        return super.onGenericMotionEvent(event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP -> animateTo(selectedHeightCm - 1)
            KeyEvent.KEYCODE_DPAD_DOWN -> animateTo(selectedHeightCm + 1)
            else -> return super.onKeyDown(keyCode, event)
        }
        return true
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        info.className = "android.widget.NumberPicker"
        info.isScrollable = true
        info.rangeInfo = AccessibilityNodeInfo.RangeInfo.obtain(
            AccessibilityNodeInfo.RangeInfo.RANGE_TYPE_INT,
            MIN_HEIGHT_CM.toFloat(), MAX_HEIGHT_CM.toFloat(), selectedHeightCm.toFloat()
        )
        info.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD)
        info.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_BACKWARD)
    }

    override fun performAccessibilityAction(action: Int, arguments: android.os.Bundle?): Boolean {
        when (action) {
            AccessibilityNodeInfo.ACTION_SCROLL_FORWARD -> animateTo(selectedHeightCm + 1)
            AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD -> animateTo(selectedHeightCm - 1)
            else -> return super.performAccessibilityAction(action, arguments)
        }
        return true
    }

    override fun onDetachedFromWindow() {
        stopMotion()
        velocityTracker?.recycle()
        velocityTracker = null
        super.onDetachedFromWindow()
    }

    private fun setOffset(value: Float) {
        offset = value.coerceIn(0f, maxOffset)
        reportHeight()
        invalidate()
    }

    private fun reportHeight() {
        val height = selectedHeightCm
        if (height != reportedHeight) {
            reportedHeight = height
            onHeightChanged?.invoke(height)
            updateDescription()
        }
    }

    private fun updateDescription() {
        contentDescription = resources.getString(
            R.string.ob_altura_picker_accessibility,
            AlturaActivity.formatHeight(selectedHeightCm)
        )
    }

    private fun snapToNearest() = animateTo(selectedHeightCm)

    private fun animateTo(heightCm: Int) {
        stopMotion()
        val target = (heightCm.coerceIn(MIN_HEIGHT_CM, MAX_HEIGHT_CM) - MIN_HEIGHT_CM) * itemSpacing
        if (abs(target - offset) < 0.5f) {
            setOffset(target)
            return
        }
        snapAnimator = ValueAnimator.ofFloat(offset, target).apply {
            duration = 190L
            interpolator = DecelerateInterpolator()
            addUpdateListener { setOffset(it.animatedValue as Float) }
            start()
        }
    }

    private fun stopMotion() {
        scroller.forceFinished(true)
        flinging = false
        snapAnimator?.cancel()
        snapAnimator = null
    }
}
