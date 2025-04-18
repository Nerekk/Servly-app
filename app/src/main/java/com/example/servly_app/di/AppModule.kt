package com.example.servly_app.di

import com.example.servly_app.core.data.ApiService
import com.example.servly_app.core.data.ChatService
import com.example.servly_app.core.data.JobPostingService
import com.example.servly_app.core.data.JobRequestService
import com.example.servly_app.core.data.RetrofitInstance
import com.example.servly_app.core.data.ReviewService
import com.example.servly_app.core.data.RoleService
import com.example.servly_app.core.data.ScheduleService
import com.example.servly_app.core.data.PaymentService
import com.example.servly_app.core.domain.repository.CustomerRepository
import com.example.servly_app.core.domain.repository.JobPostingRepository
import com.example.servly_app.core.domain.repository.JobRequestRepository
import com.example.servly_app.core.domain.repository.ProviderRepository
import com.example.servly_app.core.domain.repository.ReviewRepository
import com.example.servly_app.features.authentication.data.AuthRepository
import com.example.servly_app.features.authentication.domain.repository.AuthRepositoryImpl
import com.example.servly_app.features.authentication.domain.usecase.AuthUseCases
import com.example.servly_app.features.authentication.domain.usecase.CheckUserLoggedIn
import com.example.servly_app.features.authentication.domain.usecase.Logout
import com.example.servly_app.features.authentication.domain.usecase.SignInWithEmail
import com.example.servly_app.features.authentication.domain.usecase.SignInWithGoogle
import com.example.servly_app.features.authentication.domain.usecase.SignUpWithEmail
import com.example.servly_app.core.domain.repository.RoleRepository
import com.example.servly_app.core.domain.repository.ScheduleRepository
import com.example.servly_app.core.domain.usecase.CustomerUseCases
import com.example.servly_app.core.domain.usecase.GetCustomer
import com.example.servly_app.core.domain.usecase.GetCustomerById
import com.example.servly_app.core.domain.usecase.GetProvider
import com.example.servly_app.core.domain.usecase.GetProviderById
import com.example.servly_app.core.domain.usecase.GetUserRoles
import com.example.servly_app.core.domain.usecase.ProviderUseCases
import com.example.servly_app.core.domain.usecase.job_request.CreateJobRequest
import com.example.servly_app.core.domain.usecase.job_request.GetJobRequests
import com.example.servly_app.core.domain.usecase.job_request.GetJobRequestsPosting
import com.example.servly_app.core.domain.usecase.job_request.JobRequestUseCases
import com.example.servly_app.core.domain.usecase.job_request.UpdateJobRequestStatus
import com.example.servly_app.features._customer.job_create.data.source.CategoryService
import com.example.servly_app.features._customer.job_create.domain.repository.CategoryRepository
import com.example.servly_app.features._customer.job_create.domain.usecase.CategoryUseCases
import com.example.servly_app.core.domain.usecase.job_posting.CreateJobPosting
import com.example.servly_app.core.domain.usecase.job_posting.GetJobPosting
import com.example.servly_app.features._customer.job_create.domain.usecase.GetCategories
import com.example.servly_app.features._customer.job_create.domain.usecase.GetQuestions
import com.example.servly_app.core.domain.usecase.job_posting.GetUserJobPostings
import com.example.servly_app.core.domain.usecase.job_posting.JobPostingUseCases
import com.example.servly_app.core.domain.usecase.job_posting.UpdateJobStatus
import com.example.servly_app.core.domain.usecase.job_request.GetJobRequestForProvider
import com.example.servly_app.core.domain.usecase.job_request.GetJobRequestSelectedForCustomer
import com.example.servly_app.core.domain.usecase.reviews.CreateCustomerReview
import com.example.servly_app.core.domain.usecase.reviews.CreateProviderReview
import com.example.servly_app.core.domain.usecase.reviews.GetCustomerReviews
import com.example.servly_app.core.domain.usecase.reviews.GetProviderReviews
import com.example.servly_app.core.domain.usecase.reviews.ReviewUseCases
import com.example.servly_app.core.domain.usecase.schedule.ApproveScheduleAsCustomer
import com.example.servly_app.core.domain.usecase.schedule.CreateScheduleForJob
import com.example.servly_app.core.domain.usecase.schedule.GetScheduleForJob
import com.example.servly_app.core.domain.usecase.schedule.GetScheduleSummaryForUser
import com.example.servly_app.core.domain.usecase.schedule.GetSchedulesForDay
import com.example.servly_app.core.domain.usecase.schedule.GetSchedulesForUser
import com.example.servly_app.core.domain.usecase.schedule.RejectScheduleAsCustomer
import com.example.servly_app.core.domain.usecase.schedule.ScheduleUseCases
import com.example.servly_app.core.domain.usecase.schedule.UpdateScheduleForJob
import com.example.servly_app.features._provider.job_list.domain.repository.ProviderJobListRepository
import com.example.servly_app.features._provider.job_list.domain.usecase.GetFilteredActiveJobs
import com.example.servly_app.features._provider.job_list.domain.usecase.ProviderJobListUseCases
import com.example.servly_app.features.chat.domain.ChatRepository
import com.example.servly_app.features.chat.domain.usecase.ChatUseCases
import com.example.servly_app.features.chat.domain.usecase.GetChatDetails
import com.example.servly_app.features.chat.domain.usecase.GetChatHistory
import com.example.servly_app.features.payments.domain.CreateIntent
import com.example.servly_app.features.payments.domain.CreatePayment
import com.example.servly_app.features.payments.domain.GetPayment
import com.example.servly_app.features.payments.domain.PaymentRepository
import com.example.servly_app.features.payments.domain.PaymentUseCases
import com.example.servly_app.features.payments.domain.UpdatePayment
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
            getCustomer = GetCustomer(customerRepository),
            getCustomerById = GetCustomerById(customerRepository)
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
    fun provideJobPostingUseCases(jobPostingRepository: JobPostingRepository): JobPostingUseCases {
        return JobPostingUseCases(
            createJobPosting = CreateJobPosting(jobPostingRepository),
            getUserJobPostings = GetUserJobPostings(jobPostingRepository),
            updateJobStatus = UpdateJobStatus(jobPostingRepository),
            getJobPosting = GetJobPosting(jobPostingRepository)
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
            GetProvider(providerRepository),
            GetProviderById(providerRepository)
        )
    }

    @Provides
    @Singleton
    fun provideProviderJobListRepository(jobPostingService: JobPostingService) : ProviderJobListRepository {
        return ProviderJobListRepository(jobPostingService)
    }

    @Provides
    @Singleton
    fun provideProviderJobListUseCases(providerJobListRepository: ProviderJobListRepository) : ProviderJobListUseCases {
        return ProviderJobListUseCases(GetFilteredActiveJobs(providerJobListRepository))
    }

    @Provides
    @Singleton
    fun provideJobRequestService(): JobRequestService {
        return RetrofitInstance.createService(JobRequestService::class.java)
    }

    @Provides
    @Singleton
    fun provideJobRequestRepository(jobRequestService: JobRequestService): JobRequestRepository {
        return JobRequestRepository(jobRequestService)
    }

    @Provides
    @Singleton
    fun provideJobRequestUseCases(jobRequestRepository: JobRequestRepository): JobRequestUseCases {
        return JobRequestUseCases(
            createJobRequest = CreateJobRequest(jobRequestRepository),
            getJobRequests = GetJobRequests(jobRequestRepository),
            getJobRequestsPosting = GetJobRequestsPosting(jobRequestRepository),
            updateJobRequestStatus = UpdateJobRequestStatus(jobRequestRepository),
            getJobRequestForProvider = GetJobRequestForProvider(jobRequestRepository),
            getJobRequestSelectedForCustomer = GetJobRequestSelectedForCustomer(jobRequestRepository)
        )
    }

    @Provides
    @Singleton
    fun provideScheduleService(): ScheduleService {
        return RetrofitInstance.createService(ScheduleService::class.java)
    }

    @Provides
    @Singleton
    fun provideScheduleRepository(scheduleService: ScheduleService): ScheduleRepository {
        return ScheduleRepository(scheduleService)
    }

    @Provides
    @Singleton
    fun provideScheduleUseCases(scheduleRepository: ScheduleRepository): ScheduleUseCases {
        return ScheduleUseCases(
            getSchedulesForUser = GetSchedulesForUser(scheduleRepository),
            getScheduleForJob = GetScheduleForJob(scheduleRepository),
            createScheduleForJob = CreateScheduleForJob(scheduleRepository),
            updateScheduleForJob = UpdateScheduleForJob(scheduleRepository),
            approveScheduleAsCustomer = ApproveScheduleAsCustomer(scheduleRepository),
            rejectScheduleAsCustomer = RejectScheduleAsCustomer(scheduleRepository),
            getSchedulesForDay = GetSchedulesForDay(scheduleRepository),
            getScheduleSummaryForUser = GetScheduleSummaryForUser(scheduleRepository)
        )
    }

    @Provides
    @Singleton
    fun provideChatService(): ChatService {
        return RetrofitInstance.createService(ChatService::class.java)
    }

    @Provides
    @Singleton
    fun provideChatRepository(chatService: ChatService): ChatRepository {
        return ChatRepository(chatService)
    }

    @Provides
    @Singleton
    fun provideChatUseCases(chatRepository: ChatRepository): ChatUseCases {
        return ChatUseCases(
            getChatDetails = GetChatDetails(chatRepository),
            getChatHistory = GetChatHistory(chatRepository)
        )
    }


    @Provides
    @Singleton
    fun provideReviewService(): ReviewService {
        return RetrofitInstance.createService(ReviewService::class.java)
    }

    @Provides
    @Singleton
    fun provideReviewRepository(reviewService: ReviewService): ReviewRepository {
        return ReviewRepository(reviewService)
    }

    @Provides
    @Singleton
    fun provideReviewUseCases(reviewRepository: ReviewRepository): ReviewUseCases {
        return ReviewUseCases(
            createCustomerReview = CreateCustomerReview(reviewRepository),
            createProviderReview = CreateProviderReview(reviewRepository),
            getCustomerReviews = GetCustomerReviews(reviewRepository),
            getProviderReviews = GetProviderReviews(reviewRepository)
        )
    }


    @Provides
    @Singleton
    fun providePaymentService(): PaymentService {
        return RetrofitInstance.createService(PaymentService::class.java)
    }

    @Provides
    @Singleton
    fun providePaymentRepository(paymentService: PaymentService): PaymentRepository {
        return PaymentRepository(paymentService)
    }

    @Provides
    @Singleton
    fun providePaymentUseCases(paymentRepository: PaymentRepository): PaymentUseCases {
        return PaymentUseCases(
            createIntent = CreateIntent(paymentRepository),
            createPayment = CreatePayment(paymentRepository),
            updatePayment = UpdatePayment(paymentRepository),
            getPayment = GetPayment(paymentRepository)
        )
    }
}