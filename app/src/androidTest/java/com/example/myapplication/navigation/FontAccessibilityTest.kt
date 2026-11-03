package com.example.myapplication.navigation

import android.content.Context
import android.content.Intent
import android.view.View
import kotlin.math.roundToInt
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FontAccessibilityTest {
    @Test
    fun allTenAccessibilityButtonsOpenAndPersistTheFontSlider() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val preferences = context.getSharedPreferences("acessibilidade", Context.MODE_PRIVATE)
        val hadValue = preferences.contains("escala_fonte")
        val previous = preferences.getFloat("escala_fonte", 1f)
        val screens = listOf(
            "feed.FeedActivity" to R.id.feed_font,
            "ConfiguracoesActivity" to R.id.btnFonte,
            "ObjetivosActivity" to R.id.btnAcessibilidade,
            "DadosPessoaisActivity" to R.id.btnFonte,
            "EditarEmailActivity" to R.id.btnFonte,
            "AlterarSenhaActivity" to R.id.btnFonte,
            "NotificacoesActivity" to R.id.btnFonte,
            "admin.FeedAdministradorActivity" to R.id.admin_accessibility_badge,
            "admin.AdminAgendaActivity" to R.id.accessibility_action,
            "admin.AdminWorkoutsActivity" to R.id.accessibility_action
        )
        try {
            screens.forEach { (name, anchorId) ->
                preferences.edit().putFloat("escala_fonte", 1f).commit()
                val intent = Intent().setClassName(context, "com.example.myapplication.$name")
                ActivityScenario.launch<AppCompatActivity>(intent).use { scenario ->
                    scenario.onActivity { activity ->
                        val button = activity.findViewById<View>(anchorId)
                        assertTrue("Botão não visível: $name", button.isShown)
                        button.performClick()
                    }
                    onView(withId(R.id.feed_font_slider)).check(matches(isDisplayed()))
                    onView(withId(R.id.feed_font_slider)).perform(swipeRight())
                    val saved = preferences.getFloat("escala_fonte", 1f)
                    assertTrue("Tamanho não foi salvo: $name", saved > 1f)
                    onView(withId(R.id.feed_font_slider)).check { view, error ->
                        if (error != null) throw error
                        assertEquals("Porcentagem acessível incorreta: $name",
                            context.getString(R.string.feed_font_percentage, (saved * 100).roundToInt()),
                            androidx.core.view.ViewCompat.getStateDescription(view).toString())
                    }
                    onView(withId(R.id.feed_font_close)).perform(click())
                    scenario.recreate()
                    scenario.onActivity { it.findViewById<View>(anchorId).performClick() }
                    onView(withId(R.id.feed_font_slider)).check { view, error ->
                        if (error != null) throw error
                        assertEquals("Preferência não foi restaurada: $name", (saved * 100).roundToInt() - 80, (view as SeekBar).progress)
                    }
                    onView(withId(R.id.feed_font_close)).perform(click())
                }
            }
        } finally {
            preferences.edit().apply {
                if (hadValue) putFloat("escala_fonte", previous) else remove("escala_fonte")
            }.commit()
        }
    }
}
