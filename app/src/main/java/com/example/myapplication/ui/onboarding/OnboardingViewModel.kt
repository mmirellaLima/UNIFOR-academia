package com.example.myapplication.ui.onboarding

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import com.example.myapplication.navigation.DemoSession

class OnboardingViewModel(application: Application) : AndroidViewModel(application) {
    private val session = DemoSession(application)
    var nome by mutableStateOf(if (session.hasDraft()) session.name else "")
        private set
    private var selectedAge by mutableIntStateOf(session.age)
    private var selectedWeight by mutableIntStateOf(session.weight)
    var idade: Int
        get() = selectedAge
        set(value) { selectedAge = value; session.age = value }
    var peso: Int
        get() = selectedWeight
        set(value) { selectedWeight = value; session.weight = value }
    fun register(fullName: String, studentId: String) {
        if (session.hasDraft() && session.registration == studentId) {
            session.name = fullName
            session.resumeRegistration()
        } else {
            session.beginRegistration(fullName, studentId)
        }
        nome = fullName
        selectedAge = session.age
        selectedWeight = session.weight
    }
}
