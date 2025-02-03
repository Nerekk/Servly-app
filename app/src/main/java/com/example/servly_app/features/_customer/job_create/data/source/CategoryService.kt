package com.example.servly_app.features._customer.job_create.data.source

import com.example.servly_app.core.data.util.ControllerMappings
import com.example.servly_app.features._customer.job_create.data.dtos.CategoryInfo
import com.example.servly_app.features._customer.job_create.data.dtos.QuestionInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryService {
    @GET("api/${ControllerMappings.CATEGORY}")
    suspend fun getCategories(@Query("languageCode") languageCode: String): Response<List<CategoryInfo>>

    @GET("api/${ControllerMappings.CATEGORY}/questions/{categoryId}")
    suspend fun getQuestions(
        @Path("categoryId") categoryId: Long,
        @Query("languageCode") languageCode: String
    ): Response<List<QuestionInfo>>
}