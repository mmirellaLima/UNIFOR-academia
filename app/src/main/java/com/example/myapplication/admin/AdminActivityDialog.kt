package com.example.myapplication.admin

import com.example.myapplication.R

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.appcompat.widget.ListPopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible

/** Compact activity panels, anchored in the same positions as the reference images. */
class AdminActivityDialog : AppCompatDialogFragment() {
    private var panel: View? = null
    private var selector: ListPopupWindow? = null
    private val createMode get() = arguments?.getBoolean(ARG_CREATE) == true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View =
        inflater.inflate(R.layout.admin_dialog_activity, container, false).also { panel = it }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.clipToOutline = true
        val sample = AdminActivityPreview.entries.firstOrNull {
            it.name == arguments?.getString(ARG_PREVIEW)
        } ?: AdminActivityPreview.FUNCTIONAL
        val title = view.findViewById<EditText>(R.id.admin_title)
        val displayTitle = view.findViewById<TextView>(R.id.admin_activity_title)
        val time = view.findViewById<TextView>(R.id.admin_time)
        val teacher = view.findViewById<TextView>(R.id.admin_teacher)
        val capacity = view.findViewById<TextView>(R.id.admin_capacity)
        val teacherSelector = view.findViewById<TextView>(R.id.admin_teacher_selector)
        val capacitySelector = view.findViewById<TextView>(R.id.admin_capacity_selector)
        val statusSelector = view.findViewById<TextView>(R.id.admin_status_selector)
        view.findViewById<TextView>(R.id.admin_heading).setText(
            if (createMode) R.string.admin_add_activity_heading else R.string.admin_next_activity
        )
        title.isVisible = createMode
        displayTitle.isVisible = !createMode
        (time.layoutParams as ConstraintLayout.LayoutParams).apply {
            startToEnd = if (createMode) R.id.admin_title else R.id.admin_activity_title
        }
        displayTitle.setText(sample.title)
        title.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.admin_ic_edit, 0)
        time.setText(if (createMode) R.string.admin_time else sample.time)
        val stats = view.findViewById<View>(R.id.admin_stats)
        stats.findViewById<TextView>(R.id.admin_students).setText(if (createMode) R.string.admin_placeholder else sample.students)
        stats.findViewById<TextView>(R.id.admin_duration).setText(if (createMode) R.string.admin_placeholder else R.string.admin_one_hour)
        stats.findViewById<TextView>(R.id.admin_room).setText(if (createMode) R.string.admin_placeholder else sample.room)
        teacher.setText(sample.teacher)
        teacher.isVisible = !createMode
        capacity.setText(sample.capacity)
        capacity.isVisible = !createMode
        capacitySelector.isVisible = createMode
        teacherSelector.setText(if (createMode) R.string.admin_add_teacher else R.string.admin_substitute_teacher)
        val cancel = view.findViewById<TextView>(R.id.admin_cancel)
        cancel.setText(if (createMode) R.string.admin_cancel else R.string.admin_cancel_activity)
        cancel.setOnClickListener { dismiss() }
        val primary = view.findViewById<TextView>(R.id.admin_primary_action)
        primary.setText(if (createMode) R.string.admin_add_activity else R.string.admin_collapse_activity)
        primary.setOnClickListener { dismiss() }
        teacherSelector.setOnClickListener { showSelector(teacherSelector, R.array.admin_teachers) }
        capacitySelector.setOnClickListener { showSelector(capacitySelector, R.array.admin_capacities) }
        statusSelector.setOnClickListener { showSelector(statusSelector, R.array.admin_statuses) }
        if (createMode) {
            time.isClickable = true
            time.isFocusable = true
            time.setOnClickListener { showSelector(time, R.array.admin_times, calendar = true) }
        }
        savedInstanceState?.let { state ->
            for (id in SELECTOR_IDS) {
                val field = view.findViewById<TextView>(id)
                field.text = state.getCharSequence(id.toString(), field.text)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window ?: return
        val view = panel ?: return
        val metrics = resources.displayMetrics
        val bars = ViewCompat.getRootWindowInsets(requireActivity().window.decorView)
            ?.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
        val gutter = resources.getDimensionPixelSize(R.dimen.admin_gutter)
        val width = minOf(metrics.widthPixels - (bars?.left ?: 0) - (bars?.right ?: 0) - gutter * 2, dp(480))
        val navigation = arguments?.getInt(ARG_NAVIGATION) ?: dp(56)
        val anchor = arguments?.getInt(ARG_ANCHOR, -1) ?: -1
        val top = if (!createMode && anchor >= 0) (anchor - (bars?.top ?: 0)).coerceAtLeast(0) else 0
        val height = metrics.heightPixels - (bars?.top ?: 0) - (bars?.bottom ?: 0) - navigation - top - dp(8)
        view.measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        window.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        window.setDimAmount(0.6f)
        window.setLayout(width, minOf(view.measuredHeight, height.coerceAtLeast(dp(120))))
        window.attributes = window.attributes.apply {
            gravity = when {
                createMode -> Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                anchor >= 0 -> Gravity.TOP or Gravity.CENTER_HORIZONTAL
                else -> Gravity.CENTER
            }
            y = if (createMode) navigation + dp(8) else top
            windowAnimations = R.style.AdminPanelAnimation
        }
    }

    private fun showSelector(anchor: TextView, @ArrayRes choices: Int, calendar: Boolean = false) {
        selector?.dismiss()
        val options = resources.getStringArray(choices)
        selector = ListPopupWindow(requireContext()).apply {
            setAdapter(ArrayAdapter(requireContext(), R.layout.admin_popup_item, options))
            anchorView = anchor
            width = maxOf(anchor.width, dp(120))
            isModal = true
            inputMethodMode = PopupWindow.INPUT_METHOD_NOT_NEEDED
            setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.admin_popup_background))
            setOnItemClickListener { _, _, position, _ ->
                anchor.text = options[position]
                dismiss()
                if (calendar && childFragmentManager.findFragmentByTag(AdminCalendarDialog.TAG) == null) {
                    AdminCalendarDialog().show(childFragmentManager, AdminCalendarDialog.TAG)
                }
            }
            show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        panel?.let { root ->
            for (id in SELECTOR_IDS) outState.putCharSequence(id.toString(), root.findViewById<TextView>(id).text)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        selector?.dismiss()
        selector = null
        panel = null
        super.onDestroyView()
    }

    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()

    companion object {
        internal const val TAG = "admin_activity_panel"
        private const val ARG_CREATE = "create"
        private const val ARG_PREVIEW = "preview"
        private const val ARG_ANCHOR = "anchor"
        private const val ARG_NAVIGATION = "navigation"
        private val SELECTOR_IDS = intArrayOf(R.id.admin_teacher_selector, R.id.admin_capacity_selector, R.id.admin_status_selector, R.id.admin_time)

        internal fun newInstance(sample: AdminActivityPreview, create: Boolean, anchor: Int, navigation: Int) =
            AdminActivityDialog().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_CREATE, create)
                    putString(ARG_PREVIEW, sample.name)
                    putInt(ARG_ANCHOR, anchor)
                    putInt(ARG_NAVIGATION, navigation)
                }
            }
    }
}
