package com.pronted.presentation.scheduleexam

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Constraints
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.*
import com.pronted.R
import com.pronted.databinding.DialogClassAndSectionBinding
import com.pronted.databinding.FragmentScheduleExamBinding
import com.pronted.dto.exam.*
import com.pronted.dto.login.ClassesAction
import com.pronted.dto.student.ClassDropDownModel
import com.pronted.dto.student.SectionItem
import com.pronted.presentation.home.ChildActivity
import com.pronted.presentation.listener.ClassSectionClickListener
import com.pronted.presentation.listener.ItemClickListener
import com.pronted.presentation.listener.ScheduleExamListener
import com.pronted.presentation.timeline.AppDataViewModel
import com.pronted.presentation.userapps.ClassDropDownAdapter
import com.pronted.utils.ApiStatusCode
import com.pronted.utils.extentions.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScheduleExamFragment : BaseFragment<FragmentScheduleExamBinding>(),
    ScheduleExamListener<ScheduleModel> {
    private lateinit var classDropDownAdapter: ClassDropDownAdapter
    private lateinit var classDialog: Dialog
    private lateinit var examDialog: Dialog
    private var classesList = listOf<ClassDropDownModel>()
    private val examScheduleViewModel: ExamScheduleViewModel by viewModel()
    private var selectedClass = arrayListOf<String>()
    private var classRoomId: Int = 0
    private var examList = listOf<ExamDropDownModel>()
    private var examDropDownAdapter: ExamDropDownAdapter? = null
    private var examScheduleAdapter: ExamScheduleAdapter? = null
    private var selectedExamSchedule: ExamScheduleModel? = null
    private var selectedExamDropDown: ExamDropDownModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_schedule_exam
        ) as FragmentScheduleExamBinding
        return binding.root
    }

    override fun init() {
        binding.run {
            context?.let {
                rvExam.setHasFixedSize(true)
                examScheduleAdapter =
                    ExamScheduleAdapter(scheduleExamListener = this@ScheduleExamFragment)
                rvExam.adapter = examScheduleAdapter
                fetchAvailableClasses(false)
                filterClassSection.setOnClickListener {
                    clearExamUI()
                    if (classesList.isNotEmpty()) {
                        openClassAndSectionDialog()
                    } else {
                        fetchAvailableClasses(true)
                    }
                }
                filterExamSection.setOnClickListener {
                    openExamDialog()
                }

                fabAddExam.setOnClickListener {
                    context?.let { context ->
                        startNewActivity(
                            context, ChildActivity::class.java, bundle = bundleOf(
                                Pair(NAV_DESTINATION, R.id.addExamSchedule),
                                Pair(NAV_OBJECT, binding.filterClassSection.text),
                                Pair(NAV_OBJECT2, classRoomId),
                                Pair(NAV_OBJECT3, selectedExamDropDown),
                                Pair(NAV_OBJECT4, selectedExamSchedule?.testId)
                            )
                        )
                    }
                }
            }
        }
    }

    override fun resume() {
        (activity as BaseActivity<*>).title(getString(R.string.schedule_exam))
        selectedExamDropDown?.let {
            fetchExamSchedule(it.examId)
        }
    }

    private fun fetchAvailableClasses(showDialog: Boolean = false) {
        lifecycleScope.launchWhenResumed {
            showLoader()
            Preference.instance.putBoolean(DYNAMIC_HEADERS, false)
            examScheduleViewModel.fetchAvailableClasses()
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

                fetchExamList(item1.classroomId)
            }
        }

    private fun fetchExamList(classRoomId: Int) {
        lifecycleScope.launchWhenResumed {
            showLoader()
            examScheduleViewModel.fetchExamDropDownByClassRoomId(classRoomId).flowOn(Dispatchers.IO)
                .collectLatest {
                    dismissLoader()
                    when (it) {
                        is ExamDropDownAction.Success -> {
                            examList = it.exams
                        }
                        is ExamDropDownAction.Fail -> {
                            examList = arrayListOf()
                        }
                    }
                }
        }
    }

    private fun openExamDialog() {
        context?.let {
            val dialogLogoutBinding = DialogClassAndSectionBinding.inflate(layoutInflater)
            examDialog = Dialog(it).apply {
                setContentView(dialogLogoutBinding.root)
                window?.setLayout(
                    Constraints.LayoutParams.MATCH_PARENT,
                    Constraints.LayoutParams.WRAP_CONTENT
                )
            }
            examDialog.show()

            dialogLogoutBinding.run {
                title.text = getString(R.string.choose_exam)
                imgCancel.setOnClickListener {
                    examDialog.dismiss()
                }
                rvClassSection.setHasFixedSize(true)
                rvClassSection.layoutManager = LinearLayoutManager(
                    it,
                    LinearLayoutManager.VERTICAL, false
                )
                examDropDownAdapter = ExamDropDownAdapter(examDropDownSelectionListener)
                rvClassSection.adapter = examDropDownAdapter
                examDropDownAdapter?.list = examList
                hasClasses = examList.isNotEmpty()
            }
        }
    }

    private val examDropDownSelectionListener = object : ItemClickListener<ExamDropDownModel> {
        override fun onClicked(item: ExamDropDownModel, position: Int) {
            binding.run {
                selectedExamDropDown = item
                filterExamSection.text = item.examName
                examDialog.dismiss()
                fetchExamSchedule(item.examId)
            }
        }
    }

    private fun clearExamUI() {
        binding.run {
            filterExamSection.text = getString(R.string.select_exam)
            selectedExamDropDown = null
            rvExam.gone()
            clEmptyView.visible()
            fabAddExam.gone()
        }
    }

    private fun fetchExamSchedule(examId: Int) {
        lifecycleScope.launchWhenResumed {
            showLoader()
            examScheduleViewModel.fetchExamScheduleByExamId(examId).flowOn(Dispatchers.IO)
                .collectLatest {
                    dismissLoader()
                    when (it) {
                        is ExamScheduleAction.Success -> {
                            val examScheduleModel = it.examSchedule
                            selectedExamSchedule = examScheduleModel
                            updateExamScheduleUI(examScheduleModel)
                        }
                        is ExamScheduleAction.Fail -> {
                            if (it.errorResponse.status == ApiStatusCode.Status_406) {
                                binding.run {
                                    rvExam.gone()
                                    clEmptyView.visible()
                                    fabAddExam.visible()
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun updateExamScheduleUI(examSchedule: ExamScheduleModel) {
        binding.run {
            if (examSchedule.scheduleModel?.isNotEmpty() == true) {
                rvExam.visible()
                clEmptyView.gone()
                fabAddExam.gone()
                examScheduleAdapter?.list = examSchedule.scheduleModel as ArrayList
            } else {
                rvExam.gone()
                clEmptyView.visible()
                fabAddExam.visible()
            }
        }
    }

    override fun onEdit(item: ScheduleModel, position: Int) {
        context?.let { context ->
            startNewActivity(
                context, ChildActivity::class.java, bundle = bundleOf(
                    Pair(NAV_DESTINATION, R.id.addExamSchedule),
                    Pair(NAV_OBJECT, binding.filterClassSection.text),
                    Pair(NAV_OBJECT2, classRoomId),
                    Pair(NAV_OBJECT3, selectedExamDropDown),
                    Pair(NAV_OBJECT4, selectedExamSchedule?.testId),
                    Pair(NAV_OBJECT5, true),
                    Pair(NAV_OBJECT6, item)
                )
            )
        }
    }

    override fun onDelete(item: ScheduleModel, position: Int) {
        lifecycleScope.launch {
            showLoader()
            examScheduleViewModel.deleteSchedule(item.id).flowOn(Dispatchers.IO).collectLatest {
                dismissLoader()
                when (it) {
                    is ExamScheduleDeleteAction.Success -> {
                        examScheduleAdapter?.removeItem(position)
                        if (examScheduleAdapter?.list.isNullOrEmpty()) {
                            binding.run {
                                rvExam.gone()
                                clEmptyView.visible()
                                fabAddExam.visible()
                            }
                        }
                    }
                    is ExamScheduleDeleteAction.Fail -> {
                        it.errorResponse.message?.let { it1 -> toast(it1) }
                    }
                }
            }
        }
    }
}