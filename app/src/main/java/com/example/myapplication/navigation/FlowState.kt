package com.example.myapplication.navigation

/** UI demonstration only. No credentials or server authentication are stored. */
enum class AccessRole { STUDENT, ADMIN }
enum class SetupStep {
    AGE, WEIGHT, HEIGHT, INTRO, HEALTH, TERMS, SOURCE, REASON, FREQUENCY,
    INACTIVITY, EXPERIENCE, INJURY, PAIN, HABITS, SURGERY, COMPLETION, GOALS
}
enum class EntryDestination { WELCOME, STUDENT_HOME, ADMIN_HOME, SETUP }
data class FlowState(val active: Boolean, val role: AccessRole, val configured: Boolean) {
    fun entry(): EntryDestination = when {
        !active -> EntryDestination.WELCOME
        role == AccessRole.ADMIN -> EntryDestination.ADMIN_HOME
        configured -> EntryDestination.STUDENT_HOME
        else -> EntryDestination.SETUP
    }
}
enum class PendingDestination {
    RECOVERY, GOALS, WORKOUT, EXERCISE, FONT_SIZE, PERSONAL_DATA,
    NOTIFICATIONS, CHANGE_PASSWORD, EDIT_EMAIL, ADMIN_AGENDA, ADMIN_WORKOUTS, ADMIN_PROFILE
}
