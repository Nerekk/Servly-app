package com.example.servly_app.core.data

import com.example.servly_app.BuildConfig
import com.example.servly_app.core.data.util.AuthInterceptor
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

object RetrofitInstance {
    private fun createRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()

        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, object : JsonDeserializer<LocalDate> {
                override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDate {
                    return LocalDate.parse(json.asString, DateTimeFormatter.ISO_LOCAL_DATE)
                }
            })
            .registerTypeAdapter(LocalDate::class.java, object : JsonSerializer<LocalDate> {
                override fun serialize(src: LocalDate, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
                    return JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE))
                }
            })
            .registerTypeAdapter(LocalDateTime::class.java, object : JsonDeserializer<LocalDateTime> {
                override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDateTime {
                    return LocalDateTime.parse(json.asString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                }
            })
            .registerTypeAdapter(LocalDateTime::class.java, object : JsonSerializer<LocalDateTime> {
                override fun serialize(src: LocalDateTime, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
                    return JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                }
            })
            .registerTypeAdapter(YearMonth::class.java, object : JsonDeserializer<YearMonth> {
                override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): YearMonth {
                    return YearMonth.parse(json.asString, DateTimeFormatter.ofPattern("yyyy-MM"))
                }
            })
            .registerTypeAdapter(YearMonth::class.java, object : JsonSerializer<YearMonth> {
                override fun serialize(src: YearMonth, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
                    return JsonPrimitive(src.format(DateTimeFormatter.ofPattern("yyyy-MM")))
                }
            })
            .create()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun <T> createService(apiServiceClass: Class<T>): T {
        return createRetrofit().create(apiServiceClass)
    }
}