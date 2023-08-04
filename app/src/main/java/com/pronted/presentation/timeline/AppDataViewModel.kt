package com.pronted.presentation.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pronted.dto.employee.Employee
import com.pronted.dto.student.StudentProfileResponse

class AppDataViewModel : ViewModel() {
    private val mutableBirthDaysCount = MutableLiveData<Int>()
    private val mutableSecurityGroups = MutableLiveData<ArrayList<String>>()
    private val mutableStudentProfile = MutableLiveData<StudentProfileResponse>()
    private val mutableStudentProfileImage = MutableLiveData<String>()
    private val mutableEmployeeList = MutableLiveData<ArrayList<Employee>>()

    val birthDaysCount: LiveData<Int> get() = mutableBirthDaysCount
    val securityGroups: LiveData<ArrayList<String>> get() = mutableSecurityGroups
    val studentProfile: LiveData<StudentProfileResponse> get() = mutableStudentProfile
    val studentProfileImage: LiveData<String> get() = mutableStudentProfileImage
    val employeesList: LiveData<ArrayList<Employee>> get() = mutableEmployeeList

    fun setBirthdaysCount(count: Int) {
        mutableBirthDaysCount.value = count
    }

    fun setSecurityGroups(list: ArrayList<String>) {
        mutableSecurityGroups.value = list
    }

    fun setStudentProfile(profile: StudentProfileResponse) {
        mutableStudentProfile.value = profile
    }

    fun setStudentProfileImage(profileImage: String) {
        mutableStudentProfileImage.value = profileImage
    }

    fun setEmployees(employees: ArrayList<Employee>) {
        mutableEmployeeList.value = employees
    }
}