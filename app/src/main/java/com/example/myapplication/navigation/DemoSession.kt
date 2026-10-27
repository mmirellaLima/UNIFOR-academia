package com.example.myapplication.navigation

import android.content.Context

/** Shared local presentation state; deliberately excludes passwords. */
class DemoSession(context: Context) {
    private val appContext = context.applicationContext
    private val prefs = appContext.getSharedPreferences("navigation_demo", Context.MODE_PRIVATE)
    var active: Boolean
        get() = prefs.getBoolean("active", false)
        private set(value) { prefs.edit().putBoolean("active", value).apply() }
    var role: AccessRole
        get() = AccessRole.entries.firstOrNull { it.name == prefs.getString("role", null) } ?: AccessRole.STUDENT
        private set(value) { prefs.edit().putString("role", value.name).apply() }
    var configured: Boolean
        get() = prefs.getBoolean("configured", false)
        private set(value) { prefs.edit().putBoolean("configured", value).apply() }
    var step: SetupStep
        get() = SetupStep.entries.firstOrNull { it.name == prefs.getString("step", null) } ?: SetupStep.AGE
        set(value) { prefs.edit().putString("step", value.name).apply() }
    var name: String
        get() = prefs.getString("name", "Lucas Vasconcelos").orEmpty()
        set(value) { prefs.edit().putString("name", value).apply() }
    var registration: String
        get() = prefs.getString("registration", "2512000").orEmpty()
        set(value) { prefs.edit().putString("registration", value).apply() }
    var age: Int
        get() = prefs.getInt("age", 25)
        set(value) { prefs.edit().putInt("age", value).apply() }
    var weight: Int
        get() = prefs.getInt("weight", 78)
        set(value) { prefs.edit().putInt("weight", value).apply() }
    var height: Int
        get() = prefs.getInt("height", 175)
        set(value) { prefs.edit().putInt("height", value).apply() }
    var healthIndex: Int
        get() = prefs.getInt("health_index", 0).coerceIn(0, 9)
        set(value) { prefs.edit().putInt("health_index", value).apply() }
    var termsAccepted: Boolean
        get() = prefs.getBoolean("terms", false)
        set(value) { prefs.edit().putBoolean("terms", value).apply() }

    fun state() = FlowState(active, role, configured)
    fun beginRegistration(fullName: String, studentId: String) {
        listOf("Questionario2", "Reason", "Frequency", "Inactivity", "Experience", "Injury",
            "Pain", "Habits", "Surgery", "Completion").forEach {
            appContext.getSharedPreferences("form_" + it + "Activity", Context.MODE_PRIVATE).edit().clear().apply()
        }
        prefs.edit().clear().putString("name", fullName).putString("registration", studentId)
            .putInt("weight", 65).putInt("height", 170).putBoolean("has_draft", true).apply()
        active = true
        role = AccessRole.STUDENT
        configured = false
        step = SetupStep.AGE
    }
    fun hasDraft() = prefs.getBoolean("has_draft", false)
    fun resumeRegistration() {
        active = true
        role = AccessRole.STUDENT
        configured = false
    }
    fun enterDemo(selectedRole: AccessRole) {
        // Keep an unfinished registration separately from the configured demonstration account.
        role = selectedRole
        active = true
        configured = true
    }
    val displayName: String
        get() = if (role == AccessRole.ADMIN) "Amanda Alves" else if (configured) "Lucas Vasconcelos" else name
    val displayRegistration: String get() = if (configured) "2512000" else registration
    val displayWeight: Int get() = if (configured) 78 else weight
    val displayHeight: Int get() = if (configured) 175 else height
    fun answer(index: Int, retake: Boolean = false): String =
        prefs.getString("${if (retake) "review" else "health"}_$index", "").orEmpty()
    fun saveAnswer(index: Int, value: String, retake: Boolean = false) {
        prefs.edit().putString("${if (retake) "review" else "health"}_$index", value).apply()
    }
    fun startHealthReview() {
        val edit = prefs.edit().putBoolean("review_terms", false)
        repeat(10) { edit.remove("review_$it") }
        edit.apply()
    }
    fun completeHealthReview() {
        repeat(10) { saveAnswer(it, answer(it, retake = true)) }
        reviewTerms = true
    }
    var reviewTerms: Boolean
        get() = prefs.getBoolean("review_terms", false)
        set(value) { prefs.edit().putBoolean("review_terms", value).apply() }
    fun logout() { active = false }
}
