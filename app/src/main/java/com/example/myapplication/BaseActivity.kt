package com.example.myapplication

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.AcessibilidadePrefs

open class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        val config = Configuration(newBase.resources.configuration)
        config.fontScale = AcessibilidadePrefs.getEscala(newBase)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }
}