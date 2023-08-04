package com.pronted.di

import com.pronted.data.attendance.AttendanceRepositoryImpl
import com.pronted.data.employee.EmployeeRepositoryImpl
import com.pronted.data.exam.ExamScheduleRepositoryImpl
import com.pronted.data.feepayments.FeePaymentsRepositoryImpl
import com.pronted.data.images.ImageAccessRepositoryImpl
import com.pronted.data.login.LoginRepositoryImpl
import com.pronted.data.noticeboard.NoticeboardRepositoryImpl
import com.pronted.data.student.StudentDataRepositoryImpl
import com.pronted.data.timeline.TimelineRepositoryImpl
import com.pronted.data.userapps.UserAppsRepositoryImpl
import com.pronted.domain.attendance.AttendanceRepository
import com.pronted.domain.employee.EmployeeRepository
import com.pronted.domain.exam.ExamScheduleRepository
import com.pronted.domain.feepayments.FeePaymentsRepository
import com.pronted.domain.images.ImageAccessRepository
import com.pronted.domain.login.LoginRepository
import com.pronted.domain.noticeboard.NoticeboardRepository
import com.pronted.domain.student.StudentDataRepository
import com.pronted.domain.timeline.TimelineRepository
import com.pronted.domain.userapps.UserAppsRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<LoginRepository> {
        LoginRepositoryImpl(get())
    }
    single<UserAppsRepository> {
        UserAppsRepositoryImpl(get())
    }
    single<ImageAccessRepository> {
        ImageAccessRepositoryImpl(get())
    }
    single<StudentDataRepository> {
        StudentDataRepositoryImpl(get())
    }

    single<TimelineRepository> {
        TimelineRepositoryImpl(get())
    }

    single<AttendanceRepository> {
        AttendanceRepositoryImpl(get())
    }
    single<FeePaymentsRepository> {
        FeePaymentsRepositoryImpl(get())
    }
    single<EmployeeRepository> {
        EmployeeRepositoryImpl(get())
    }

    single<NoticeboardRepository> {
        NoticeboardRepositoryImpl(get())
    }

    single<ExamScheduleRepository>{
        ExamScheduleRepositoryImpl(get())
    }
}