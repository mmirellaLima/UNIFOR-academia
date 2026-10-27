package com.example.myapplication.navigation

import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.R

/** Connects the existing five-item native bar without replacing the delivered views. */
object LegacyNavigation {
    fun bind(activity: AppCompatActivity, selected: Int) {
        val bar = activity.findViewById<ViewGroup>(R.id.barraInferior) ?: return
        val actions = listOf<() -> Unit>(
            { AppNavigation.home(activity) }, { AppNavigation.progress(activity) },
            { AppNavigation.chat(activity) }, { AppNavigation.workouts(activity) },
            { AppNavigation.profile(activity) }
        )
        val administrator = DemoSession(activity).role == AccessRole.ADMIN
        val labels = listOf(activity.getString(R.string.feed_home),
            activity.getString(if (administrator) R.string.flow_agenda else R.string.feed_progress),
            activity.getString(R.string.flow_chat), activity.getString(R.string.feed_training),
            activity.getString(R.string.feed_profile))
        for (index in 0 until minOf(5, bar.childCount)) {
            val item = bar.getChildAt(index)
            item.setOnClickListener { actions[index]() }
            item.isSelected = index == selected
            item.contentDescription = labels[index]
            if (index == 2) {
                (item as? ImageView)?.setImageResource(R.drawable.logo_chat)
            } else {
                val color = ContextCompat.getColor(activity,
                    if (index == selected) R.color.azul_selecionado else android.R.color.white)
                tint(item, color, labels[index], administrator && index == 1)
            }
        }
        activity.findViewById<View>(R.id.btnFonte)?.setOnClickListener {
            AppNavigation.pending(activity, PendingDestination.FONT_SIZE)
        }
    }
    private fun tint(view: View, color: Int, label: String, agenda: Boolean) {
        when (view) {
            is TextView -> { view.setTextColor(color); view.text = label }
            is ImageView -> {
                if (agenda) view.setImageResource(R.drawable.admin_ic_agenda)
                view.imageTintList = ColorStateList.valueOf(color)
                view.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
            }
            is ViewGroup -> for (index in 0 until view.childCount) tint(view.getChildAt(index), color, label, agenda)
        }
    }
}
