package com.example.servly_app.features._customer.job_create.domain.repository

import com.example.servly_app.core.util.LanguageUtils
import com.example.servly_app.features._customer.job_create.data.dtos.CategoryInfo
import com.example.servly_app.features._customer.job_create.data.dtos.QuestionInfo
import com.example.servly_app.features._customer.job_create.data.source.CategoryService

class CategoryRepository(private val categoryService: CategoryService) {
    suspend fun getCategories(): Result<List<CategoryInfo>> {
        return try {
            val response = categoryService.getCategories(LanguageUtils.getSystemLanguage())
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error fetching categories: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getQuestions(categoryId: Long): Result<List<QuestionInfo>> {
        return try {
            val response = categoryService.getQuestions(categoryId, LanguageUtils.getSystemLanguage())
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error fetching categories: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}