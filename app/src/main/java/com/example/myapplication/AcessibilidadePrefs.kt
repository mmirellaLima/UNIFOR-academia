package com.example.myapplication

import android.content.Context

object AcessibilidadePrefs {
    private const val ARQUIVO = "acessibilidade"
    private const val CHAVE_ESCALA = "escala_fonte"

    const val MINIMO = 0.8f
    const val MAXIMO = 1.6f
    const val PASSO = 0.1f

    fun getEscala(context: Context): Float =
        context.getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE)
            .getFloat(CHAVE_ESCALA, 1.0f)

    fun setEscala(context: Context, valor: Float) {
        context.getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE)
            .edit().putFloat(CHAVE_ESCALA, valor).apply()
    }
}