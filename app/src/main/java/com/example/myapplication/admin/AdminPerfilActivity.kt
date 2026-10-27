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
import com.example.myapplication.navigation.PendingDestination

class AdminPerfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.admin_activity_perfil)

        DemoSession(this).enterDemo(AccessRole.ADMIN)

        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.admin_profile_root)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, 0, bars.right, bars.bottom)
            findViewById<View>(R.id.admin_profile_header).setPadding(0, bars.top, 0, 0)
            insets
        }

        val session = DemoSession(this)
        findViewById<TextView>(R.id.admin_user_name).text = session.displayName

        findViewById<View>(R.id.admin_back_button).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        findViewById<View>(R.id.admin_button_settings).setOnClickListener {
            AppNavigation.confirmLogout(this)
        }

        findViewById<View>(R.id.admin_button_salvar).setOnClickListener {
            AppNavigation.home(this)
        }

        findViewById<View>(R.id.admin_card_atividade_funcional).setOnClickListener {
            AdminActivityDialog.newInstance(AdminActivityPreview.FUNCTIONAL, false, -1, 0).show(
                supportFragmentManager, AdminActivityDialog.TAG
            )
        }

        findViewById<View>(R.id.admin_card_atividade_yoga).setOnClickListener {
            AdminActivityDialog.newInstance(AdminActivityPreview.YOGA, false, -1, 0).show(
                supportFragmentManager, AdminActivityDialog.TAG
            )
        }

        listOf(R.id.admin_ver_todas_atividades, R.id.admin_ver_todos_alunos, R.id.admin_card_gestao_alunos).forEach {
            findViewById<View>(it).setOnClickListener {
                startActivity(Intent(this, AdminWorkoutsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
            }
        }

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val homeNav = findViewById<ViewGroup>(R.id.admin_nav_home)
        (homeNav.getChildAt(0) as? ImageView)?.setColorFilter(ContextCompat.getColor(this, R.color.admin_text))
        (homeNav.getChildAt(1) as? TextView)?.setTextColor(ContextCompat.getColor(this, R.color.admin_text))

        val profileNav = findViewById<ViewGroup>(R.id.admin_nav_profile)
        (profileNav.getChildAt(0) as? ImageView)?.setColorFilter(ContextCompat.getColor(this, R.color.admin_navigation_active))
        (profileNav.getChildAt(1) as? TextView)?.setTextColor(ContextCompat.getColor(this, R.color.admin_navigation_active))

        findViewById<View>(R.id.admin_nav_home).setOnClickListener {
            startActivity(Intent(this, FeedAdministradorActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
        }
        findViewById<View>(R.id.admin_nav_agenda).setOnClickListener {
            startActivity(Intent(this, AdminAgendaActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
        }
        findViewById<View>(R.id.admin_nav_chat).setOnClickListener {
            AppNavigation.chat(this)
        }
        findViewById<View>(R.id.admin_nav_training).setOnClickListener {
            startActivity(Intent(this, AdminWorkoutsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
        }
    }
}
