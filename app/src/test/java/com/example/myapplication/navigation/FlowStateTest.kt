package com.example.myapplication.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

class FlowStateTest {
    @Test fun inactiveAccessAlwaysReturnsToWelcome() {
        AccessRole.entries.forEach { role ->
            listOf(false, true).forEach { configured ->
                assertEquals(EntryDestination.WELCOME, FlowState(false, role, configured).entry())
            }
        }
    }
    @Test fun administratorNeverEntersStudentSetup() {
        listOf(false, true).forEach { configured ->
            assertEquals(EntryDestination.ADMIN_HOME, FlowState(true, AccessRole.ADMIN, configured).entry())
        }
    }
    @Test fun incompleteStudentResumesSetupInsteadOfOpeningTheFeed() {
        assertEquals(EntryDestination.SETUP, FlowState(true, AccessRole.STUDENT, false).entry())
    }
    @Test fun configuredStudentOpensStudentHome() {
        assertEquals(EntryDestination.STUDENT_HOME, FlowState(true, AccessRole.STUDENT, true).entry())
    }
}
