package com.example.servly_app.core.data.util

enum class JobRequestStatus {
    ACTIVE,
    WAITING_FOR_PROVIDER_APPROVE,
    IN_PROGRESS,
    WAITING_FOR_PROVIDER_COMPLETE,
    WAITING_FOR_CUSTOMER_COMPLETE,
    COMPLETED,
    REJECTED,
    CANCELED_IN_PROGRESS_BY_CUSTOMER,
    CANCELED_IN_PROGRESS_BY_PROVIDER,
    WITHDRAWN
}