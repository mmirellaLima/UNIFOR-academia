package com.example.myapplication.navigation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R

/** Reserved destination. The responsible teammate supplies its content later. */
class PendingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        com.example.myapplication.navigation.AppNavigation.styleSystemBars(this)
        setContentView(R.layout.flow_activity_pending)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.flow_pending_root)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
        findViewById<android.view.View>(R.id.flow_back).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
    override fun onResume() {
        super.onResume()
        if (intent.getStringExtra(AppNavigation.EXTRA_STEP) == SetupStep.GOALS.name) {
            DemoSession(this).step = SetupStep.GOALS
        }
    }
}
