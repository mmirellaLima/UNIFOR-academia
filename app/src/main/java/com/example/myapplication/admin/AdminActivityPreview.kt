package com.example.myapplication.admin

import androidx.annotation.StringRes
import com.example.myapplication.R

/** Sample content from the prototype; this screen does not create or update activities. */
internal enum class AdminActivityPreview(
    @StringRes val title: Int,
    @StringRes val time: Int,
    @StringRes val students: Int,
    @StringRes val room: Int,
    @StringRes val teacher: Int,
    @StringRes val capacity: Int
) {
    FUNCTIONAL(
        R.string.admin_functional, R.string.admin_time_functional,
        R.string.admin_students_functional, R.string.admin_room_functional,
        R.string.admin_teacher_lorena, R.string.admin_capacity_functional
    ),
    STRENGTH(
        R.string.admin_strength, R.string.admin_time_strength,
        R.string.admin_students_strength, R.string.admin_room_strength,
        R.string.admin_teacher_joao, R.string.admin_capacity_strength
    ),
    STRENGTH_TOMORROW(
        R.string.admin_strength, R.string.admin_time_strength_tomorrow,
        R.string.admin_students_strength_tomorrow, R.string.admin_room_strength,
        R.string.admin_teacher_joao, R.string.admin_capacity_strength_tomorrow
    ),
    YOGA_TOMORROW(
        R.string.admin_yoga, R.string.admin_time_yoga_tomorrow,
        R.string.admin_students_yoga_tomorrow, R.string.admin_room_yoga,
        R.string.admin_teacher_laura, R.string.admin_capacity_yoga_tomorrow
    ),
    YOGA(
        R.string.admin_yoga, R.string.admin_time_yoga,
        R.string.admin_students_yoga, R.string.admin_room_yoga,
        R.string.admin_teacher_laura, R.string.admin_capacity_yoga
    )
}
