package com.pronted.presentation.timetable

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.BaseFragment
import com.base.Preference
import com.base.Trace
import com.base.inflateFragment
import com.pronted.R
import com.pronted.databinding.DialogClassAndSectionBinding
import com.pronted.databinding.FragmentTimetablePagerViewBinding
import com.pronted.dto.login.ClassesAction
import com.pronted.dto.login.UserAppList
import com.pronted.dto.student.ClassDropDownModel
import com.pronted.dto.student.SectionItem
import com.pronted.dto.timeline.TimetableAction
import com.pronted.presentation.listener.ClassSectionClickListener
import com.pronted.presentation.profile.StudentViewModel
import com.pronted.presentation.timeline.AppDataViewModel
import com.pronted.presentation.timeline.TimeLineViewModel
import com.pronted.presentation.userapps.ClassDropDownAdapter
import com.pronted.utils.DateUtil
import com.pronted.utils.extentions.ACCESS_TOKEN
import com.pronted.utils.extentions.COLLEGE
import com.pronted.utils.extentions.DYNAMIC_HEADERS
import com.pronted.utils.extentions.USER_SELECTED_ROLE
import com.pronted.utils.toDateString
import io.paperdb.Paper
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class TimetablePagerFragment : BaseFragment<FragmentTimetablePagerViewBinding>() {
    // Store instance variables
    private var tabPosition = 0
    private lateinit var selectedDate: String
    private val currentDate by lazy {
        Calendar.getInstance().time.toDateString(DateUtil.y4M2d2)
    }
    private var boardType: String? = ""
    private var classesList = listOf<ClassDropDownModel>()
    private lateinit var classDialog: Dialog
    var selectedClass = arrayListOf<String>()

    lateinit var classDropDownAdapter: ClassDropDownAdapter
    lateinit var timeTableViewAdapter: TimeTableViewAdapter
    private var classRoomId: Int = 0

    private val studentViewModel: StudentViewModel by viewModel()

    private val timeLineViewModel: TimeLineViewModel by viewModel()

    // Using the activityViewModels() Kotlin property delegate from the
    // fragment-ktx artifact to retrieve the ViewModel in the activity scope
    private val appDataViewModel: AppDataViewModel by activityViewModels()

    //private lateinit var birthdayAdapter: BirthdaysPagerAdapter
    // Using the activityViewModels() Kotlin property delegate from the
    // fragment-ktx artifact to retrieve the ViewModel in the activity scope

    companion object {
        fun newInstance(page: Int, selectedDate: String): Fragment {
            val fragment = TimetablePagerFragment()
            val bundle = Bundle()
            bundle.putInt("var_arg", page)
            bundle.putString("selected_date", selectedDate)
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_timetable_pager_view
        ) as FragmentTimetablePagerViewBinding
        return binding.root
    }

    override fun init() {
        tabPosition = arguments?.getInt("var_arg") ?: 0
        selectedDate = arguments?.getString("selected_date") ?: currentDate
        Trace.i("$tabPosition")
        binding.run {
            hasTimetable = false
            rvTimetable.setHasFixedSize(true)
            rvTimetable.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            context?.let {
                timeTableViewAdapter = TimeTableViewAdapter(it, tabPosition == 0)
                rvTimetable.adapter = timeTableViewAdapter
            }
            if (tabPosition == 1) {
                filterClassSection.visibility = View.VISIBLE
                filterClassSection.setOnClickListener {
                    if (boardType == COLLEGE) {
                        //todo handle college class dialog
                    } else {
                        if (classesList.isNotEmpty())
                            openClassAndSectionDialog()
                        else
                            fetchAvailableClasses(true)
                    }
                }
            } else {
                filterClassSection.visibility = View.GONE
            }

        }

        refreshTimetable(selectedDate)

    }

    fun refreshTimetable(selectedDate: String) {
        Trace.i("Selected date:$selectedDate")
        this.selectedDate = selectedDate
        if (tabPosition == 0) {
            Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.let {
                fetchTimeTable(it.userUniqueId, true)
            }
        } else {
            if (classRoomId == 0)
                toast(getString(R.string.please_select_class))
            else
                fetchTimeTable(classRoomId)
        }
    }

    private fun openClassAndSectionDialog() {
        context?.let {
            val dialogLogoutBinding = DialogClassAndSectionBinding.inflate(layoutInflater)
            classDialog = Dialog(it).apply {
                setContentView(dialogLogoutBinding.root)
                window?.setLayout(
                    Constraints.LayoutParams.MATCH_PARENT,
                    Constraints.LayoutParams.WRAP_CONTENT
                )
            }
            classDialog.show()
            dialogLogoutBinding.run {
                imgCancel.setOnClickListener {
                    classDialog.dismiss()
                }
                rvClassSection.setHasFixedSize(true)
                rvClassSection.layoutManager = LinearLayoutManager(
                    it,
                    LinearLayoutManager.VERTICAL, false
                )
                Trace.i("Selected Item:" + selectedClass)
                classDropDownAdapter = ClassDropDownAdapter(it, selectedClass, classSectionListener)
                rvClassSection.adapter = classDropDownAdapter
                classDropDownAdapter.list = classesList
                hasClasses = classesList.isNotEmpty()
            }
        }

    }

    /**
     * Fetch student profile
     */
    private fun fetchAvailableClasses(showDialog: Boolean = false) {
        lifecycleScope.launchWhenResumed {
            showLoader()
            Preference.instance.putBoolean(DYNAMIC_HEADERS, false)
            studentViewModel.fetchAvailableClasses()
                .collectLatest { classAction ->
                    dismissLoader()
                    when (classAction) {
                        is ClassesAction.Success -> {
                            Trace.e("Classes list: " + classAction.classes)
                            classesList = classAction.classes
                            if (showDialog)
                                openClassAndSectionDialog()
                        }
                        is ClassesAction.Fail -> {
                            Trace.i(
                                classAction.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                            classesList = arrayListOf()
                            if (showDialog)
                                openClassAndSectionDialog()
                        }
                    }
                }
        }
    }

    private val classSectionListener =
        object : ClassSectionClickListener<ClassDropDownModel, SectionItem> {
            override fun onSectionClick(item: ClassDropDownModel, item1: SectionItem) {
                classRoomId = item1.classroomId
                binding.filterClassSection.text = String.format(
                    getString(R.string.class_section_format),
                    item.courseName,
                    item1.classroomName
                )
                classDialog.dismiss()
                selectedClass.clear()
                selectedClass.add(item.courseName)
                selectedClass.add(item1.classroomName)
                fetchTimeTable(classRoomId)
            }
        }

    /**
     * Fetch time table of Employee
     */
    private fun fetchTimeTable(requestId: Int, isEmployee: Boolean = false) {
        lifecycleScope.launchWhenResumed {
            showLoader()
            val headers = mapOf(
                Pair("Authorization", Preference.instance.getString(ACCESS_TOKEN) ?: ""),
                Pair("APP_NAME", Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.appName ?: ""),
                Pair(
                    "SCHOOL_CODE",
                    Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.schoolCode ?: ""
                )
            )
            Preference.instance.putBoolean(DYNAMIC_HEADERS, true)
            timeLineViewModel.fetchTimetable(requestId, selectedDate, isEmployee, headers)
                .collectLatest { timetableAction ->
                    dismissLoader()
                    when (timetableAction) {
                        is TimetableAction.Success -> {
                            Trace.e("Class Timetable : " + timetableAction.response)
                            if(timetableAction.response.size > 0){
                                timetableAction.response[0].periodWiseTimetableList?.let {
                                    timeTableViewAdapter.list = it
                                    binding.hasTimetable = it.isNotEmpty()
                                }
                            }
                        }
                        is TimetableAction.Fail -> {
                            Trace.i(
                                timetableAction.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                            binding.hasTimetable = false
                        }
                    }
                }
        }
    }
}