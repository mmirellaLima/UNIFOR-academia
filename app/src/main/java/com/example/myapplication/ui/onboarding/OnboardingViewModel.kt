package com.example.myapplication.ui.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class OnboardingViewModel : ViewModel() {
    var nome by mutableStateOf("")
    var idade by mutableIntStateOf(25)
    var peso by mutableIntStateOf(65)
}