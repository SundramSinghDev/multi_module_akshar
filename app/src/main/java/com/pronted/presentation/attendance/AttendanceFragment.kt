package com.pronted.presentation.attendance

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Constraints
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.*
import com.pronted.R
import com.pronted.databinding.DialogClassAndSectionBinding
import com.pronted.databinding.FragmentAttendanceBinding
import com.pronted.dto.attendance.*
import com.pronted.dto.login.ClassesAction
import com.pronted.dto.student.ClassDropDownModel
import com.pronted.dto.student.SectionItem
import com.pronted.presentation.listener.ClassSectionClickListener
import com.pronted.presentation.listener.ItemClickListener
import com.pronted.presentation.userapps.ClassDropDownAdapter
import com.pronted.utils.DateUtil
import com.pronted.utils.extentions.*
import com.pronted.utils.toDateString
import com.shrikanthravi.collapsiblecalendarview.data.Day
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class AttendanceFragment : BaseFragment<FragmentAttendanceBinding>() {
    private val attendanceViewModel: AttendanceViewModel by viewModel()
    private lateinit var selectedDate: String
    private lateinit var classDropDownAdapter: ClassDropDownAdapter
    private lateinit var classDialog: Dialog
    private lateinit var shiftDialog: Dialog
    private lateinit var marksAsAllDialog: Dialog
    private var classesList = listOf<ClassDropDownModel>()
    private var selectedClass = arrayListOf<String>()
    private var classRoomId: Int = 0
    private var shifts = listOf<ShiftResponseModel>()
    private var shiftDropDownAdapter: ShiftDropDownAdapter? = null
    private var selectedShift: ShiftResponseModel? = null
    private var studentsAttendanceAdapter: StudentsAttendanceAdapter? = null
    private var markAsAllDropDownAdapter: MarkAsAllDropDownAdapter? = null
    private var markAsAllList = listOf(
        Pair(KEY_PRESENT, PRESENT),
        Pair(KEY_ABSENT, ABSENT),
        Pair(KEY_LEAVE, LEAVE)
    )
    private var attendanceModelList: List<AttendanceModel> = emptyList()
    private val STUDENT = "Student"
    private val EMPLOYEE = "Employee"
    private var selectedProfileType: String? = null
    private var profileTypeDialog: Dialog? = null
    private var profileTypeDropDownAdapter: ProfileTypeDropDownAdapter? = null
    private val profileTypeList = listOf(EMPLOYEE, STUDENT)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_attendance
        ) as FragmentAttendanceBinding
        return binding.root
    }

    override fun init() {
        binding.run {
            studentsAttendanceAdapter = StudentsAttendanceAdapter()
            recyclerView.adapter = studentsAttendanceAdapter
            selectedDate = Calendar.getInstance().time.toDateString(DateUtil.y4M2d2)
            calendarView.select(
                Day(
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(
                        Calendar.DATE
                    )
                )
            )
            calendarView.setCalendarListener(collapsibleCalendarListener)

            filterProfileSection.setOnClickListener {
                openProfileTypeDialog()
            }
            filterClassSection.setOnClickListener {
                if (classesList.isNotEmpty()) {
                    openClassAndSectionDialog()
                } else {
                    fetchAvailableClasses(true)
                }
            }
            filterShiftSection.setOnClickListener {
                openShiftDialog()
            }
            filterMarkAllSection.setOnClickListener {
                openMarkAsAllDialog()
            }
            btnSaveAttendance.setOnClickListener {
                if (selectedProfileType == EMPLOYEE) {
                    saveEmployeesAttendance()
                } else {
                    saveStudentsAttendance()
                }
            }
        }
    }

    override fun resume() {
        (activity as BaseActivity<*>).title(getString(R.string.attendance))
    }

    private val collapsibleCalendarListener = object : CollapsibleCalendar.CalendarListener {
        override fun onDayChanged() {}
        override fun onClickListener() {}
        override fun onDaySelect() {
            binding.calendarView.selectedDay?.let {
                selectedDate = "${it.year}-${(it.month + 1).toString().padStart(2, '0')}-${
                    (it.day).toString().padStart(2, '0')
                }"
                if (selectedProfileType == EMPLOYEE) {
                    fetchEmployeesAttendance(selectedShift?.shiftId ?: 0, selectedDate)
                } else {
                    fetchStudentsAttendance(classRoomId, selectedShift?.shiftId ?: 0, selectedDate)
                }
            }
        }

        override fun onItemClick(v: View) {}
        override fun onDataUpdate() {}
        override fun onMonthChange() {}
        override fun onWeekChange(position: Int) {}
    }

    private fun fetchAvailableClasses(showDialog: Boolean = false) {
        lifecycleScope.launchWhenResumed {
            showLoader()
            Preference.instance.putBoolean(DYNAMIC_HEADERS, false)
            attendanceViewModel.fetchAvailableClasses()
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
                clearUI()
                fetchShifts(item1.classroomId)
            }
        }

    private fun fetchShifts(classRoomId: Int) {
        lifecycleScope.launch {
            showLoader()
            selectedProfileType?.let {
                attendanceViewModel.fetchShifts(it, classRoomId).flowOn(Dispatchers.IO)
                    .collectLatest {
                        dismissLoader()
                        shifts = when (it) {
                            is ShiftAction.Success -> {
                                it.response
                            }
                            is ShiftAction.Fail -> {
                                emptyList()
                            }
                        }
                        validate()
                    }
            }
        }
    }

    private fun openShiftDialog() {
        context?.let {
            val dialogLogoutBinding = DialogClassAndSectionBinding.inflate(layoutInflater)
            shiftDialog = Dialog(it).apply {
                setContentView(dialogLogoutBinding.root)
                window?.setLayout(
                    Constraints.LayoutParams.MATCH_PARENT,
                    Constraints.LayoutParams.WRAP_CONTENT
                )
            }
            shiftDialog.show()

            dialogLogoutBinding.run {
                title.text = getString(R.string.select_shift)
                imgCancel.setOnClickListener {
                    shiftDialog.dismiss()
                }
                rvClassSection.setHasFixedSize(true)
                rvClassSection.layoutManager = LinearLayoutManager(
                    it,
                    LinearLayoutManager.VERTICAL, false
                )
                shiftDropDownAdapter = ShiftDropDownAdapter(shiftDropDownSelectionListener)
                rvClassSection.adapter = shiftDropDownAdapter
                shiftDropDownAdapter?.list = shifts
                hasClasses = shifts.isNotEmpty()
            }
        }
    }

    private val shiftDropDownSelectionListener = object : ItemClickListener<ShiftResponseModel> {
        override fun onClicked(item: ShiftResponseModel, position: Int) {
            binding.run {
                selectedShift = item
                filterShiftSection.text = item.name
                shiftDialog.dismiss()
                if (selectedProfileType == EMPLOYEE) {
                    fetchEmployeesAttendance(selectedShift?.shiftId ?: 0, selectedDate)
                } else {
                    fetchStudentsAttendance(classRoomId, selectedShift?.shiftId ?: 0, selectedDate)
                }
            }
        }
    }

    private fun fetchStudentsAttendance(classRoomId: Int, shiftId: Int, selectedDate: String) {
        lifecycleScope.launch {
            showLoader()
            attendanceViewModel.fetchStudentsAttendance(classRoomId, shiftId, selectedDate)
                .flowOn(Dispatchers.IO).collectLatest {
                    dismissLoader()
                    when (it) {
                        is StudentsAttendanceAction.Success -> {
                            updateAttendance(it.response)
                        }
                        is StudentsAttendanceAction.Fail -> {
                            it.errorResponse.message?.let { it1 -> toast(it1) }
                        }
                    }
                    validate()
                }
        }
    }

    private fun updateAttendance(attendanceList: ArrayList<AttendanceModel>) {
        attendanceModelList = attendanceList
        studentsAttendanceAdapter?.list = attendanceModelList
        binding.filterMarkAllSection.visible()
        binding.recyclerView.visible()
    }

    private fun openMarkAsAllDialog() {
        context?.let {
            val dialogLogoutBinding = DialogClassAndSectionBinding.inflate(layoutInflater)
            marksAsAllDialog = Dialog(it).apply {
                setContentView(dialogLogoutBinding.root)
                window?.setLayout(
                    Constraints.LayoutParams.MATCH_PARENT,
                    Constraints.LayoutParams.WRAP_CONTENT
                )
            }
            marksAsAllDialog.show()

            dialogLogoutBinding.run {
                title.text = getString(R.string.mark_as_all)
                imgCancel.setOnClickListener {
                    marksAsAllDialog.dismiss()
                }
                rvClassSection.setHasFixedSize(true)
                rvClassSection.layoutManager = LinearLayoutManager(
                    it,
                    LinearLayoutManager.VERTICAL, false
                )
                markAsAllDropDownAdapter =
                    MarkAsAllDropDownAdapter(markAsAllDropDownSelectionListener)
                rvClassSection.adapter = markAsAllDropDownAdapter
                markAsAllDropDownAdapter?.list = markAsAllList
                hasClasses = markAsAllList.isNotEmpty()
            }
        }
    }

    private val markAsAllDropDownSelectionListener = object : ItemClickListener<String> {
        override fun onClicked(item: String, position: Int) {
            binding.run {
                studentsAttendanceAdapter?.run {
                    list.forEach { it.attendanceInd = item }
                    notifyDataSetChanged()
                }
                marksAsAllDialog.dismiss()
            }
        }
    }

    private fun clearUI() {
        binding.run {
            if (selectedProfileType == EMPLOYEE) {
                classRoomId = 0
                classesList = emptyList()
                selectedClass.clear()
                filterClassSection.text = getString(R.string.select_class)
            }
            selectedShift = null
            attendanceModelList = emptyList()
            filterShiftSection.text = getString(R.string.select_shift)
            filterMarkAllSection.gone()
            recyclerView.gone()
        }
    }

    private fun validate() {
        binding.run {
            val isValid = if (selectedProfileType == STUDENT) {
                selectedProfileType != null && classRoomId != 0 && selectedShift != null && selectedDate.isNotBlank() && attendanceModelList.isNotEmpty()
            } else {
                selectedProfileType != null && selectedShift != null && selectedDate.isNotBlank() && attendanceModelList.isNotEmpty()
            }
            btnSaveAttendance.isEnabled = isValid
        }
    }

    private fun openProfileTypeDialog() {
        context?.let {
            val dialogLogoutBinding = DialogClassAndSectionBinding.inflate(layoutInflater)
            profileTypeDialog = Dialog(it).apply {
                setContentView(dialogLogoutBinding.root)
                window?.setLayout(
                    Constraints.LayoutParams.MATCH_PARENT,
                    Constraints.LayoutParams.WRAP_CONTENT
                )
            }
            profileTypeDialog?.show()

            dialogLogoutBinding.run {
                title.text = getString(R.string.select_profile_type)
                imgCancel.setOnClickListener {
                    profileTypeDialog?.dismiss()
                }
                rvClassSection.setHasFixedSize(true)
                rvClassSection.layoutManager = LinearLayoutManager(
                    it,
                    LinearLayoutManager.VERTICAL, false
                )
                profileTypeDropDownAdapter =
                    ProfileTypeDropDownAdapter(profileTypeDropDownSelectionListener)
                rvClassSection.adapter = profileTypeDropDownAdapter
                profileTypeDropDownAdapter?.list = profileTypeList
                hasClasses = profileTypeList.isNotEmpty()
            }
        }
    }

    private val profileTypeDropDownSelectionListener =
        object : ItemClickListener<String> {
            override fun onClicked(item: String, position: Int) {
                binding.run {
                    if (selectedProfileType != item) {
                        selectedProfileType = item
                        filterProfileSection.text = item
                        profileTypeDialog?.dismiss()
                        clearUI()
                        validate()
                        if (selectedProfileType == STUDENT) {
                            filterClassSection.visible()
                            fetchAvailableClasses(false)
                        } else {
                            filterClassSection.gone()
                            fetchShifts(0)
                        }
                    }
                }
            }
        }

    private fun fetchEmployeesAttendance(shiftId: Int, selectedDate: String) {
        lifecycleScope.launch {
            showLoader()
            attendanceViewModel.fetchEmployeesAttendance(shiftId, selectedDate)
                .flowOn(Dispatchers.IO).collectLatest {
                    dismissLoader()
                    when (it) {
                        is EmployeesAttendanceAction.Success -> {
                            updateAttendance(it.response)
                        }
                        is EmployeesAttendanceAction.Fail -> {
                            it.errorResponse.message?.let { it1 -> toast(it1) }
                        }
                    }
                    validate()
                }
        }
    }

    private fun saveStudentsAttendance() {
        lifecycleScope.launch {
            showLoader()
            attendanceViewModel.saveStudentsAttendance(
                STUDENT,
                classRoomId,
                attendanceModelList
            ).flowOn(Dispatchers.IO).collectLatest {
                dismissLoader()
                when (it) {
                    is SaveStudentsAttendanceAction.Success -> {
                        updateAttendance(it.response)
                        toast(getString(R.string.attendance_saved_successfully))
                    }
                    is SaveStudentsAttendanceAction.Fail -> {
                        it.errorResponse.message?.let { it1 -> toast(it1) }
                    }
                }
            }
        }
    }

    private fun saveEmployeesAttendance() {
        lifecycleScope.launch {
            showLoader()
            attendanceViewModel.saveEmployeesAttendance(
                EMPLOYEE,
                attendanceModelList
            ).flowOn(Dispatchers.IO).collectLatest {
                dismissLoader()
                when (it) {
                    is SaveEmployeesAttendanceAction.Success -> {
                        updateAttendance(it.response)
                        toast(getString(R.string.attendance_saved_successfully))
                    }
                    is SaveEmployeesAttendanceAction.Fail -> {
                        it.errorResponse.message?.let { it1 -> toast(it1) }
                    }
                }
            }
        }
    }
}