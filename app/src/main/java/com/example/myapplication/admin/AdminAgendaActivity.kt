package com.example.myapplication.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.myapplication.ChatActivity
import com.example.myapplication.R
import com.example.myapplication.navigation.AccessRole
import com.example.myapplication.navigation.AppNavigation
import com.example.myapplication.navigation.DemoSession

class AdminAgendaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_agenda)

        DemoSession(this).enterDemo(AccessRole.ADMIN)

        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        val root = findViewById<View>(R.id.feed_root)
        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
        ViewCompat.requestApplyInsets(root)

        findViewById<View>(R.id.back_button).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        bindActivity(R.id.agenda_today_strength, AdminActivityPreview.STRENGTH)
        bindActivity(R.id.agenda_today_yoga, AdminActivityPreview.YOGA)
        bindActivity(R.id.agenda_tomorrow_strength, AdminActivityPreview.STRENGTH_TOMORROW)
        bindActivity(R.id.agenda_tomorrow_yoga, AdminActivityPreview.YOGA_TOMORROW)

        for (id in intArrayOf(R.id.agenda_add_today, R.id.agenda_add_tomorrow)) {
            findViewById<View>(id)?.setOnClickListener {
                showPanel(AdminActivityPreview.FUNCTIONAL, create = true)
            }
        }

        findViewById<View>(R.id.accessibility_action)?.setOnClickListener {
            val content = findViewById<View>(R.id.screen_content)
            val enlarged = !content.isSelected
            content.isSelected = enlarged
            resizeText(content, if (enlarged) 1.2f else 1f / 1.2f)
        }

        setupBottomNavigation()
    }

    private fun bindActivity(id: Int, preview: AdminActivityPreview) {
        findViewById<View>(id)?.apply {
            findViewById<TextView>(R.id.admin_time)?.setText(preview.time)
            findViewById<TextView>(R.id.admin_students)?.setText(preview.students)
            contentDescription = getString(R.string.admin_open_activity, getString(preview.title))
            setOnClickListener { showPanel(preview) }
        }
    }

    private fun showPanel(preview: AdminActivityPreview, create: Boolean = false) {
        if (supportFragmentManager.findFragmentByTag(AdminActivityDialog.TAG) != null) return
        AdminActivityDialog.newInstance(preview, create, -1,
            findViewById<View>(R.id.bottom_navigation)?.height ?: 0
        ).show(supportFragmentManager, AdminActivityDialog.TAG)
    }

    private fun setupBottomNavigation() {
        val nav = findViewById<View>(R.id.bottom_navigation) ?: return

        // Unhighlight home tab
        val homeNav = nav.findViewById<ViewGroup>(R.id.admin_nav_home)
        (homeNav?.getChildAt(0) as? ImageView)?.setColorFilter(ContextCompat.getColor(this, R.color.admin_text))
        (homeNav?.getChildAt(1) as? TextView)?.setTextColor(ContextCompat.getColor(this, R.color.admin_text))

        // Highlight agenda tab
        val agendaNav = nav.findViewById<ViewGroup>(R.id.admin_nav_agenda)
        (agendaNav?.getChildAt(0) as? ImageView)?.setColorFilter(ContextCompat.getColor(this, R.color.admin_navigation_active))
        (agendaNav?.getChildAt(1) as? TextView)?.setTextColor(ContextCompat.getColor(this, R.color.admin_navigation_active))

        homeNav?.setOnClickListener {
            startActivity(Intent(this, FeedAdministradorActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
        }
        nav.findViewById<View>(R.id.admin_nav_chat)?.setOnClickListener {
            AppNavigation.chat(this)
        }
        nav.findViewById<View>(R.id.admin_nav_training)?.setOnClickListener {
            startActivity(Intent(this, AdminWorkoutsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
        }
        nav.findViewById<View>(R.id.admin_nav_profile)?.setOnClickListener {
            startActivity(Intent(this, AdminPerfilActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
        }
    }

    private fun resizeText(view: View, factor: Float) {
        if (view is TextView) {
            view.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, view.textSize * factor)
        } else if (view is ViewGroup) {
            for (index in 0 until view.childCount) resizeText(view.getChildAt(index), factor)
        }
    }
}
