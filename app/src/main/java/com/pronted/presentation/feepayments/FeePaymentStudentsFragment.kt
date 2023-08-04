package com.pronted.presentation.feepayments

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Constraints
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.*
import com.pronted.R
import com.pronted.databinding.DialogClassAndSectionBinding
import com.pronted.databinding.FragmentStudentsBinding
import com.pronted.dto.login.ClassesAction
import com.pronted.dto.student.ClassDropDownModel
import com.pronted.dto.student.SectionItem
import com.pronted.dto.student.StudentProfileResponse
import com.pronted.dto.student.StudentsAction
import com.pronted.presentation.home.ChildActivity
import com.pronted.presentation.listener.ClassSectionClickListener
import com.pronted.presentation.listener.ItemClickListener
import com.pronted.presentation.profile.StudentViewModel
import com.pronted.presentation.userapps.ClassDropDownAdapter
import com.pronted.presentation.userapps.StudentAdapter
import com.pronted.utils.extentions.DYNAMIC_HEADERS
import com.pronted.utils.extentions.NAV_DESTINATION
import com.pronted.utils.extentions.NAV_OBJECT
import com.pronted.utils.extentions.startNewActivity
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeePaymentStudentsFragment : BaseFragment<FragmentStudentsBinding>() {
    private lateinit var studentAdapter: StudentAdapter
    lateinit var classDropDownAdapter: ClassDropDownAdapter
    private val studentViewModel: StudentViewModel by viewModel()
    private lateinit var classDialog: Dialog
    var classesList = listOf<ClassDropDownModel>()
    var selectedClass = arrayListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_students
        ) as FragmentStudentsBinding
        return binding.root
    }

    override fun init() {
        binding.run {
            context?.let {
                filterClassSection.setOnClickListener {
                    if (classesList.isNotEmpty())
                        openClassAndSectionDialog()
                    else
                        fetchAvailableClasses(true)
                }
                rvStudents.setHasFixedSize(true)
                rvStudents.layoutManager = LinearLayoutManager(
                    it,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                studentAdapter =
                    StudentAdapter(
                        it,
                        this@FeePaymentStudentsFragment,
                        studentItemClickListener
                    )
                rvStudents.adapter = studentAdapter
                fetchAvailableClasses(false)
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

    override fun resume() {
        (activity as BaseActivity<*>).title(getString(R.string.students))
    }

    private val studentItemClickListener = object : ItemClickListener<StudentProfileResponse> {
        @SuppressLint("NotifyDataSetChanged")
        override fun onClicked(item: StudentProfileResponse, positoin:Int) {
            Trace.i("Selected student: $item")
            context?.let {
                startNewActivity(
                    it, ChildActivity::class.java, bundle = bundleOf(
                        Pair(NAV_DESTINATION, R.id.studentFeePayments),
                        Pair(NAV_OBJECT, item)
                    )
                )
            }
        }
    }

    private val classSectionListener =
        object : ClassSectionClickListener<ClassDropDownModel, SectionItem> {
            override fun onSectionClick(item: ClassDropDownModel, item1: SectionItem) {
                binding.filterClassSection.text = String.format(
                    getString(R.string.class_section_format),
                    item.courseName,
                    item1.classroomName
                )
                classDialog.dismiss()
                selectedClass.clear()
                selectedClass.add(item.courseName)
                selectedClass.add(item1.classroomName)

                fetchStudentList(item1.classroomId)
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

    /**
     * Fetch student list
     */
    fun fetchStudentList(classroomId: Int) {
        lifecycleScope.launchWhenResumed {
            showLoader()
            Preference.instance.putBoolean(DYNAMIC_HEADERS, false)
            studentViewModel.fetchStudentsByClassroomId(classroomId)
                .collectLatest { studentsAction ->
                    dismissLoader()
                    when (studentsAction) {
                        is StudentsAction.Success -> {
                            Trace.e("Students list: " + studentsAction.students)
                            studentAdapter.list = studentsAction.students
                            binding.hasStudents = studentsAction.students.isNotEmpty()
                        }
                        is StudentsAction.Fail -> {
                            Trace.i(
                                studentsAction.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                            binding.hasStudents = false
                        }
                    }
                }
        }
    }
}