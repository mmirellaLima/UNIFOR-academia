package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.navigation.*

class MeusTreinosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        com.example.myapplication.navigation.AppNavigation.styleSystemBars(this)
        setContentView(R.layout.activity_meustreinos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.meusTreinos)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
        val session = DemoSession(this)
        findViewById<TextView>(R.id.tvNome).text = session.displayName
        findViewById<TextView>(R.id.tvMatricula).text = session.displayRegistration
        listOf(R.id.btnTreinoA to "A", R.id.btnTreinoB to "B", R.id.btnTreinoC to "C").forEach { (id, letter) ->
            findViewById<View>(id).setOnClickListener {
                AppNavigation.workout(this, "Treino $letter")
            }
        }
        findViewById<View>(R.id.btnVoltar).setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        LegacyNavigation.bind(this, 3)
    }
}
