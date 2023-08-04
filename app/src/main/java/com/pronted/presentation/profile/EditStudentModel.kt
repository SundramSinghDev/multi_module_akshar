package com.pronted.presentation.profile

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField
import com.pronted.R
import com.pronted.dto.student.AddressModel
import com.pronted.dto.student.StudentContact
import com.pronted.dto.student.StudentProfileAction
import com.pronted.dto.student.StudentProfileResponse
import com.pronted.utils.extentions.getNationalMobileNumber
import com.pronted.utils.extentions.isMobileNumber

class EditStudentModel : BaseObservable() {
    private val firstName = ObservableField<String>()
    private val lastName = ObservableField<String>()
    private val gender = ObservableField<String>()
    private val verified = ObservableField<String>()
    private val bloodGroup = ObservableField<String>()
    private val bloodGroupKey = ObservableField<String>()
    private val admissionNumber = ObservableField<String>()
    private val rollNumber = ObservableField<String>()
    private val dob = ObservableField<String>()
    private val profileImage = ObservableField<String>()

    private val parentName = ObservableField<String>()
    private val relation = ObservableField<String>()
    private val parentEmail = ObservableField<String>()
    private val parentMobile = ObservableField<String>()

    private val houseNo = ObservableField<String>()
    private val street = ObservableField<String>()
    private val city = ObservableField<String>()
    private val district = ObservableField<String>()
    private val state = ObservableField<String>()
    private val country = ObservableField<String>()
    private val postalCode = ObservableField<String>()


    val firstNameError = ObservableField<String>()
    val lastNameError = ObservableField<String>()
    val emailError = ObservableField<String>()
    val genderError = ObservableField<String>()
    val parentNameError = ObservableField<String>()
    val parentMobileError = ObservableField<String>()
    val dobError = ObservableField<String>()


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

    fun getVerified(): ObservableField<String> {

        return verified
    }

    fun setVerified(gender:String){
        this.verified.set(gender)
    }
    fun verified(): String {
        verified.get()?.let {
            return it
        }
        return ""
    }

    fun getBloodGroup(): ObservableField<String> {
        return bloodGroup
    }

    fun bloodGroup(): String {
        bloodGroup.get()?.let {
            return it
        }
        return ""
    }

    fun setBloodGroup(bg: String) {
        bloodGroup.set(bg)
    }

    fun getBloodGroupKey(): ObservableField<String> {
        return bloodGroupKey
    }

    fun setBloodGroupKey(bgKey: Int) {
        bloodGroupKey.set(bgKey.toString())
    }

    fun bloodGroupKey(): Int {
        try {
            bloodGroupKey.get()?.let {
                return it.toInt()
            }
        } catch (ex: java.lang.NumberFormatException) {
            return 0
        }
        return 0
    }

    fun getAdmissionNumber(): ObservableField<String> {
        return admissionNumber
    }

    fun admissionNumber(): String {
        admissionNumber.get()?.let {
            return it
        }
        return ""
    }

    fun getRollNumber(): ObservableField<String> {
        return rollNumber
    }

    fun rollNumber(): String {
        rollNumber.get()?.let {
            return it
        }
        return ""
    }

    fun getDob(): ObservableField<String> {
        dobError.set(null)
        return dob
    }

    fun dob(): String {
        dob.get()?.let {
            return it
        }
        return ""
    }

    fun getParentName(): ObservableField<String> {
        parentNameError.set(null)
        return parentName
    }

    fun parentName(): String {
        parentName.get()?.let {
            return it
        }
        return ""
    }

    fun getRelation(): ObservableField<String> {
        return relation
    }

    fun relation(): String {
        relation.get()?.let {
            return it
        }
        return ""
    }

    fun getParentEmail(): ObservableField<String> {
        emailError.set(null)
        return parentEmail
    }

    fun parentEmail(): String {
        parentEmail.get()?.let {
            return it
        }
        return ""
    }

    fun getParentMobile(): ObservableField<String> {
        parentMobileError.set(null)
        return parentMobile
    }

    private fun parentMobile(): String {
        parentMobile.get()?.let {
            return "+91${it}"
        }
        return ""
    }

    private fun nationalMobileNumber(number: String): String {
        return number.getNationalMobileNumber()
    }

    fun getNumber(): String {
        return nationalMobileNumber(parentMobile())
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
        return if (firstName().isNotBlank() && lastName().isNotBlank() && gender().isNotBlank() && parentName().isNotBlank()
            && parentMobile().isNotBlank() && parentMobile().isMobileNumber() && dob().isNotBlank()
        ) {
            true
        } else {
            if (parentMobile().isBlank()) {
                parentMobileError.set(context.getString(R.string.parent_mobile_required))
            } else if (!parentMobile().isMobileNumber()) {
                parentMobileError.set(context.getString(R.string.valid_mobile_number))
            }
            if (firstName().isBlank()) {
                firstNameError.set(context.getString(R.string.first_name_required))
            }
            if (lastName().isBlank()) {
                lastNameError.set(context.getString(R.string.last_name_required))
            }
            if (gender().isBlank()) {
                genderError.set(context.getString(R.string.student_gender_required))
            }

            if (parentName().isBlank()) {
                parentNameError.set(context.getString(R.string.parent_name_required))
            }
            if (dob().isBlank()) {
                dobError.set(context.getString(R.string.dateofbirth_is_required))
            }
            false
        }
    }

    fun setData(profile: StudentProfileResponse) {
        firstName.set(profile.firstName)
        lastName.set(profile.lastName)
        verified.set(profile.isVerified)
        gender.set(profile.gender)
        admissionNumber.set(profile.admissionNumber)
        rollNumber.set(profile.rollNbr)
        dob.set(profile.dateOfBirth)
        parentName.set(profile.studentContact?.primaryParentName)
        parentEmail.set(profile.studentContact?.primaryParentEmail)
        parentMobile.set(profile.studentContact?.primaryParentMobile)
        relation.set(profile.studentContact?.primaryParentRelationship)
        houseNo.set(profile.studentContact?.address?.addressLine1)
        street.set(profile.studentContact?.address?.addressLine2)
        city.set(profile.studentContact?.address?.city)
        district.set(profile.studentContact?.address?.district)
        state.set(profile.studentContact?.address?.state)
        country.set(profile.studentContact?.address?.country)
        postalCode.set(profile.studentContact?.address?.postalcode)
    }


    fun getEditedProfileData(studentProfile:StudentProfileResponse, classRoomId:Int = 0):StudentProfileResponse {

        return StudentProfileResponse(
            studentProfileId = studentProfile.studentProfileId,
            classroomId = classRoomId,
            firstName = firstName(),
            lastName = lastName(),
            admissionNumber = admissionNumber(),
            rollNbr = rollNumber(),
            dateOfBirth = dob(),
            gender = gender(),
            isVerified = verified(),
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
    }

}