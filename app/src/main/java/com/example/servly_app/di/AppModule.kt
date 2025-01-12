package com.example.servly_app.di

import com.example.servly_app.features.authentication.data.AuthRepository
import com.example.servly_app.features.authentication.domain.repository.AuthRepositoryImpl
import com.example.servly_app.features.authentication.domain.usecase.AuthUseCases
import com.example.servly_app.features.authentication.domain.usecase.CheckUserLoggedIn
import com.example.servly_app.features.authentication.domain.usecase.Logout
import com.example.servly_app.features.authentication.domain.usecase.SendVerificationCode
import com.example.servly_app.features.authentication.domain.usecase.VerifyCode
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository() : AuthRepository {
        return AuthRepositoryImpl(FirebaseAuth.getInstance())
    }

    @Provides
    @Singleton
    fun provideAuthUseCases(repository: AuthRepository) : AuthUseCases {
        return AuthUseCases(
            checkUserLoggedIn = CheckUserLoggedIn(repository),
            sendVerificationCode = SendVerificationCode(repository),
            verifyCode = VerifyCode(repository),
            logout = Logout(repository)
        )
    }
}