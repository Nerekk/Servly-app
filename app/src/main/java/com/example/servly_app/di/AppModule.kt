package com.example.servly_app.di

import com.example.servly_app.core.data.ApiService
import com.example.servly_app.core.data.RetrofitInstance
import com.example.servly_app.core.data.RoleDataService
import com.example.servly_app.features.authentication.data.AuthRepository
import com.example.servly_app.features.authentication.domain.repository.AuthRepositoryImpl
import com.example.servly_app.features.authentication.domain.usecase.AuthUseCases
import com.example.servly_app.features.authentication.domain.usecase.CheckUserLoggedIn
import com.example.servly_app.features.authentication.domain.usecase.Logout
import com.example.servly_app.features.authentication.domain.usecase.SignInWithEmail
import com.example.servly_app.features.authentication.domain.usecase.SignInWithGoogle
import com.example.servly_app.features.authentication.domain.usecase.SignUpWithEmail
import com.example.servly_app.core.domain.repository.RoleRepository
import com.example.servly_app.core.domain.usecase.GetUserRoles
import com.example.servly_app.features.role_selection.domain.repository.UserFormRepository
import com.example.servly_app.features.role_selection.domain.usecase.CreateCustomer
import com.example.servly_app.features.role_selection.domain.usecase.CreateProvider
import com.example.servly_app.features.role_selection.domain.usecase.CustomerFormUseCases
import com.example.servly_app.features.role_selection.domain.usecase.ProviderFormUseCases
import com.example.servly_app.features.role_selection.domain.usecase.UpdateCustomer
import com.example.servly_app.features.role_selection.domain.usecase.UpdateProvider
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
            signInWithEmail = SignInWithEmail(repository),
            signUpWithEmail = SignUpWithEmail(repository),
            signInWithGoogle = SignInWithGoogle(repository),
            logout = Logout(repository)
        )
    }


    @Provides
    @Singleton
    fun provideApiService() : ApiService {
        return RetrofitInstance.createService(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRoleRepository(apiService: ApiService) : RoleRepository {
        return RoleRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideGetUserRoles(repository: RoleRepository) : GetUserRoles {
        return GetUserRoles(repository)
    }



    @Provides
    @Singleton
    fun provideRoleDataService() : RoleDataService {
        return RetrofitInstance.createService(RoleDataService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserFormRepository(roleDataService: RoleDataService) : UserFormRepository {
        return UserFormRepository(roleDataService)
    }

    @Provides
    @Singleton
    fun provideCustomerFormUseCases(userFormRepository: UserFormRepository): CustomerFormUseCases {
        return CustomerFormUseCases(
            createCustomer = CreateCustomer(userFormRepository),
            updateCustomer = UpdateCustomer(userFormRepository)
        )
    }

    @Provides
    @Singleton
    fun provideProviderFormUseCases(userFormRepository: UserFormRepository): ProviderFormUseCases {
        return ProviderFormUseCases(
            createProvider = CreateProvider(userFormRepository),
            updateProvider = UpdateProvider(userFormRepository)
        )
    }
}