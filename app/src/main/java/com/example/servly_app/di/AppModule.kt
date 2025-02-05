package com.example.servly_app.di

import com.example.servly_app.core.data.ApiService
import com.example.servly_app.core.data.JobPostingService
import com.example.servly_app.core.data.RetrofitInstance
import com.example.servly_app.core.data.RoleService
import com.example.servly_app.core.domain.repository.CustomerRepository
import com.example.servly_app.core.domain.repository.JobPostingRepository
import com.example.servly_app.core.domain.repository.ProviderRepository
import com.example.servly_app.features.authentication.data.AuthRepository
import com.example.servly_app.features.authentication.domain.repository.AuthRepositoryImpl
import com.example.servly_app.features.authentication.domain.usecase.AuthUseCases
import com.example.servly_app.features.authentication.domain.usecase.CheckUserLoggedIn
import com.example.servly_app.features.authentication.domain.usecase.Logout
import com.example.servly_app.features.authentication.domain.usecase.SignInWithEmail
import com.example.servly_app.features.authentication.domain.usecase.SignInWithGoogle
import com.example.servly_app.features.authentication.domain.usecase.SignUpWithEmail
import com.example.servly_app.core.domain.repository.RoleRepository
import com.example.servly_app.core.domain.usecase.CustomerUseCases
import com.example.servly_app.core.domain.usecase.GetCustomer
import com.example.servly_app.core.domain.usecase.GetProvider
import com.example.servly_app.core.domain.usecase.GetUserRoles
import com.example.servly_app.core.domain.usecase.ProviderUseCases
import com.example.servly_app.features._customer.job_create.data.source.CategoryService
import com.example.servly_app.features._customer.job_create.domain.repository.CategoryRepository
import com.example.servly_app.features._customer.job_create.domain.usecase.CategoryUseCases
import com.example.servly_app.features._customer.job_create.domain.usecase.CreateJobPosting
import com.example.servly_app.features._customer.job_create.domain.usecase.GetCategories
import com.example.servly_app.features._customer.job_create.domain.usecase.GetQuestions
import com.example.servly_app.features._customer.job_list.domain.usecase.GetUserJobPostings
import com.example.servly_app.features._customer.job_list.domain.usecase.RequestUseCases
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
    fun provideRoleDataService() : RoleService {
        return RetrofitInstance.createService(RoleService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserFormRepository(roleService: RoleService) : UserFormRepository {
        return UserFormRepository(roleService)
    }

    @Provides
    @Singleton
    fun provideCustomerRepository(roleService: RoleService) : CustomerRepository {
        return CustomerRepository(roleService)
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
    fun provideCustomerUseCases(customerRepository: CustomerRepository): CustomerUseCases {
        return CustomerUseCases(
            getCustomer = GetCustomer(customerRepository)
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


    @Provides
    @Singleton
    fun provideCategoryService() : CategoryService {
        return RetrofitInstance.createService(CategoryService::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(categoryService: CategoryService) : CategoryRepository {
        return CategoryRepository(categoryService)
    }

    @Provides
    @Singleton
    fun provideCategoryUseCases(categoryRepository: CategoryRepository): CategoryUseCases {
        return CategoryUseCases(
            getCategories = GetCategories(categoryRepository),
            getQuestions = GetQuestions(categoryRepository)
        )
    }

    @Provides
    @Singleton
    fun provideJobPostingService() : JobPostingService {
        return RetrofitInstance.createService(JobPostingService::class.java)
    }

    @Provides
    @Singleton
    fun provideJobPostingRepository(jobPostingService: JobPostingService) : JobPostingRepository {
        return JobPostingRepository(jobPostingService)
    }

    @Provides
    @Singleton
    fun provideCreateJobPosting(jobPostingRepository: JobPostingRepository) : CreateJobPosting {
        return CreateJobPosting(jobPostingRepository)
    }

    @Provides
    @Singleton
    fun provideRequestUseCases(jobPostingRepository: JobPostingRepository) : RequestUseCases {
        return RequestUseCases(
            getUserJobPostings = GetUserJobPostings(jobPostingRepository)
        )
    }

    @Provides
    @Singleton
    fun provideProviderRepository(roleService: RoleService) : ProviderRepository {
        return ProviderRepository(roleService)
    }

    @Provides
    @Singleton
    fun provideProviderUseCases(providerRepository: ProviderRepository) : ProviderUseCases {
        return ProviderUseCases(
            GetProvider(providerRepository)
        )
    }
}