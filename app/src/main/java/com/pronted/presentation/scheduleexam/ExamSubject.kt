package com.pronted.presentation.scheduleexam

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.pronted.R

enum class ExamSubject(
    val subject: String,
    @DrawableRes val src: Int,
    @ColorRes val backgroundColor: Int
) {
    TELUGU("Telugu", R.drawable.timetable_telugu, R.color.timetable_telugu),
    HINDI("Hindi", R.drawable.timetable_hindi, R.color.timetable_hindi),
    ENGLISH("English", R.drawable.timetable_english, R.color.timetable_english),
    MATHS("Mathematics", R.drawable.timetable_math, R.color.timetable_math),
    SCIENCE("Science", R.drawable.timetable_science, R.color.timetable_science),
    ENVIRONMENTAL_STUDIES(
        "Environmental Science",
        R.drawable.timetable_environmental_study,
        R.color.timetable_environmental
    ),
    BIOLOGY("Biology", R.drawable.timetable_bio, R.color.timetable_bio),
    PHYSICS("Physics", R.drawable.timetable_physics, R.color.timetable_physics),
    SOCIAL_STUDIES(
        "Social Studies",
        R.drawable.timetable_social_study,
        R.color.timetable_socialstudy
    ),
    CIVICS("Civics", R.drawable.timetable_civics, R.color.timetable_civics),
    COMMERCE("Commerce", R.drawable.timetable_commerce, R.color.timetable_commerce),
    ECONOMICS("Economics", R.drawable.timetable_economics, R.color.timetable_economics),
    CHEMISTRY("Chemsitry", R.drawable.timetable_chemistry, R.color.timetable_chemistry),
    OTHER("Other", R.drawable.timetableother, R.color.timetable_other),
}