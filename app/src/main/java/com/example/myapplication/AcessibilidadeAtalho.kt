package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.navigation.FontAccessibility

fun AppCompatActivity.abrirMenuAcessibilidade() {
    FontAccessibility.show(this)
}
