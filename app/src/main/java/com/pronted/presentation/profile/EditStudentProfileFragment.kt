package com.pronted.presentation.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
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
import androidx.lifecycle.lifecycleScope
import com.base.*
import com.base.permission.Permission
import com.base.permission.PermissionCallback
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.isseiaoki.simplecropview.CropImageView
import com.pronted.R
import com.pronted.databinding.DialogImagePickBinding
import com.pronted.databinding.FragmentEditStudentProfileBinding
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

class EditStudentProfileFragment : BaseFragment<FragmentEditStudentProfileBinding>() {
    private var studentProfile: StudentProfileResponse? = null
    private val editStudentModel: EditStudentModel by lazy {
        EditStudentModel()
    }
    private val materialDateBuilder by lazy {
        MaterialDatePicker.Builder.datePicker()
    }
    private val imagesViewModel: ImagesViewModel by viewModel()
    private val studentViewModel: StudentViewModel by viewModel()

    private var statesList: List<String> = listOf()
    private var bloodGroupDropdownList: List<String> = listOf()
    private var classRoomId = 0
    private var sectionItem: SectionItem? = null

    private val cameraPermissionHandler: CameraPermissionHandler by lazy {
        CameraPermissionHandler(context, cameraHandler)
    }
    private val storagePermissionHandler: StoragePermissionHandler by lazy {
        StoragePermissionHandler(context, storageHandler)
    }

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
            inflater, container, R.layout.fragment_edit_student_profile
        ) as FragmentEditStudentProfileBinding
        return binding.root
    }

    override fun create(savedInstanceState: Bundle?) {
        registerActivityResult(this@EditStudentProfileFragment)
    }

    override fun init() {
        binding.run {
            studentProfile = arguments?.getSerializable(NAV_OBJECT) as StudentProfileResponse
            studentProfile?.let {
                editStudentModel.setData(it)
                fetchProfileImage(it.studentProfileId.toString())
                if (it.isVerified == "Y") {
                    cbVerified.isChecked = true
                    cbVerified.isEnabled = false
                } else {
                    cbVerified.isChecked = false
                    cbVerified.isEnabled = true
                }
                if (it.gender.equals("Male", true)) {
                    male.isChecked = true
                } else {
                    female.isChecked = true
                }
                Trace.e("Blood group: " + it.bloodGroup)
                binding.bloodGroupDropDown.setText(
                    it.bloodGroup,
                    false
                )
                binding.stateDropDown.setText(it.studentContact?.address?.state)
            }
            cbVerified.setOnCheckedChangeListener { _, isChecked ->
                    editStudentModel.setVerified(if(isChecked) "Y" else "N")
            }
            genderGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.male -> editStudentModel.setGender(getString(R.string.male))
                    R.id.female -> editStudentModel.setGender(getString(R.string.female))
                }
            }
            editStudent = editStudentModel
            setDateTouchListener(paymentDate)
            binding.ivImageEdit.setOnClickListener {
                openPickImageDialog()
            }
            fetchBloodGroups()
            binding.bloodGroupDropDown.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    editStudentModel.setBloodGroup(bloodGroupDropdownList[position])
                }
            statesList = states.toList()
            binding.stateDropDown.setAdapter(getDropdownAdapter(statesList))
            binding.stateDropDown.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, id ->
                    editStudentModel.setState(statesList[position])
                }
            saveProfile.setOnClickListener {
                context?.let {
                    if (editStudentModel.isValidProfileData(it)) {
                        studentProfile?.let { profile ->
                            if (classRoomId == 0) {
                                classRoomId = if (profile.classroom?.classroomId != null)
                                    profile.classroom.classroomId!!
                                else
                                    sectionItem?.classroomId!!
                            }
                            updateProfileData(
                                editStudentModel.getEditedProfileData(
                                    profile,
                                    classRoomId
                                )
                            )
                        }

                    }
                }

            }
        }
    }

    private fun updateProfileData(editedProfileData: StudentProfileResponse) {
        lifecycleScope.launchWhenResumed {
            showLoader()
            studentViewModel.updateStudentProfile(editedProfileData).collectLatest {
                dismissLoader()
                when(it){
                    is UpdateStudentProfileAction.Success ->{
                        toast("User Profile Updated Successfully")
                        (context as ChildActivity).onBack()
                    }
                    is UpdateStudentProfileAction.Fail ->{
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
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setDateTouchListener(v: TextInputEditText) {
        v.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                showDatePicker()
            }
            false
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val date = binding.paymentDate.text.toString()
        date.convertToDate(DateUtil.y4M2d2)?.let {
            calendar.time = it.setTo2359()
        }

        materialDateBuilder.setSelection(calendar.timeInMillis)
        val materialDatePicker = materialDateBuilder.build()
        materialDatePicker.addOnPositiveButtonClickListener {
            binding.paymentDate.setText(
                DateUtil.y4M2d2.format(
                    it.toDate(
                        convert = true
                    )
                )
            )
        }
        materialDatePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER")
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
        EasyImage.openGallery(this@EditStudentProfileFragment, IMG_RESULT)
    }

    private fun capture() {
        Trace.e("Capture")
        EasyImage.openCamera(this@EditStudentProfileFragment, CAMERA_REQUEST)
    }


    private fun fetchProfileImage(profileId: String) {
        lifecycleScope.launchWhenResumed {
            imagesViewModel.fetchProfileImage(
                module = STUDENTS,
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

    private fun fetchBloodGroups() {
        lifecycleScope.launchWhenResumed {
            studentViewModel.fetchBloodGroups(
            ).collectLatest { imageAction ->
                when (imageAction) {
                    is BloodGroupAction.Success -> {
                        Trace.e(imageAction.bloodGroups.toString())
                        studentProfile?.bloodGroup?.let {
                            for (bloodGroup in imageAction.bloodGroups) {
                                if (it == bloodGroup.value) {
                                    editStudentModel.setBloodGroupKey(bloodGroup.lookupSetupId)
                                    break
                                }
                            }
                        }

                        val map  =
                            imageAction.bloodGroups.associate { it.lookupSetupId to it.value }
                        bloodGroupDropdownList = map.values.toList()
                        binding.bloodGroupDropDown.setAdapter(
                            getDropdownAdapter(
                                bloodGroupDropdownList
                            )
                        )

                    }
                    is BloodGroupAction.Fail -> {
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
                Pair("APP_NAME", Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.appName ?: ""),
                Pair(
                    "SCHOOL_CODE",
                    Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.schoolCode ?: ""
                ),
                Pair(
                    "SCHOOL_CODE",
                    Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.schoolCode ?: ""
                ),
                Pair("MOBILE", studentProfile?.studentContact?.primaryParentMobile ?: "")
            )
            Preference.instance.putBoolean(DYNAMIC_HEADERS, true)
            imagesViewModel.uploadProfilePhoto(
                module = STUDENTS,
                profileId = studentProfile?.studentProfileId.toString(),
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
                                this@EditStudentProfileFragment, bundle = bundleOf(
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
}