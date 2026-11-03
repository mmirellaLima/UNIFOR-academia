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
import com.example.myapplication.R
import com.example.myapplication.navigation.AccessRole
import com.example.myapplication.navigation.AppNavigation
import com.example.myapplication.navigation.DemoSession

class AdminWorkoutsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_workouts)

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

        com.example.myapplication.navigation.FontAccessibility.bind(this, R.id.accessibility_action)

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val nav = findViewById<View>(R.id.bottom_navigation) ?: return

        // Unhighlight home tab
        val homeNav = nav.findViewById<ViewGroup>(R.id.admin_nav_home)
        (homeNav?.getChildAt(0) as? ImageView)?.setColorFilter(ContextCompat.getColor(this, R.color.admin_text))
        (homeNav?.getChildAt(1) as? TextView)?.setTextColor(ContextCompat.getColor(this, R.color.admin_text))

        // Highlight training tab
        val trainingNav = nav.findViewById<ViewGroup>(R.id.admin_nav_training)
        (trainingNav?.getChildAt(0) as? ImageView)?.setColorFilter(ContextCompat.getColor(this, R.color.admin_navigation_active))
        (trainingNav?.getChildAt(1) as? TextView)?.setTextColor(ContextCompat.getColor(this, R.color.admin_navigation_active))

        homeNav?.setOnClickListener {
            startActivity(Intent(this, FeedAdministradorActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
        }
        nav.findViewById<View>(R.id.admin_nav_agenda)?.setOnClickListener {
            startActivity(Intent(this, AdminAgendaActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
        }
        nav.findViewById<View>(R.id.admin_nav_chat)?.setOnClickListener {
            AppNavigation.chat(this)
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
