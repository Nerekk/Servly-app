package com.example.servly_app.features._customer.job_create.presentation.main.components

import android.os.Parcelable
import com.example.servly_app.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class JobCategory(
    val id: Long,
    val name: String,
    val imageName: String
): Parcelable {
    fun getImageResource(): Int {
        return R.drawable.test_square_image
    }
}

val serviceCategories = listOf(
    JobCategory(1, "Elektryk", "Icon"),
    JobCategory(2, "Hydraulik", "Icon"),
    JobCategory(3, "Malarz", "Icon"),
    JobCategory(4, "Sprzątanie", "Icon"),
    JobCategory(5, "Mechanik", "Icon"),
    JobCategory(6, "Montaż", "Icon"),
    JobCategory(7, "Naprawy", "Icon"),
    JobCategory(8, "Inne", "Icon"),
)