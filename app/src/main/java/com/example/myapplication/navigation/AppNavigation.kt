package com.example.myapplication.navigation

import android.app.Activity
import android.content.Intent
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.*
import com.example.myapplication.admin.FeedAdministradorActivity
import com.example.myapplication.admin.AdminPerfilActivity
import com.example.myapplication.admin.AdminAgendaActivity
import com.example.myapplication.admin.AdminWorkoutsActivity
import com.example.myapplication.feed.FeedActivity
import com.example.myapplication.onboarding.*
import com.example.myapplication.questionario2.*

/** The only bridge between the teammates' Compose screens and XML activities. */
object AppNavigation {
    const val EXTRA_RETAKE = "health_review"
    const val EXTRA_STEP = "setup_step"
    const val EXTRA_LOGIN = "show_login"

    fun styleSystemBars(activity: Activity) {
        androidx.core.view.WindowInsetsControllerCompat(activity.window, activity.window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }
    }

    fun open(activity: Activity, destination: Class<*>) {
        if (activity.javaClass == destination) return
        activity.startActivity(Intent(activity, destination).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
    }
    fun pending(activity: Activity, destination: PendingDestination, selection: String? = null) {
        activity.startActivity(Intent(activity, PendingActivity::class.java)
            .putExtra("destination", destination.name).putExtra("selection", selection))
    }
    fun home(activity: Activity) = open(activity, if (DemoSession(activity).role == AccessRole.ADMIN)
        FeedAdministradorActivity::class.java else FeedActivity::class.java)
    fun progress(activity: Activity) {
        if (DemoSession(activity).role == AccessRole.ADMIN) open(activity, AdminAgendaActivity::class.java)
        else open(activity, ProgressActivity::class.java)
    }
    fun workouts(activity: Activity) {
        if (DemoSession(activity).role == AccessRole.ADMIN) open(activity, AdminWorkoutsActivity::class.java)
        else open(activity, MeusTreinosActivity::class.java)
    }
    fun profile(activity: Activity) {
        if (DemoSession(activity).role == AccessRole.ADMIN) open(activity, AdminPerfilActivity::class.java)
        else open(activity, PerfilActivity::class.java)
    }
    fun goals(activity: Activity) = open(activity, ObjetivosActivity::class.java)
    fun chat(activity: Activity) = open(activity, ChatActivity::class.java)
    fun settings(activity: Activity) {
        if (DemoSession(activity).role == AccessRole.STUDENT) open(activity, ConfiguracoesActivity::class.java)
    }
    fun reviewHealth(activity: Activity) {
        DemoSession(activity).startHealthReview()
        activity.startActivity(Intent(activity, HealthActivity::class.java).putExtra(EXTRA_RETAKE, true))
    }
    fun terms(activity: Activity, retake: Boolean) {
        activity.startActivity(Intent(activity, TermosActivity::class.java).putExtra(EXTRA_RETAKE, retake))
    }
    fun finishReview(activity: Activity) {
        activity.startActivity(Intent(activity, ConfiguracoesActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP))
        activity.finish()
    }
    fun stepClass(step: SetupStep): Class<*> = when (step) {
        SetupStep.AGE, SetupStep.WEIGHT -> MainActivity::class.java
        SetupStep.HEIGHT -> AlturaActivity::class.java
        SetupStep.INTRO -> IntroActivity::class.java
        SetupStep.HEALTH -> HealthActivity::class.java
        SetupStep.TERMS -> TermosActivity::class.java
        SetupStep.SOURCE -> Questionario2Activity::class.java
        SetupStep.REASON -> ReasonActivity::class.java
        SetupStep.FREQUENCY -> FrequencyActivity::class.java
        SetupStep.INACTIVITY -> InactivityActivity::class.java
        SetupStep.EXPERIENCE -> ExperienceActivity::class.java
        SetupStep.INJURY -> InjuryActivity::class.java
        SetupStep.PAIN -> PainActivity::class.java
        SetupStep.HABITS -> HabitsActivity::class.java
        SetupStep.SURGERY -> SurgeryActivity::class.java
        SetupStep.COMPLETION -> CompletionActivity::class.java
        SetupStep.GOALS -> PendingActivity::class.java
    }
    fun setup(activity: Activity, step: SetupStep) {
        DemoSession(activity).step = step
        activity.startActivity(Intent(activity, stepClass(step)).putExtra(EXTRA_STEP, step.name)
            .apply { if (step == SetupStep.GOALS) putExtra("destination", PendingDestination.GOALS.name) })
    }
    fun resumeSetup(activity: Activity, step: SetupStep) {
        // Recreate the predecessors too, so Back works after process death or a cold start.
        val steps = if (step == SetupStep.AGE) listOf(step)
            else SetupStep.entries.drop(1).takeWhile { it.ordinal <= step.ordinal }
        val intents = steps.map {
            Intent(activity, stepClass(it)).putExtra(EXTRA_STEP, it.name).apply {
                if (it == SetupStep.GOALS) putExtra("destination", PendingDestination.GOALS.name)
            }
        }
        activity.startActivities(intents.toTypedArray())
    }
    fun logout(activity: Activity) {
        DemoSession(activity).logout()
        activity.startActivity(Intent(activity, MainActivity::class.java).putExtra(EXTRA_LOGIN, true)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        activity.finish()
    }
    fun confirmLogout(activity: Activity) {
        AlertDialog.Builder(activity).setTitle(R.string.flow_logout_title)
            .setNegativeButton(R.string.flow_cancel, null)
            .setPositiveButton(R.string.flow_logout) { _, _ -> logout(activity) }.show()
    }
    fun dashboardBack(activity: AppCompatActivity) {
        activity.onBackPressedDispatcher.addCallback(activity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() = confirmLogout(activity)
        })
    }
}
