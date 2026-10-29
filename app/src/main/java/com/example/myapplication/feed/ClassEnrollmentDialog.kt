package com.example.myapplication.feed

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R

class ClassEnrollmentDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val key = requireArguments().getString("class_key")
        val leaving = requireArguments().getBoolean("leaving")
        val isPilates = key == "pilates"
        require(key == "jiu" || isPilates)
        val content = requireActivity().layoutInflater.inflate(R.layout.feed_enrollment_dialog, null)
        content.findViewById<android.widget.ImageView>(R.id.feed_enrollment_photo)
            .setImageResource(if (isPilates) R.drawable.feed_pilates else R.drawable.feed_jiu_jitsu)
        content.findViewById<TextView>(R.id.feed_enrollment_class)
            .setText(if (isPilates) R.string.feed_pilates else R.string.feed_jiu)
        content.findViewById<TextView>(R.id.feed_enrollment_teacher)
            .setText(if (isPilates) R.string.feed_pilates_teacher else R.string.feed_jiu_teacher)
        content.findViewById<TextView>(R.id.feed_enrollment_time)
            .setText(if (isPilates) R.string.feed_pilates_time else R.string.feed_jiu_time)
        content.findViewById<TextView>(R.id.feed_enrollment_title)
            .setText(if (leaving) R.string.feed_confirm_leave else R.string.feed_confirm_enrollment)
        val confirm = content.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.feed_enrollment_confirm)
        confirm.setText(if (leaving) R.string.feed_leave else R.string.feed_confirm)
        confirm.backgroundTintList = android.content.res.ColorStateList.valueOf(
            Color.parseColor(if (leaving) "#B93630" else "#3E46F4"))
        content.findViewById<View>(R.id.feed_enrollment_check).visibility = if (leaving) View.VISIBLE else View.GONE
        val scale = com.example.myapplication.AcessibilidadePrefs.getEscala(requireContext())
        scaleText(content, scale)
        content.findViewById<View>(R.id.feed_enrollment_cancel).setOnClickListener { dismiss() }
        content.findViewById<View>(R.id.feed_enrollment_confirm).setOnClickListener { button ->
            button.isEnabled = false
            requireContext().getSharedPreferences("feed_demo_classes", android.content.Context.MODE_PRIVATE)
                .edit().putBoolean(key, !leaving).apply()
            (activity as? FeedActivity)?.onClassEnrollmentChanged()
            Toast.makeText(requireContext(), if (leaving) R.string.feed_leave_success else R.string.feed_enrollment_success, Toast.LENGTH_SHORT).show()
            dismiss()
        }
        return AlertDialog.Builder(requireContext()).setView(content).create().apply {
            setCanceledOnTouchOutside(true)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val density = resources.displayMetrics.density
            val available = requireActivity().findViewById<View>(R.id.feed_root).let {
                it.width - it.paddingLeft - it.paddingRight
            }
            setLayout(minOf((340 * density).toInt(), available - (32 * density).toInt()),
                ViewGroup.LayoutParams.WRAP_CONTENT)
            addFlags(android.view.WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            setDimAmount(0.65f)
        }
    }

    private fun scaleText(view: View, scale: Float) {
        if (view is TextView) view.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.textSize * scale)
        if (view is ViewGroup) for (index in 0 until view.childCount) scaleText(view.getChildAt(index), scale)
    }

    companion object {
        const val TAG = "feed_class_enrollment"
        fun forClass(key: String, leaving: Boolean = false) = ClassEnrollmentDialog().apply {
            arguments = Bundle().apply { putString("class_key", key); putBoolean("leaving", leaving) }
        }
    }
}
