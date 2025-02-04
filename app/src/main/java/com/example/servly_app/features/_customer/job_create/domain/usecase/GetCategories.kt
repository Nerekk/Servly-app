package com.example.servly_app.features._customer.job_create.domain.usecase

import com.example.servly_app.features._customer.job_create.data.dtos.CategoryInfo
import com.example.servly_app.features._customer.job_create.domain.repository.CategoryRepository

class GetCategories(private val categoryRepository: CategoryRepository) {
    suspend operator fun invoke(): Result<List<CategoryInfo>> {
        return categoryRepository.getCategories()
    }
}