package com.example.servly_app.features._customer.job_create.data.dtos

import com.example.servly_app.features._customer.job_create.presentation.main.components.JobCategory

data class CategoryInfo(
    val id: Long,
    val name: String,
    val icon: String
) {
    fun toJobCategory(): JobCategory {
        return JobCategory(
            id = id,
            name = name,
            imageName = icon
        )
    }
}