package com.pronted.di

import com.pronted.BuildConfig
import com.pronted.domain.attendance.AttendanceApi
import com.pronted.domain.employee.EmployeeApi
import com.pronted.domain.exam.ExamScheduleApi
import com.pronted.domain.feepayments.FeePaymentsApi
import com.pronted.domain.images.ImageAccessApi
import com.pronted.domain.login.LoginApi
import com.pronted.domain.noticeboard.NoticeboardApi
import com.pronted.domain.student.StudentApi
import com.pronted.domain.timeline.TimelineApi
import com.pronted.domain.userapps.UserAppsApi
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    factory {
        get<Retrofit.Builder>().baseUrl(BuildConfig.BASE_AUTH_URL).build()
            .create(LoginApi::class.java)
    }
    single {
        get<Retrofit.Builder>().baseUrl(BuildConfig.BASE_AUTH_URL).build()
            .create(UserAppsApi::class.java)
    }

    single {
        get<Retrofit>().create(ImageAccessApi::class.java)
    }
    single {
        get<Retrofit>().create(StudentApi::class.java)
    }

    single {
        get<Retrofit>().create(TimelineApi::class.java)
    }
    single {
        get<Retrofit>().create(AttendanceApi::class.java)
    }

    single {
        get<Retrofit>().create(FeePaymentsApi::class.java)
    }
    single {
        get<Retrofit>().create(EmployeeApi::class.java)
    }

    single {
        get<Retrofit>().create(NoticeboardApi::class.java)
    }

    single {
        get<Retrofit>().create(ExamScheduleApi::class.java)
    }
}