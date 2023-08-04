package com.pronted.dto.employee

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pronted.dto.student.AddressModel
import java.io.Serializable

data class Employee(
    @SerializedName("isSelected")
    @Expose
    var isSelected: Boolean = false,
    @SerializedName("employeeProfileId")
    @Expose
    var employeeProfileId: Int = 0,
    @SerializedName("employeeId")
    @Expose
    var employeeId: String = "",
    @SerializedName("namePrefix")
    @Expose
    var namePrefix: String = "",
    @SerializedName("firstName")
    @Expose
    var firstName: String = "",
    @SerializedName("middleName")
    @Expose
    var middleName: String = "",
    @SerializedName("lastName")
    @Expose
    var lastName: String = "",
    @SerializedName("department")
    @Expose
    var department: String = "",
    @SerializedName("designation")
    @Expose
    var designation: String = "",
    @SerializedName("gender")
    @Expose
    var gender: String = "",
    @SerializedName("phonePrimary")
    @Expose
    var phonePrimary: String = "",
    @SerializedName("email")
    @Expose
    var email: String = "",
    @SerializedName("imageUrl")
    @Expose
    var imageUrl: String = "",
    @SerializedName("fullName")
    @Expose
    var fullName: String = "",
    @SerializedName("address")
    @Expose
    var address: AddressModel? = null,
    @SerializedName("emailList")
    @Expose
    var emailList: ArrayList<String> = arrayListOf(),
    @SerializedName("primaryPhoneList")
    @Expose
    var primaryPhoneList: ArrayList<String> = arrayListOf()
) : Serializable

data class EmployeeDepartment(
    @SerializedName("lookupSetupId")
    @Expose
    var lookupSetupId: Int = 0,
    @SerializedName("fieldName")
    @Expose
    var fieldName: String = "",
    @SerializedName("module")
    @Expose
    var module: String = "",
    @SerializedName("value")
    @Expose
    var value: String = "",
    @SerializedName("isActive")
    @Expose
    var isActive: Boolean = false,
    @SerializedName("isSelected")
    @Expose
    var isSelected: Boolean = false,
    @SerializedName("selectedIndex")
    @Expose
    var selectedIndex: Int = -1
) : Serializable


data class FeeTemp(
    @SerializedName("id")
    @Expose
    var id: String = "",

    @SerializedName("templateName")
    @Expose
    var templateName: String = "",

    @SerializedName("headerLine1")
    @Expose
    var headerLine1: String = "",

    @SerializedName("headerLine2")
    @Expose
    var headerLine2: String = "",

    @SerializedName("headerLine3")
    @Expose
    var headerLine3: String = "",

    @SerializedName("templateType")
    @Expose
    var templateType: String = ""
) : Serializable


data class EmployeeCount(
    @SerializedName("value")
    @Expose
    var value: Int = 0
) : Serializable

data class Invoice(
    @SerializedName("url")
    @Expose
    var url: String? = ""
):Serializable