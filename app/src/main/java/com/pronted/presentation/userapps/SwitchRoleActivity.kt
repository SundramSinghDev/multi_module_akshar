package com.pronted.presentation.userapps

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.akshar.store.base.activity.Ced_MainActivity
import com.base.BaseActivity
import com.base.Preference
import com.base.Trace
import com.base.inflateActivity
import com.pronted.R
import com.pronted.databinding.ActivitySwitchRoleBinding
import com.pronted.dto.employee.EmployeeCountAction
import com.pronted.dto.login.*
import com.pronted.dto.student.StudentsCountAction
import com.pronted.presentation.employee.EmployeeViewModel
import com.pronted.presentation.home.SmartParentActivity
import com.pronted.presentation.home.SmartSchoolActivity
import com.pronted.presentation.listener.ItemClickListener
import com.pronted.presentation.profile.StudentViewModel
import com.pronted.utils.extentions.*
import io.paperdb.Paper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import org.koin.androidx.viewmodel.ext.android.viewModel

class SwitchRoleActivity : BaseActivity<ActivitySwitchRoleBinding>() {
    private val viewModel: UserAppsViewModel by viewModel()
    private val imagesViewModel: ImagesViewModel by viewModel()
    private val studentViewModel: StudentViewModel by viewModel()
    private val employeeViewModel: EmployeeViewModel by viewModel()
    private var childrenAdapter: ChildrenAdapter? = null
    private var schoolsAdapter: SchoolsAdapter? = null
    private var childrenList: List<UserAppList> = listOf()
    private var schoolsList: List<UserAppList> = listOf()

    //profile image processing for children
    private var childImageCounter = 0
    private var totalChildImageCount = 0

