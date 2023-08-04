package com.pronted.presentation.employee

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField
import com.pronted.R
import com.pronted.dto.employee.Employee
import com.pronted.dto.student.AddressModel
import com.pronted.dto.student.StudentContact
import com.pronted.dto.student.StudentProfileAction
import com.pronted.dto.student.StudentProfileResponse
import com.pronted.utils.extentions.getNationalMobileNumber
import com.pronted.utils.extentions.isEmailAddress
import com.pronted.utils.extentions.isMobileNumber

class EditEmployeeModel : BaseObservable() {
    private val firstName = ObservableField<String>()
    private val lastName = ObservableField<String>()
    private val gender = ObservableField<String>()
    private val employeeId = ObservableField<String>()
    private val profileImage = ObservableField<String>()


    private val department = ObservableField<String>()
    private val designation = ObservableField<String>()
    private val email = ObservableField<String>()
    private val mobile = ObservableField<String>()


    private val houseNo = ObservableField<String>()
    private val street = ObservableField<String>()
    private val city = ObservableField<String>()
    private val district = ObservableField<String>()
    private val state = ObservableField<String>()
    private val country = ObservableField<String>()
    private val postalCode = ObservableField<String>()


    val firstNameError = ObservableField<String>()
    val lastNameError = ObservableField<String>()
    val empIdError = ObservableField<String>()
    val emailError = ObservableField<String>()
    val genderError = ObservableField<String>()
    val mobileError = ObservableField<String>()


    fun getFirstName(): ObservableField<String> {
        firstNameError.set(null)
        return firstName
    }

    fun firstName(): String {
        firstName.get()?.let {
            return it
        }
        return ""
    }

    fun getProfileImage(): ObservableField<String> {
        return profileImage
    }

    fun profileImage(): String {
        profileImage.get()?.let {
            return it
        }
        return ""
    }


    fun getLastName(): ObservableField<String> {
        lastNameError.set(null)
        return lastName
    }

    fun lastName(): String {
        lastName.get()?.let {
            return it
        }
        return ""
    }

    fun getGender(): ObservableField<String> {
        genderError.set(null)
        return gender
    }

    fun setGender(gender:String){
        this.gender.set(gender)
    }
    fun gender(): String {
        gender.get()?.let {
            return it
        }
        return ""
    }

    fun getEmployeeId(): ObservableField<String> {
        return employeeId
    }

    fun employeeId(): String {
        employeeId.get()?.let {
            return it
        }
        return ""
    }


    fun getDepartment(): ObservableField<String> {
        return department
    }

    fun setDepartment(department:String){
        this.department.set(department)
    }

    fun department(): String {
        department.get()?.let {
            return it
        }
        return ""
    }


    fun getDesignation(): ObservableField<String> {
        return designation
    }

    fun setDesignation(state:String){
        this.designation.set(state)
    }

    fun designation(): String {
        designation.get()?.let {
            return it
        }
        return ""
    }


    fun getEmail(): ObservableField<String> {
        emailError.set(null)
        return email
    }

    fun email(): String {
        email.get()?.let {
            return it
        }
        return ""
    }

    fun getMobile(): ObservableField<String> {
        mobileError.set(null)
        return mobile
    }

    private fun mobile(): String {
        mobile.get()?.let {
            return "+91${it}"
        }
        return ""
    }

    private fun nationalMobileNumber(number: String): String {
        return number.getNationalMobileNumber()
    }

    fun getNumber(): String {
        return nationalMobileNumber(mobile())
    }


    fun getHouseNo(): ObservableField<String> {
        return houseNo
    }

    fun houseNumber(): String {
        houseNo.get()?.let {
            return it
        }
        return ""
    }

    fun getStreet(): ObservableField<String> {
        return street
    }

    fun street(): String {
        street.get()?.let {
            return it
        }
        return ""
    }

    fun getCity(): ObservableField<String> {
        return city
    }

    fun city(): String {
        city.get()?.let {
            return it
        }
        return ""
    }

    fun getDistrict(): ObservableField<String> {
        return district
    }

    fun district(): String {
        district.get()?.let {
            return it
        }
        return ""
    }

    fun getState(): ObservableField<String> {
        return state
    }

    fun setState(state:String){
        this.state.set(state)
    }

    fun state(): String {
        state.get()?.let {
            return it
        }
        return ""
    }

    fun getCountry(): ObservableField<String> {
        return country
    }

    fun country(): String {
        country.get()?.let {
            return it
        }
        return ""
    }

    fun getPostalCode(): ObservableField<String> {
        return postalCode
    }

    fun postalCode(): String {
        postalCode.get()?.let {
            return it
        }
        return ""
    }

    fun isValidProfileData(context: Context): Boolean {
        return if (firstName().isNotBlank() && lastName().isNotBlank() && gender().isNotBlank() && employeeId().isNotBlank()
            && mobile().isNotBlank() && mobile().isMobileNumber() && email().isNotBlank()
        ) {
            true
        } else {
            if (firstName().isBlank()) {
                firstNameError.set(context.getString(R.string.first_name_required))
            }
            if (lastName().isBlank()) {
                lastNameError.set(context.getString(R.string.last_name_required))
            }

            if (employeeId().isBlank()) {
                empIdError.set(context.getString(R.string.emp_id_required))
            }
            if (gender().isBlank()) {
                genderError.set(context.getString(R.string.student_gender_required))
            }
            if (mobile().isBlank()) {
                mobileError.set(context.getString(R.string.parent_mobile_required))
            } else if (!mobile().isMobileNumber()) {
                mobileError.set(context.getString(R.string.valid_mobile_number))
            }
            if (email().isBlank()) {
                emailError.set(context.getString(R.string.enter_email))
            }
            if (!email().isEmailAddress()) {
                emailError.set(context.getString(R.string.valid_email))
            }
            false
        }
    }

    fun setData(profile: Employee) {
        firstName.set(profile.firstName)
        lastName.set(profile.lastName)
        gender.set(profile.gender)
        employeeId.set(profile.employeeId)
        department.set(profile.department)
        designation.set(profile.designation)
        email.set(profile.email)
        mobile.set(profile.phonePrimary)
        houseNo.set(profile.address?.addressLine1)
        street.set(profile.address?.addressLine2)
        city.set(profile.address?.city)
        district.set(profile.address?.district)
        state.set(profile.address?.state)
        country.set(profile.address?.country)
        postalCode.set(profile.address?.postalcode)
    }


    fun getEditedProfileData(profile: Employee):Employee {

        return Employee(
            employeeProfileId = profile.employeeProfileId,
            firstName = firstName(),
            lastName = lastName(),
            gender = gender(),
            employeeId = employeeId(),
            email = email(),
            phonePrimary = nationalMobileNumber(mobile()),
            department = department(),
            designation = designation(),
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
    }

}