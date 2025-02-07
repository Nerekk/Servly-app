package com.example.servly_app.features._customer.job_create.data.dtos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionAnswer(
    val id: Long,
    val answer: String = ""
): Parcelable