    //profile image processing for schools
    private var schoolItemCounter = 0
    private var totalSchoolsItemCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            inflateActivity(this, R.layout.activity_switch_role) as ActivitySwitchRoleBinding
        setContentView(binding.root)
        //remove below line to support night mode as well
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        init()
    }

    private fun init() {
        binding.run {
            ivLogout.setOnClickListener {
                logout(this@SwitchRoleActivity, true)
            }
            //TODO for magenative module or akshar module
            magenativeModule.setOnClickListener {
                val intent = Intent(applicationContext, Ced_MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            rvChildren.setHasFixedSize(true)
            rvChildren.layoutManager = LinearLayoutManager(
                this@SwitchRoleActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            childrenAdapter = ChildrenAdapter(this@SwitchRoleActivity, childrenItemClickListener)
            rvChildren.adapter = childrenAdapter
            updateChildrenAdapter()
            (rvChildren.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

            rvSchools.setHasFixedSize(true)
            rvSchools.layoutManager = LinearLayoutManager(
                this@SwitchRoleActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            schoolsAdapter = SchoolsAdapter(this@SwitchRoleActivity, schoolsItemClickListener)
            rvSchools.adapter = schoolsAdapter
            updateSchoolsAdapter()
            (rvSchools.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        }
        Trace.e("Access Token: " + Preference.instance.getString(ACCESS_TOKEN))
        fetchChildrenList()

    }

    private fun updateChildrenAdapter() {
        Paper.book().read<List<UserAppList>>(CHILDREN_LIST)
            ?.let { list ->
                childrenAdapter?.list = list
                binding.hasChildren = list.isNotEmpty()
            }
    }

    private fun updateSchoolsAdapter() {
        Paper.book().read<List<UserAppList>>(SCHOOLS_LIST)
            ?.let { list ->
                schoolsAdapter?.list = list
                binding.hasSchools = list.isNotEmpty()
            }
    }

    private val childrenItemClickListener = object : ItemClickListener<UserAppList> {
        @SuppressLint("NotifyDataSetChanged")
        override fun onClicked(item: UserAppList, positoin: Int) {
            Trace.i("Selected Children id: $item")
            schoolsAdapter?.selectedItemPos = -1
            schoolsAdapter?.notifyDataSetChanged()
            Paper.book().write(USER_SELECTED_ROLE, item)
            startNewActivity(
                this@SwitchRoleActivity,
                SmartParentActivity::class.java,
                clearTop = true,
                finish = true
            )
        }
    }
    private val schoolsItemClickListener = object : ItemClickListener<UserAppList> {
        @SuppressLint("NotifyDataSetChanged")
        override fun onClicked(item: UserAppList, positoin: Int) {
            Trace.i("Selected schools id: $item")
            childrenAdapter?.selectedItemPos = -1
            childrenAdapter?.notifyDataSetChanged()
            Paper.book().write(USER_SELECTED_ROLE, item)
            fetchBoardTypeOfSchool()
        }
    }


    private fun fetchSchoolList() {
        binding.run {
            val mobileNumber = Preference.instance.getString(USER_MOBILE)
            mobileNumber?.let {
                lifecycleScope.launchWhenResumed {
                    showLoader(this@SwitchRoleActivity)
                    viewModel.fetchUserAccessInfo(it, SPECTRUM_ROLE).flowOn(Dispatchers.IO)
                        .collectLatest { userAccessAction ->
                            dismissLoader()
                            when (userAccessAction) {
                                is UserAccessAction.Success -> {
                                    if (userAccessAction.response.isNotEmpty()) {
                                        Trace.i("School list: " + userAccessAction.response.toString())
                                        hasSchools = true
                                        Paper.book().write(SCHOOLS_LIST, userAccessAction.response)
                                        schoolsList = userAccessAction.response
                                        processSchoolsImages()
                                    }
                                }

                                is UserAccessAction.Fail -> {
                                    toast(
                                        userAccessAction.errorResponse.message ?: getString(
                                            R
                                                .string.something_went_wrong_try_again
                                        )
                                    )
                                }
                            }

                        }
                }
            }
        }

    }


    private fun fetchChildrenList() {
        binding.run {
            val mobileNumber = Preference.instance.getString(USER_MOBILE)
            mobileNumber?.let {
                lifecycleScope.launchWhenResumed {
                    showLoader(this@SwitchRoleActivity)
                    viewModel.fetchUserAccessInfo(it, SMART_PARENT).flowOn(Dispatchers.IO)
                        .collectLatest { userAccessAction ->
                            dismissLoader()
                            when (userAccessAction) {
                                is UserAccessAction.Success -> {
                                    if (userAccessAction.response.isNotEmpty()) {
                                        Trace.i("Child List:" + userAccessAction.response.toString())
                                        hasChildren = true
                                        Paper.book().write(CHILDREN_LIST, userAccessAction.response)
                                        childrenList = userAccessAction.response
                                        updateChildrenAdapter()
                                        processChildrenImages()
                                    }
                                    fetchSchoolList()
                                }

                                is UserAccessAction.Fail -> {
                                    toast(
                                        userAccessAction.errorResponse.message ?: getString(
                                            R
                                                .string.something_went_wrong_try_again
                                        )
                                    )
                                    dismissLoader()
                                    fetchSchoolList()
                                    Log.d("Akshar", "Failed")
                                }
                            }

                        }
                }
            }
        }
    }

    private fun fetchBoardTypeOfSchool() {
        lifecycleScope.launchWhenResumed {
            showLoader(this@SwitchRoleActivity)
            viewModel.fetchSchoolBoard().flowOn(Dispatchers.IO)
                .collectLatest { userAccessAction ->
                    dismissLoader()
                    Preference.instance.putString(
                        BOARD_TYPE, SCHOOL
                    )
                    when (userAccessAction) {
                        is SchoolBoardAction.Success -> {
                            if (userAccessAction.response.type.isNotEmpty())
                                Preference.instance.putString(
                                    BOARD_TYPE,
                                    userAccessAction.response.type
                                )
                        }

                        is SchoolBoardAction.Fail -> {
                            Trace.e(
                                userAccessAction.errorResponse.message ?: getString(
                                    R
                                        .string.something_went_wrong_try_again
                                )
                            )
                        }
                    }
                    startNewActivity(
                        this@SwitchRoleActivity,
                        SmartSchoolActivity::class.java,
                        clearTop = true,
                        finish = true
                    )
                }
        }
    }

    private fun processChildrenImages() {
        if (childrenList.isNotEmpty()) {
            totalChildImageCount = childrenList.size
            fetchChildrenProfileImage(childrenList[childImageCounter])
        }
    }

    private fun saveAndProcessNextChildImage() {
        if (childImageCounter < totalChildImageCount - 1) {
            childImageCounter++
            fetchChildrenProfileImage(childrenList[childImageCounter])
        } else {
            childImageCounter = 0
            totalChildImageCount = 0
            Paper.book().write(CHILDREN_LIST, childrenList)
            updateChildrenAdapter()
        }

    }

    private fun processSchoolsImages() {
        if (schoolsList.isNotEmpty()) {
            totalSchoolsItemCount = schoolsList.size
            fetchSchoolProfileImage(schoolsList[schoolItemCounter])
        }
    }

    private fun saveAndProcessNextSchoolImage() {
        if (schoolItemCounter < totalSchoolsItemCount - 1) {
            schoolItemCounter++
            fetchSchoolProfileImage(schoolsList[schoolItemCounter])
        } else {
            schoolItemCounter = 0
            totalSchoolsItemCount = 0
            Paper.book().write(SCHOOLS_LIST, schoolsList)
            updateSchoolsAdapter()
            processStudentCount()
        }

    }

    private fun processStudentCount() {
        schoolItemCounter = 0
        totalSchoolsItemCount = 0
        if (schoolsList.isNotEmpty()) {
            totalSchoolsItemCount = schoolsList.size
            fetchStudentCount(schoolsList[schoolItemCounter])
        }
    }

    private fun saveAndProcessNextSchoolStudentCount() {
        if (schoolItemCounter < totalSchoolsItemCount - 1) {
            schoolItemCounter++
            fetchStudentCount(schoolsList[schoolItemCounter])
        } else {
            schoolItemCounter = 0
            totalSchoolsItemCount = 0
            Paper.book().write(SCHOOLS_LIST, schoolsList)
            updateSchoolsAdapter()
            processEmployeeCount()
        }
    }

    private fun processEmployeeCount() {
        schoolItemCounter = 0
        totalSchoolsItemCount = 0
        if (schoolsList.isNotEmpty()) {
            totalSchoolsItemCount = schoolsList.size
            fetchEmployeeCount(schoolsList[schoolItemCounter])
        }
    }

    private fun saveAndProcessNextSchoolEmployeeCount() {
        if (schoolItemCounter < totalSchoolsItemCount - 1) {
            schoolItemCounter++
            fetchEmployeeCount(schoolsList[schoolItemCounter])
        } else {
            schoolItemCounter = 0
            totalSchoolsItemCount = 0
            Paper.book().write(SCHOOLS_LIST, schoolsList)
            updateSchoolsAdapter()
        }
    }

    private fun fetchChildrenProfileImage(userAppList: UserAppList) {
        lifecycleScope.launchWhenResumed {
            val headers = mapOf(
                Pair("Authorization", Preference.instance.getString(ACCESS_TOKEN) ?: ""),
                Pair("APP_NAME", "SmartParent"),
                Pair("SCHOOL_CODE", userAppList.schoolCode),
                Pair("STUDENT_PROFILE_ID", userAppList.userUniqueId.toString()),
            )
            Preference.instance.putBoolean(DYNAMIC_HEADERS, true)
            imagesViewModel.fetchProfileImage(
                headers,
                STUDENTS,
                userAppList.userUniqueId.toString()
            )
                .collectLatest { imageAction ->
                    Preference.instance.putBoolean(DYNAMIC_HEADERS, false)
                    when (imageAction) {
                        is ImageAction.Success -> {
                            childrenList[childImageCounter].profileImage =
                                imageAction.imageResponse.imageUrl
                            Paper.book().write(CHILDREN_LIST, childrenList)
                            saveAndProcessNextChildImage()
                        }

                        is ImageAction.Fail -> {
                            Trace.i(
                                imageAction.errorResponse.message
                                    ?: getString(R.string.something_went_wrong_try_again)
                            )
                            saveAndProcessNextChildImage()
                        }
                    }
                }
        }
    }

    private fun fetchSchoolProfileImage(userAppList: UserAppList) {
        lifecycleScope.launchWhenResumed {
            val headers = mapOf(
                Pair("Authorization", Preference.instance.getString(ACCESS_TOKEN) ?: ""),
                Pair("APP_NAME", "Spectrum"),
                Pair("SCHOOL_CODE", userAppList.schoolCode),
                Pair("STUDENT_PROFILE_ID", userAppList.userUniqueId.toString()),
            )
            Preference.instance.putBoolean(DYNAMIC_HEADERS, true)
            imagesViewModel.fetchProfileImage(headers, SCHOOL, "logo")
                .collectLatest { imageAction ->
                    Preference.instance.putBoolean(DYNAMIC_HEADERS, false)
                    when (imageAction) {
                        is ImageAction.Success -> {
                            schoolsList[schoolItemCounter].profileImage =
                                imageAction.imageResponse.imageUrl
                            Paper.book().write(SCHOOLS_LIST, schoolsList)
                            saveAndProcessNextSchoolImage()
                        }

                        is ImageAction.Fail -> {
                            Trace.i(
                                imageAction.errorResponse.message
                                    ?: getString(R.string.something_went_wrong_try_again)
                            )
                            saveAndProcessNextSchoolImage()
                        }
                    }
                }
        }
    }

    private fun fetchStudentCount(userAppList: UserAppList) {
        lifecycleScope.launchWhenResumed {
            val headers = mapOf(
                Pair("Authorization", Preference.instance.getString(ACCESS_TOKEN) ?: ""),
                Pair("APP_NAME", "Spectrum"),
                Pair("SCHOOL_CODE", userAppList.schoolCode),
                Pair("STUDENT_PROFILE_ID", userAppList.userUniqueId.toString()),
            )
            Preference.instance.putBoolean(DYNAMIC_HEADERS, true)
            studentViewModel.fetchStudentsCount(headers)
                .collectLatest { countAction ->
                    Preference.instance.putBoolean(DYNAMIC_HEADERS, false)
                    when (countAction) {
                        is StudentsCountAction.Success -> {
                            schoolsList[schoolItemCounter].studentCount = countAction.response.value
                            Paper.book().write(SCHOOLS_LIST, schoolsList)
                            saveAndProcessNextSchoolStudentCount()
                        }

                        is StudentsCountAction.Fail -> {
                            Trace.i(
                                countAction.errorResponse.message
                                    ?: getString(R.string.something_went_wrong_try_again)
                            )
                            saveAndProcessNextSchoolStudentCount()
                        }
                    }
                }
        }
    }

    private fun fetchEmployeeCount(userAppList: UserAppList) {
        lifecycleScope.launchWhenResumed {
            val headers = mapOf(
                Pair("Authorization", Preference.instance.getString(ACCESS_TOKEN) ?: ""),
                Pair("APP_NAME", "Spectrum"),
                Pair("SCHOOL_CODE", userAppList.schoolCode),
                Pair("STUDENT_PROFILE_ID", userAppList.userUniqueId.toString()),
            )
            Preference.instance.putBoolean(DYNAMIC_HEADERS, true)
            employeeViewModel.fetchEmployeeCount(headers)
                .collectLatest { countAction ->
                    Preference.instance.putBoolean(DYNAMIC_HEADERS, false)
                    when (countAction) {
                        is EmployeeCountAction.Success -> {
                            schoolsList[schoolItemCounter].employeeCount =
                                countAction.response.value
                            Paper.book().write(SCHOOLS_LIST, schoolsList)
                            saveAndProcessNextSchoolEmployeeCount()
                        }

                        is EmployeeCountAction.Fail -> {
                            Trace.i(
                                countAction.errorResponse.message
                                    ?: getString(R.string.something_went_wrong_try_again)
                            )
                            saveAndProcessNextSchoolEmployeeCount()
                        }
                    }
                }
        }
    }

}