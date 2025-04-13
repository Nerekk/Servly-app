package com.example.servly_app.core.util

import com.example.servly_app.R
import com.example.servly_app.core.data.util.JobRequestStatus
import com.example.servly_app.core.data.util.JobStatus
import com.example.servly_app.core.data.util.ScheduleStatus

object EnumUtils {
    fun getStatusString(status: ScheduleStatus): Int {
        return when (status) {
            ScheduleStatus.WAITING_FOR_CUSTOMER_APPROVAL, ScheduleStatus.UPDATED_WAITING_FOR_CUSTOMER_APPROVAL -> R.string.enum_schedule_waiting
            ScheduleStatus.REJECTED -> R.string.enum_schedule_rejected
            ScheduleStatus.APPROVED -> R.string.enum_schedule_approved
        }
    }

    fun getStatusString(status: JobRequestStatus): Int {
        return when (status) {
            JobRequestStatus.ACTIVE -> R.string.enum_job_active
            JobRequestStatus.IN_PROGRESS -> R.string.enum_job_inprogress
            JobRequestStatus.WAITING_FOR_PROVIDER_COMPLETE,
            JobRequestStatus.WAITING_FOR_CUSTOMER_COMPLETE,
            JobRequestStatus.WAITING_FOR_PROVIDER_APPROVE -> R.string.enum_job_request_waiting
            JobRequestStatus.COMPLETED -> R.string.enum_job_completed
            JobRequestStatus.REJECTED -> R.string.enum_job_request_rejected
            JobRequestStatus.CANCELED_IN_PROGRESS_BY_CUSTOMER -> R.string.enum_job_request_canceledby_customer
            JobRequestStatus.CANCELED_IN_PROGRESS_BY_PROVIDER -> R.string.enum_job_request_canceledby_provider
            JobRequestStatus.WITHDRAWN -> R.string.enum_job_request_withdrawn
        }
    }

    fun getStatusString(status: JobStatus): Int {
        return when (status) {
            JobStatus.ACTIVE -> R.string.enum_job_active
            JobStatus.IN_PROGRESS -> R.string.enum_job_inprogress
            JobStatus.COMPLETED -> R.string.enum_job_completed
            JobStatus.CANCELED -> R.string.enum_job_canceled
        }
    }

    fun getReviewStatusString(status: JobRequestStatus?): Int {
        return when (status) {
            JobRequestStatus.COMPLETED -> R.string.enum_job_completed
            JobRequestStatus.CANCELED_IN_PROGRESS_BY_PROVIDER -> R.string.enum_job_request_canceledby_provider
            JobRequestStatus.CANCELED_IN_PROGRESS_BY_CUSTOMER -> R.string.enum_job_request_canceledby_customer
            else -> R.string.unknown
        }
    }
}