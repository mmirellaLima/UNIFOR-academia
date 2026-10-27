package com.example.myapplication.admin

import com.example.myapplication.R

import androidx.annotation.StringRes

/** Sample content from the prototype; this screen does not create or update activities. */
internal enum class AdminActivityPreview(
    @param:StringRes val title: Int,
    @param:StringRes val time: Int,
    @param:StringRes val students: Int,
    @param:StringRes val room: Int,
    @param:StringRes val teacher: Int,
    @param:StringRes val capacity: Int
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
    YOGA(
        R.string.admin_yoga, R.string.admin_time_yoga,
        R.string.admin_students_yoga, R.string.admin_room_yoga,
        R.string.admin_teacher_laura, R.string.admin_capacity_yoga
    )
}
