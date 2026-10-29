package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.abrirMenuAcessibilidade() {
    MenuAcessibilidadeDialog().show(supportFragmentManager, "acessibilidade")
}