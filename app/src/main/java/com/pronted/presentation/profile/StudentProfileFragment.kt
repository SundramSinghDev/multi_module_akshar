package com.pronted.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.base.*
import com.pronted.R
import com.pronted.databinding.FragmentStudentProfileBinding
import com.pronted.dto.employee.Employee
import com.pronted.dto.login.ImageAction
import com.pronted.dto.login.UserAppList
import com.pronted.dto.student.StudentProfileAction
import com.pronted.dto.student.StudentProfileResponse
import com.pronted.dto.student.StudentsAction
import com.pronted.presentation.home.ChildActivity
import com.pronted.presentation.userapps.ImagesViewModel
import com.pronted.utils.DateUtil
import com.pronted.utils.SecurityResponseLabel
import com.pronted.utils.convertToDate
import com.pronted.utils.databinding.loadImage
import com.pronted.utils.databinding.parseTextWithValue
import com.pronted.utils.extentions.*
import com.pronted.utils.toDateString
import io.paperdb.Paper
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class StudentProfileFragment : BaseFragment<FragmentStudentProfileBinding>() {

    private val imagesViewModel: ImagesViewModel by viewModel()
    private val studentViewModel: StudentViewModel by viewModel()
    private var students = arrayListOf<StudentProfileResponse>()
    private lateinit var studentModel: StudentProfileResponse
    private var position = 0
    private var rightAnimation = false
    private var isEdited = false
    private var classRoomId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_student_profile
        ) as FragmentStudentProfileBinding
        return binding.root
    }

    override fun init() {
        binding.run {
            if (Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.appName == SMART_PARENT) {
                fetchStudentProfile()
                clNavigation.visibility = View.GONE
            } else {
                classRoomId = arguments?.getInt(NAV_OBJECT3) ?: 0
                position = arguments?.getInt(NAV_OBJECT2) ?: 0
                students =
                    (arguments?.getSerializable(NAV_OBJECT) as ArrayList<StudentProfileResponse>?)
                        ?: arrayListOf()
                studentModel = students[position]
                Paper.book().read<ArrayList<String>>(SECURITY_GROUP_LIST)
                    ?.let { securityGroupList ->
                        binding.tvEdit.visibility =
                            if (securityGroupList.contains(SecurityResponseLabel.ME_STUDENT_PROFILE_EDIT)) View.VISIBLE else View.GONE
                    }
                loadProfileData()
                updateNavigation()
                previous.setOnClickListener {
                    position = --position
                    rightAnimation = false
                    studentModel = students[position]
                    loadProfileData()
                    updateNavigation()
                }
                next.setOnClickListener {
                    position = ++position
                    rightAnimation = true
                    studentModel = students[position]
                    loadProfileData()
                    updateNavigation()
                }
            }
            tvEdit.setOnClickListener {

                context?.let {
                    isEdited = true
                    val bundle = bundleOf(
                        Pair(NAV_DESTINATION, R.id.editStudentProfile),
                        Pair(NAV_OBJECT, studentModel)
                    )
                    if (it is ChildActivity) {
                        (context as ChildActivity).getNavController()
                            .navigate(R.id.editStudentProfile, bundle)
                    } else {
                        startNewActivity(
                            it, ChildActivity::class.java, bundle = bundle
                        )
                    }
                }
            }
        }
    }


    override fun create(savedInstanceState: Bundle?) {
        Trace.e("Oncreate")
    }

    override fun resume() {
        (activity as BaseActivity<*>).title(getString(R.string.student_profile))
        if (isEdited) {
            if (Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.appName == SMART_PARENT) {
                fetchStudentProfile()
            } else {
                if (classRoomId != 0)
                    fetchStudentList(classRoomId)
            }
            isEdited = false
        }
    }


    private fun loadProfileData() {
        binding.run {
            context?.let {
                student = studentModel
                tvProfileName.text = studentModel.fullName.capitalizeWords()
                if (studentModel.isVerified == "Y") {
                    tvVerified.text = getString(R.string.verified)
                    ivVerified.setImageResource(R.drawable.ic_baseline_check_circle_24)
                } else {
                    tvVerified.text = getString(R.string.not_verified)
                    ivVerified.setImageResource(R.drawable.ic_baseline_warning_24)
                }
                parseTextWithValue(
                    tvDob,
                    getString(R.string.format_dob),
                    studentModel.dateOfBirth?.convertToDate(DateUtil.y4M2d2)?.toDateString(
                        DateUtil.m3D2Y4
                    )
                )
                val animation = AnimationUtils.loadAnimation(
                    it,
                    if (rightAnimation) R.anim.left_slide else R.anim.right_slide
                )
                clDetails1.animation = animation
                clDetails2.animation = animation
                clDetails3.animation = animation
                if (studentModel.imageUrl.isNullOrEmpty())
                    fetchProfileImage(studentModel.studentProfileId.toString())
                else
                    loadProfileImage(studentModel.imageUrl ?: "")
            }
        }
    }

    private fun updateNavigation() {
        binding.run {
            context?.let {
                if (students.isNotEmpty() && students.size > 1) {
                    clNavigation.visibility = View.VISIBLE
                    if (position == 0) {
                        previous.isEnabled = false
                        previous.background = ResourcesCompat.getDrawable(
                            resources, R.drawable.btn_secondary_background_disabled, it.theme
                        )
                    } else if (position == students.size - 1) {
                        next.isEnabled = false
                        next.background = ResourcesCompat.getDrawable(
                            resources, R.drawable.btn_secondary_background_disabled, it.theme
                        )
                    } else {
                        previous.isEnabled = true
                        previous.background = ResourcesCompat.getDrawable(
                            resources, R.drawable.btn_secondary_background, it.theme
                        )
                        next.isEnabled = true
                        next.background = ResourcesCompat.getDrawable(
                            resources, R.drawable.btn_secondary_background, it.theme
                        )
                    }

                } else {
                    clNavigation.visibility = View.GONE
                }

            }
        }

    }

    /**
     * Fetch student profile
     */
    private fun fetchStudentProfile() {
        lifecycleScope.launchWhenResumed {
            showLoader()
            Preference.instance.putBoolean(DYNAMIC_HEADERS, false)
            studentViewModel.fetchStudentProfile()
                .collectLatest { profileAction ->
                    dismissLoader()
                    when (profileAction) {
                        is StudentProfileAction.Success -> {
                            Trace.e("Student profile: " + profileAction.studentProfileResponse)
                            studentModel = profileAction.studentProfileResponse
                            loadProfileData()
                            fetchProfileImage(studentModel.studentProfileId.toString())
                        }
                        is StudentProfileAction.Fail -> {
                            Trace.i(
                                profileAction.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                        }
                    }
                }
        }
    }

    private fun loadProfileImage(url: String = "") {
        binding.run {
            context?.let {
                if (url.isNotEmpty()) {
                    ivProfile.visibility = View.VISIBLE
                    ivTextProfile.visibility = View.GONE
                    AppCompatResources.getDrawable(it, R.drawable.ic_user)
                        ?.let { default ->
                            loadImage(ivProfile, url, default)
                        }
                } else {
                    //fetchProfileImage(studentModel.studentProfileId.toString())
                    ivProfile.visibility = View.GONE
                    ivTextProfile.visibility = View.VISIBLE
                    tvShortName.text =
                        studentModel.fullName.substring(0, 2).uppercase()
                    when (position % 3) {
                        0 -> {
                            ivTextBg.setImageResource(R.color.light_pink)
                            tvShortName.setTextColor(it.getColor(R.color.pink))
                        }
                        1 -> {
                            ivTextBg.setImageResource(R.color.light_blue1)
                            tvShortName.setTextColor(it.getColor(R.color.light_blue))
                        }
                        else -> {
                            ivTextBg.setImageResource(R.color.light_green1)
                            tvShortName.setTextColor(it.getColor(R.color.green_normal))
                        }

                    }
                }
            }
        }
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
                        loadProfileImage(imageAction.imageResponse.imageUrl ?: "")

                    }
                    is ImageAction.Fail -> {
                        Trace.i(
                            imageAction.errorResponse.message
                                ?: getString(R.string.something_went_wrong_try_again)
                        )
                        loadProfileImage()
                    }
                }
            }
        }
    }

    /**
     * Fetch student list
     */
    private fun fetchStudentList(classroomId: Int) {
        lifecycleScope.launchWhenResumed {
            showLoader()
            Preference.instance.putBoolean(DYNAMIC_HEADERS, false)
            studentViewModel.fetchStudentsByClassroomId(classroomId)
                .collectLatest { studentsAction ->
                    dismissLoader()
                    when (studentsAction) {
                        is StudentsAction.Success -> {
                            Trace.e("Students list: " + studentsAction.students)
                            students = studentsAction.students
                            studentModel = students[position]
                            loadProfileData()
                        }
                        is StudentsAction.Fail -> {
                            Trace.i(
                                studentsAction.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                        }
                    }
                }
        }
    }


}