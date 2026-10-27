package com.example.myapplication.admin

import com.example.myapplication.R

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.GridLayout
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.graphics.drawable.toDrawable
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

/** Calendar drawn with native Views to keep the reference's square blue date markers. */
class AdminCalendarDialog : AppCompatDialogFragment() {
    private var month = YearMonth.of(2026, 9)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        month = savedInstanceState?.getString(STATE_MONTH)?.let(YearMonth::parse) ?: month
        val content = layoutInflater.inflate(R.layout.admin_dialog_calendar, FrameLayout(requireContext()), false)
        content.contentDescription = getString(R.string.admin_calendar)
        val grid = content.findViewById<GridLayout>(R.id.admin_calendar_days)
        val label = content.findViewById<TextView>(R.id.admin_calendar_month)
        content.findViewById<TextView>(R.id.admin_previous_month).setOnClickListener {
            month = month.minusMonths(1)
            renderMonth(grid, label)
        }
        content.findViewById<TextView>(R.id.admin_next_month).setOnClickListener {
            month = month.plusMonths(1)
            renderMonth(grid, label)
        }
        renderMonth(grid, label)
        return AppCompatDialog(requireActivity(), R.style.AdminCalendarTheme).apply {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(content)
        }
    }

    private fun renderMonth(grid: GridLayout, label: TextView) {
        val locale = Locale.forLanguageTag("pt-BR")
        label.text = month.format(DateTimeFormatter.ofPattern("MMMM yyyy", locale))
            .replaceFirstChar { it.titlecase(locale) }
        grid.removeAllViews()
        for (initial in resources.getStringArray(R.array.admin_calendar_weekdays)) {
            grid.addView(dayCell(initial).apply { isClickable = false })
        }
        val offset = month.atDay(1).dayOfWeek.value % 7
        val first = month.atDay(1).minusDays(offset.toLong())
        repeat(42) { index ->
            val date = first.plusDays(index.toLong())
            grid.addView(dayCell(date.dayOfMonth.toString()).apply {
                contentDescription = date.format(DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", locale))
                val selected = date == LocalDate.of(2026, 9, 29)
                val highlighted = date == LocalDate.of(2026, 9, 25)
                setTextColor(when {
                    selected || highlighted -> Color.WHITE
                    date.month != month.month -> Color.LTGRAY
                    else -> Color.DKGRAY
                })
                if (selected || highlighted) {
                    setBackgroundResource(R.drawable.admin_calendar_selected)
                    if (highlighted) background.mutate().setTint(Color.rgb(0, 174, 246))
                }
                isFocusable = true
                setOnClickListener { dismiss() }
            })
        }
    }

    private fun dayCell(label: String) = TextView(requireContext()).apply {
        text = label
        textSize = 12f
        typeface = android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.NORMAL)
        gravity = Gravity.CENTER
        setTextColor(Color.DKGRAY)
        layoutParams = GridLayout.LayoutParams().apply {
            width = 0
            height = (36 * resources.displayMetrics.density).toInt()
            columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setBackgroundDrawable(Color.WHITE.toDrawable())
            setLayout(
                minOf(resources.displayMetrics.widthPixels - 2 * resources.getDimensionPixelSize(R.dimen.admin_gutter), (400 * resources.displayMetrics.density).toInt()),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(STATE_MONTH, month.toString())
        super.onSaveInstanceState(outState)
    }

    companion object {
        internal const val TAG = "admin_calendar"
        private const val STATE_MONTH = "month"
    }
}
