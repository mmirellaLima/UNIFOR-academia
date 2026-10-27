package com.example.myapplication.admin

import com.example.myapplication.R
import com.example.myapplication.navigation.*

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/** Presentation of the administrator feed and its prototype transitions. */
class FeedAdministradorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.admin_activity_feed)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }
        val root = findViewById<View>(R.id.admin_feed_root)
        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
        ViewCompat.requestApplyInsets(root)
        findViewById<View>(R.id.admin_back_button).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val next = findViewById<View>(R.id.admin_next_activity)
        next.findViewById<View>(R.id.admin_view_activity).setOnClickListener {
            showPanel(AdminActivityPreview.FUNCTIONAL, anchor = next)
        }
        val strength = findViewById<View>(R.id.admin_strength_activity)
        strength.contentDescription = getString(R.string.admin_open_activity, getString(R.string.admin_strength))
        strength.setOnClickListener { showPanel(AdminActivityPreview.STRENGTH) }
        val yoga = findViewById<View>(R.id.admin_yoga_activity)
        yoga.contentDescription = getString(R.string.admin_open_activity, getString(R.string.admin_yoga))
        yoga.setOnClickListener { showPanel(AdminActivityPreview.YOGA) }
        findViewById<View>(R.id.admin_add_activity).setOnClickListener { showPanel(create = true) }
        findViewById<View>(R.id.admin_nav_home).setOnClickListener {
            findViewById<androidx.core.widget.NestedScrollView>(R.id.admin_feed_scroll).smoothScrollTo(0, 0)
        }
        findViewById<View>(R.id.admin_nav_chat).setOnClickListener { AppNavigation.chat(this) }
        findViewById<View>(R.id.admin_nav_training).setOnClickListener { AppNavigation.workouts(this) }
        findViewById<View>(R.id.admin_nav_profile).setOnClickListener { AppNavigation.profile(this) }
        findViewById<View>(R.id.admin_nav_agenda).setOnClickListener {
            AppNavigation.pending(this, PendingDestination.ADMIN_AGENDA)
        }
        findViewById<View>(R.id.admin_accessibility_badge).setOnClickListener {
            AppNavigation.pending(this, PendingDestination.FONT_SIZE)
        }
        AppNavigation.dashboardBack(this)
    }

    private fun showPanel(
        preview: AdminActivityPreview = AdminActivityPreview.FUNCTIONAL,
        create: Boolean = false,
        anchor: View? = null
    ) {
        if (supportFragmentManager.findFragmentByTag(AdminActivityDialog.TAG) != null) return
        val position = IntArray(2)
        anchor?.getLocationOnScreen(position)
        AdminActivityDialog.newInstance(
            preview, create, if (anchor != null) position[1] else -1,
            findViewById<View>(R.id.admin_bottom_navigation).height
        ).show(supportFragmentManager, AdminActivityDialog.TAG)
    }
}
