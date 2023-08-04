package com.pronted.presentation.employee

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.media.tv.SectionResponse
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.Constraints
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.base.*
import com.base.permission.Permission
import com.base.permission.PermissionCallback
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.isseiaoki.simplecropview.CropImageView
import com.pronted.R
import com.pronted.databinding.DialogImagePickBinding
import com.pronted.databinding.FragmentEditEmployeeProfileBinding
import com.pronted.databinding.FragmentEditStudentProfileBinding
import com.pronted.dto.employee.Employee
import com.pronted.dto.employee.EmployeeDepartmentAction
import com.pronted.dto.employee.UpdateEmployeeProfileAction
import com.pronted.dto.login.ImageAction
import com.pronted.dto.login.UploadImageAction
import com.pronted.dto.login.UserAppList
import com.pronted.dto.student.BloodGroupAction
import com.pronted.dto.student.SectionItem
import com.pronted.dto.student.StudentProfileResponse
import com.pronted.dto.student.UpdateStudentProfileAction
import com.pronted.presentation.handler.CameraPermissionHandler
import com.pronted.presentation.handler.PermissionHandler
import com.pronted.presentation.handler.StoragePermissionHandler
import com.pronted.presentation.home.ChildActivity
import com.pronted.presentation.profile.EditStudentModel
import com.pronted.presentation.profile.StudentViewModel
import com.pronted.presentation.timeline.AppDataViewModel
import com.pronted.presentation.userapps.ImagesViewModel
import com.pronted.utils.DateUtil
import com.pronted.utils.convertToDate
import com.pronted.utils.databinding.loadImage
import com.pronted.utils.extentions.*
import com.pronted.utils.setTo2359
import com.pronted.utils.toDate
import io.paperdb.Paper
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class EditEmployeeProfileFragment : BaseFragment<FragmentEditEmployeeProfileBinding>() {
    private var employeeProfile: Employee? = null
    private val editEmployeeModel: EditEmployeeModel by lazy {
        EditEmployeeModel()
    }
    private val materialDateBuilder by lazy {
        MaterialDatePicker.Builder.datePicker()
    }
    private val imagesViewModel: ImagesViewModel by viewModel()
    private val employeeViewModel: EmployeeViewModel by viewModel()
    private val appDataViewModel: AppDataViewModel by activityViewModels()
    private var employeeList = arrayListOf<Employee>()
    private var statesList: List<String> = listOf()
    private var position = 0

    private val cameraPermissionHandler: CameraPermissionHandler by lazy {
        CameraPermissionHandler(context, cameraHandler)
    }
    private val storagePermissionHandler: StoragePermissionHandler by lazy {
        StoragePermissionHandler(context, storageHandler)
    }
    private var departmentList: List<String> = listOf()
    private var designationList: List<String> = listOf()

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private fun registerActivityResult(fragment: Fragment) {
        activityResultLauncher = fragment.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(), activityResultCallback
        )
    }

    private val activityResultCallback: ActivityResultCallback<ActivityResult> =
        ActivityResultCallback {
            if (it.resultCode == CROP_IMAGE_REQUEST) {
                it.data?.extras?.let { bundle ->
                    bundle.getInt(IMAGE_TYPE).let { it1 ->
                        bundle.getString(IMAGE_PATH)?.let { it2 ->
                            Trace.e("Image path" + it2)
                            uploadProfileImage(it2)
                        }
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_edit_employee_profile
        ) as FragmentEditEmployeeProfileBinding
        return binding.root
    }

    override fun create(savedInstanceState: Bundle?) {
        registerActivityResult(this@EditEmployeeProfileFragment)
    }

    override fun init() {
        binding.run {
            employeeProfile = arguments?.getSerializable(NAV_OBJECT) as Employee
            position = arguments?.getInt(NAV_OBJECT2) ?: 0
            employeeProfile?.let {
                editEmployeeModel.setData(it)
                fetchProfileImage(it.employeeId)
                if (it.gender.equals("Male", true)) {
                    male.isChecked = true
                } else {
                    female.isChecked = true
                }

                binding.stateDropDown.setText(it.address?.state)
                binding.departmentDropDown.setText(it.department)
                binding.designationDropDown.setText(it.designation)
            }
            genderGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.male -> editEmployeeModel.setGender(getString(R.string.male))
                    R.id.female -> editEmployeeModel.setGender(getString(R.string.female))
                }
            }
            editEmployee = editEmployeeModel
            binding.ivImageEdit.setOnClickListener {
                openPickImageDialog()
            }

            statesList = states.toList()
            binding.stateDropDown.setAdapter(getDropdownAdapter(statesList))
            binding.stateDropDown.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    editEmployeeModel.setState(statesList[position])
                }

            binding.departmentDropDown.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, id ->
                    editEmployeeModel.setDepartment(departmentList[position])
                }
            binding.designationDropDown.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    editEmployeeModel.setDesignation(designationList[position])
                }
            fetchEmployeeDesignations()
            fetchEmployeeDepartments()
            saveProfile.setOnClickListener {
                context?.let {
                    if (editEmployeeModel.isValidProfileData(it)) {
                        employeeProfile?.let { profile ->
                            Trace.e(
                                "Edited profile: " + (editEmployeeModel.getEditedProfileData(
                                    profile
                                ))
                            )
                            updateProfileData(
                                editEmployeeModel.getEditedProfileData(
                                    profile
                                )
                            )
                        }

                    }
                }

            }
        }
    }

    private fun updateProfileData(editedProfileData: Employee) {
        lifecycleScope.launchWhenResumed {
            showLoader()
            employeeViewModel.updateEmployeeProfile(editedProfileData).collectLatest {
                dismissLoader()
                when (it) {
                    is UpdateEmployeeProfileAction.Success -> {
                        toast("Profile Updated Successfully")
                        if(employeeList.isNotEmpty() && position < employeeList.size){
                            employeeList[position] = editedProfileData
                            appDataViewModel.setEmployees(employeeList)
                        }
                        (context as ChildActivity).onBack()
                    }
                    is UpdateEmployeeProfileAction.Fail -> {
                        toast(
                            it.errorResponse.message
                                ?: getString(R.string.something_went_wrong_try_again)
                        )
                    }
                }
            }
        }
    }

    private fun openPickImageDialog() {
        context?.let { context ->
            val imagePickBinding = DialogImagePickBinding.inflate(layoutInflater)
            val dialog =
                Dialog(context).apply {
                    setContentView(imagePickBinding.root)
                    window?.setLayout(
                        Constraints.LayoutParams.MATCH_PARENT,
                        Constraints.LayoutParams.WRAP_CONTENT
                    )
                }

            dialog.show()
            imagePickBinding.run {
                imageAvailable = false
                imgCancel.setOnClickListener {
                    dialog.dismiss()
                }
                pickImage.setOnClickListener {
                    dialog.dismiss()
                    if (storagePermissionHandler.isPermissionGranted()) {
                        pick()
                    } else {
                        storagePermissionHandler.requestPermission()
                    }
                }
                launchCamera.setOnClickListener {
                    dialog.dismiss()
                    if (cameraPermissionHandler.isPermissionGranted()) {
                        capture()
                    } else {
                        cameraPermissionHandler.requestPermission()
                    }
                }
            }
        }
    }

    override fun resume() {
        (activity as BaseActivity<*>).title(getString(R.string.edit_profile))
        appDataViewModel.employeesList.observe(this) {
            employeeList = it
            Trace.i("List count" + employeeList.size + "position: " + position)
        }
    }


    private val cameraHandler = object : PermissionHandler {
        override fun request(
            permission: Permission,
            permissionCallback: PermissionCallback
        ) {
            requestPermission(permission, permissionCallback)
        }

        override fun permissionGranted(permission: Permission) {
            capture()
        }
    }

    private val storageHandler = object : PermissionHandler {
        override fun request(
            permission: Permission,
            permissionCallback: PermissionCallback
        ) {
            requestPermission(permission, permissionCallback)
        }

        override fun permissionGranted(permission: Permission) {
            pick()
        }
    }

    private fun pick() {
        Trace.e("Pick")
        EasyImage.openGallery(this@EditEmployeeProfileFragment, IMG_RESULT)
    }

    private fun capture() {
        Trace.e("Capture")
        EasyImage.openCamera(this@EditEmployeeProfileFragment, CAMERA_REQUEST)
    }


    private fun fetchProfileImage(profileId: String) {
        lifecycleScope.launchWhenResumed {
            imagesViewModel.fetchProfileImage(
                module = EMPLOYEES,
                profileId = profileId
            ).collectLatest { imageAction ->
                when (imageAction) {
                    is ImageAction.Success -> {
                        Trace.e(imageAction.imageResponse.imageUrl)
                        //employeeList[position].imageUrl = imageAction.imageResponse.imageUrl ?: ""
                        loadProfileImage(imageAction.imageResponse.imageUrl)

                    }
                    is ImageAction.Fail -> {
                        Trace.i(
                            imageAction.errorResponse.message
                                ?: getString(R.string.something_went_wrong_try_again)
                        )
                    }
                }
            }
        }
    }


    private fun getDropdownAdapter(list: List<String>): ArrayAdapter<String>? {
        return context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_list_item_1,
                list
            )
        }
    }

    private fun uploadProfileImage(imageUrl: String) {
        lifecycleScope.launchWhenResumed {
            val headers = mapOf(
                Pair("Authorization", Preference.instance.getString(ACCESS_TOKEN) ?: ""),
                Pair("APP_NAME", "SmartParent"),
                Pair(
                    "SCHOOL_CODE",
                    Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.schoolCode ?: ""
                )
                //Pair("MOBILE", employeeProfile?.studentContact?.primaryParentMobile ?: "")
            )
            Preference.instance.putBoolean(DYNAMIC_HEADERS, true)
            imagesViewModel.uploadProfilePhoto(
                module = STUDENTS,
                profileId = employeeProfile?.employeeId.toString(),
                headers = headers,
                imageUrl = File(imageUrl).imageContentType()
            ).collectLatest { imageAction ->
                when (imageAction) {
                    is UploadImageAction.Success -> {
                        Trace.e(imageAction.imageResponse.imageUrl)
                        //employeeList[position].imageUrl = imageAction.imageResponse.imageUrl ?: ""
                        loadProfileImage(imageAction.imageResponse.imageUrl)

                    }
                    is UploadImageAction.Fail -> {
                        Trace.i(
                            imageAction.errorResponse.message
                                ?: getString(R.string.something_went_wrong_try_again)
                        )
                    }
                }
            }
        }
    }

    private fun loadProfileImage(imageUrl: String?) {
        context?.let {
            AppCompatResources.getDrawable(it, R.drawable.ic_user)
                ?.let { default ->
                    loadImage(binding.ivProfile, imageUrl, default)
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        EasyImage.handleActivityResult(
            requestCode,
            resultCode,
            data,
            context as Activity,
            object : DefaultCallback() {
                override fun onImagesPicked(
                    imageFiles: MutableList<File>,
                    source: EasyImage.ImageSource,
                    type: Int
                ) {
                    if (imageFiles.size > 0) {
                        context?.let {
                            cropImage(
                                this@EditEmployeeProfileFragment, bundle = bundleOf(
                                    Pair(IMAGE_PATH, imageFiles[0].absolutePath),
                                    Pair(IMAGE_CROP_MODE, CropImageView.CropMode.SQUARE.id)
                                ), requestForResult = true, launcher = activityResultLauncher
                            )
                        }
                    } else {
                        showSnackBar(getString(R.string.failed_try_again))
                    }
                }

                override fun onImagePickerError(
                    e: Exception?,
                    source: EasyImage.ImageSource?,
                    type: Int
                ) {
                    Trace.e(e?.message)
                    Trace.a()
                }

                override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
                    if (source == EasyImage.ImageSource.CAMERA) {
                        context?.let { EasyImage.lastlyTakenButCanceledPhoto(it) }?.delete()
                    }
                }
            })
    }

    private fun fetchEmployeeDepartments() {
        lifecycleScope.launchWhenResumed {
            employeeViewModel.fetchEmployeeDepartments(EMP_DEPARTMENT)
                .collectLatest { imageAction ->
                    when (imageAction) {
                        is EmployeeDepartmentAction.Success -> {
                            Trace.e(imageAction.response.toString())
                            val map =
                                imageAction.response.associate { it.lookupSetupId to it.value }
                            departmentList = map.values.toList()
                            binding.departmentDropDown.setAdapter(getDropdownAdapter(departmentList.toList()))
                        }
                        is EmployeeDepartmentAction.Fail -> {
                            Trace.i(
                                imageAction.errorResponse.message
                                    ?: getString(R.string.something_went_wrong_try_again)
                            )
                        }
                    }
                }
        }
    }

    private fun fetchEmployeeDesignations() {
        lifecycleScope.launchWhenResumed {
            employeeViewModel.fetchEmployeeDepartments(EMP_DESIGNATION)
                .collectLatest { imageAction ->
                    when (imageAction) {
                        is EmployeeDepartmentAction.Success -> {
                            Trace.e(imageAction.response.toString())
                            val map =
                                imageAction.response.associate { it.lookupSetupId to it.value }
                            designationList = map.values.toList()
                            binding.designationDropDown.setAdapter(
                                getDropdownAdapter(
                                    designationList.toList()
                                )
                            )
                        }
                        is EmployeeDepartmentAction.Fail -> {
                            Trace.i(
                                imageAction.errorResponse.message
                                    ?: getString(R.string.something_went_wrong_try_again)
                            )
                        }
                    }
                }
        }
    }
}