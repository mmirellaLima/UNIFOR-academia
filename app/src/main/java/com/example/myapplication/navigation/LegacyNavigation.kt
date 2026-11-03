package com.example.myapplication.navigation

import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.R

/** Barra compartilhada pelas telas XML e pela tela de progresso em Compose. */
object LegacyNavigation {

    fun bind(
        activity: AppCompatActivity,
        selected: Int,
        barId: Int = R.id.barraInferior
    ) {
        val bar = activity.findViewById<ViewGroup>(barId) ?: return
        val administrator = DemoSession(activity).role == AccessRole.ADMIN

        bindBar(
            bar,
            selected,
            listOf(
                { AppNavigation.home(activity) },
                { AppNavigation.progress(activity) },
                { AppNavigation.chat(activity) },
                { AppNavigation.workouts(activity) },
                { AppNavigation.profile(activity) }
            ),
            administrator
        )

        FontAccessibility.bind(activity)
    }

    fun bindBar(
        bar: ViewGroup,
        selected: Int,
        actions: List<() -> Unit>,
        administrator: Boolean = false
    ) {
        require(actions.size == 5 && selected in 0..4)

        val labels = listOf(
            bar.context.getString(R.string.feed_home),
            bar.context.getString(
                if (administrator) R.string.flow_agenda
                else R.string.feed_progress
            ),
            bar.context.getString(R.string.flow_chat),
            bar.context.getString(R.string.feed_training),
            bar.context.getString(R.string.feed_profile)
        )

        for (index in 0 until minOf(5, bar.childCount)) {
            val item = bar.getChildAt(index)

            item.setOnClickListener { actions[index]() }
            item.isSelected = index == selected
            item.contentDescription = labels[index]

            if (index == 2) {
                (item as? ImageView)?.apply {
                    setImageResource(R.drawable.feed_unifor_icon)
                    imageTintList = null
                }
            } else {
                val color = ContextCompat.getColor(
                    bar.context,
                    if (index == selected) R.color.navigation_selected
                    else R.color.navigation_default
                )

                tint(item, color, labels[index], administrator && index == 1)
            }
        }
    }

    private fun tint(
        view: View,
        color: Int,
        label: String,
        agenda: Boolean
    ) {
        when (view) {
            is TextView -> {
                view.setTextColor(color)
                view.text = label
            }

            is ImageView -> {
                if (agenda) {
                    view.setImageResource(R.drawable.admin_ic_agenda)
                }

                view.imageTintList = ColorStateList.valueOf(color)
                view.importantForAccessibility =
                    View.IMPORTANT_FOR_ACCESSIBILITY_NO
            }

            is ViewGroup -> {
                for (index in 0 until view.childCount) {
                    tint(view.getChildAt(index), color, label, agenda)
                }
            }
        }
    }
}