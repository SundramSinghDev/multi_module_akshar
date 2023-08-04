package com.pronted.di

import com.pronted.data.attendance.AttendanceUseCaseImpl
import com.pronted.data.employee.EmployeeUseCaseImpl
import com.pronted.data.exam.AddExamScheduleUseCaseImpl
import com.pronted.data.exam.ExamScheduleUseCaseImpl
import com.pronted.data.feepayments.FeePaymentsUseCaseImpl
import com.pronted.data.images.ImageAccessUseCaseImpl
import com.pronted.data.login.LoginUseCaseImpl
import com.pronted.data.noticeboard.NoticeboardUseCaseImpl
import com.pronted.data.student.StudentDataUseCaseImpl
import com.pronted.data.timeline.TimelineUseCaseImpl
import com.pronted.data.userapps.UserAppsUseCaseImpl
import com.pronted.domain.attendance.AttendanceUseCase
import com.pronted.domain.employee.EmployeeUseCase
import com.pronted.domain.exam.AddExamScheduleUseCase
import com.pronted.domain.exam.ExamScheduleUseCase
import com.pronted.domain.feepayments.FeePaymentUseCase
import com.pronted.domain.images.ImageAccessUseCase
import com.pronted.domain.login.LoginUseCase
import com.pronted.domain.noticeboard.NoticeboardUseCase
import com.pronted.domain.student.StudentDataUseCase
import com.pronted.domain.timeline.TimelineUseCase
import com.pronted.domain.userapps.UserAppsUseCase
import com.pronted.presentation.attendance.AttendanceViewModel
import com.pronted.presentation.authentication.LoginViewModel
import com.pronted.presentation.authentication.OtpViewModel
import com.pronted.presentation.employee.EmployeeViewModel
import com.pronted.presentation.feepayments.FeePaymentsViewModel
import com.pronted.presentation.noticeboard.NoticeboardViewModel
import com.pronted.presentation.profile.StudentViewModel
import com.pronted.presentation.scheduleexam.AddExamScheduleViewModel
import com.pronted.presentation.scheduleexam.ExamScheduleViewModel
import com.pronted.presentation.scheduleexam.ParentExamScheduleViewModel
import com.pronted.presentation.timeline.TimeLineViewModel
import com.pronted.presentation.userapps.ImagesViewModel
import com.pronted.presentation.userapps.UserAppsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    //Authentication
    factory<LoginUseCase> { LoginUseCaseImpl(get()) }
    //user apps access
    factory<UserAppsUseCase> { UserAppsUseCaseImpl(get()) }

    //Image access
    factory<ImageAccessUseCase> { ImageAccessUseCaseImpl(get()) }

    //Student data
    factory<StudentDataUseCase> { StudentDataUseCaseImpl(get()) }

    //Student data
    factory<TimelineUseCase> { TimelineUseCaseImpl(get()) }

    //Student data
    factory<AttendanceUseCase> { AttendanceUseCaseImpl(get(), get()) }

    //Fee payments
    factory<FeePaymentUseCase> { FeePaymentsUseCaseImpl(get()) }

    //Employee
    factory<EmployeeUseCase> { EmployeeUseCaseImpl(get()) }

    //Noticeboard
    factory<NoticeboardUseCase> { NoticeboardUseCaseImpl(get()) }

    //ExamSchedule
    factory<ExamScheduleUseCase> { ExamScheduleUseCaseImpl(get(), get()) }
    factory<AddExamScheduleUseCase> { AddExamScheduleUseCaseImpl(get()) }

    viewModel {
        LoginViewModel(get())
    }
    viewModel {
        OtpViewModel(get())
    }
    viewModel {
        UserAppsViewModel(get())
    }
    viewModel {
        ImagesViewModel(get())
    }
    viewModel {
        StudentViewModel(get())
    }
    viewModel {
        TimeLineViewModel(get())
    }
    viewModel {
        AttendanceViewModel(get())
    }

    viewModel {
        FeePaymentsViewModel(get())
    }

    viewModel {
        EmployeeViewModel(get())
    }

    viewModel {
        NoticeboardViewModel(get())
    }

    viewModel {
        ExamScheduleViewModel(get())
    }

    viewModel {
        AddExamScheduleViewModel(get())
    }

    viewModel {
        ParentExamScheduleViewModel(get())
    }
}