package com.pronted.presentation.noticeboard

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField
import com.pronted.R
import com.pronted.dto.login.UserAppList
import com.pronted.dto.noticeboard.NoticeBoard
import com.pronted.dto.student.AddressModel
import com.pronted.dto.student.StudentContact
import com.pronted.dto.student.StudentProfileAction
import com.pronted.dto.student.StudentProfileResponse
import com.pronted.utils.extentions.USER_SELECTED_ROLE
import com.pronted.utils.extentions.getNationalMobileNumber
import com.pronted.utils.extentions.isMobileNumber
import io.paperdb.Paper

class NoticeModel : BaseObservable() {
    private val title = ObservableField<String>()
    private val description = ObservableField<String>()
    private val startDate = ObservableField<String>()
    private val endDate = ObservableField<String>()
    private val visibility = ObservableField<String>()


    val titleError = ObservableField<String>()
    val descriptionError = ObservableField<String>()
    val startDateError = ObservableField<String>()
    val endDateError = ObservableField<String>()


    fun getTitle(): ObservableField<String> {
        titleError.set(null)
        return title
    }

    fun title(): String {
        title.get()?.let {
            return it
        }
        return ""
    }


    fun getDescription(): ObservableField<String> {
        descriptionError.set(null)
        return description
    }

    fun description(): String {
        description.get()?.let {
            return it
        }
        return ""
    }


    fun getStartDate(): ObservableField<String> {
        startDateError.set(null)
        return startDate
    }

    fun startDate(): String {
        startDate.get()?.let {
            return it
        }
        return ""
    }

    fun getEndDate(): ObservableField<String> {
        endDateError.set(null)
        return endDate
    }

    fun endDate(): String {
        endDate.get()?.let {
            return it
        }
        return ""
    }

    fun visibility(): String {
        visibility.get()?.let {
            return it
        }
        return ""
    }

    fun setVisibility(visibilityStr: String) {
        visibility.set(visibilityStr)
    }

    fun isValidNotice(context: Context): Boolean {
        return if (title().isNotBlank() && description().isNotBlank() && startDate().isNotBlank() && endDate().isNotBlank()) {
            true
        } else {
            if (title().isBlank()) {
                titleError.set(context.getString(R.string.title_required))
            }
            if (description().isBlank()) {
                descriptionError.set(context.getString(R.string.desc_required))
            }
            if (startDate().isBlank()) {
                startDateError.set(context.getString(R.string.start_date_required))
            }
            if (endDate().isBlank()) {
                endDateError.set(context.getString(R.string.end_date_required))
            }

            false
        }
    }

    fun setData(notice: NoticeBoard) {
        title.set(notice.title)
        description.set(notice.description)
        startDate.set(notice.startDate)
        endDate.set(notice.endDate)
        visibility.set(notice.visibility)
    }

    fun getNotice(noticeId:Int): NoticeBoard {
        return NoticeBoard(
            id = noticeId,
            schoolCd = Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.schoolCode ?: "",
            title = title(),
            description = description(),
            startDate = startDate(),
            endDate = endDate(),
            visibility = visibility(),
            auditFields = null
        )
    }

/*
    fun getEditedProfileData(
        studentProfile: StudentProfileResponse,
        classRoomId: Int = 0
    ): StudentProfileResponse {

        return StudentProfileResponse(
            studentProfileId = studentProfile.studentProfileId,
            classroomId = classRoomId,
            firstName = firstName(),
            lastName = lastName(),
            admissionNumber = admissionNumber(),
            rollNbr = rollNumber(),
            dateOfBirth = dob(),
            gender = gender(),
            bloodGroup = bloodGroup(),
            studentContact = StudentContact(
                studentProfileId = studentProfile.studentProfileId,
                primaryParentName = parentName(),
                primaryParentEmail = parentEmail(),
                primaryParentRelationship = relation(),
                primaryParentMobile = nationalMobileNumber(parentMobile()),
                address = AddressModel(
                    addressLine1 = houseNumber(),
                    addressLine2 = street(),
                    city = city(),
                    district = district(),
                    state = state(),
                    country = country(),
                    postalcode = postalCode()
                )
            )
        )
    }*/

}