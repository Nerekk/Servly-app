package com.example.servly_app.features._customer.job_create.domain.usecase

import com.example.servly_app.features._customer.job_create.data.dtos.QuestionInfo
import com.example.servly_app.features._customer.job_create.domain.repository.CategoryRepository

class GetQuestions(private val categoryRepository: CategoryRepository) {
    suspend operator fun invoke(categoryId: Long): Result<List<QuestionInfo>> {
        return categoryRepository.getQuestions(categoryId)
    }
}