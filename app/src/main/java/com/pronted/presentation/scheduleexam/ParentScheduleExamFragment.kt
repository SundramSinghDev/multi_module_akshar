package com.pronted.presentation.scheduleexam

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Constraints
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.BaseActivity
import com.base.BaseFragment
import com.base.inflateFragment
import com.pronted.R
import com.pronted.databinding.DialogClassAndSectionBinding
import com.pronted.databinding.FragmentParentScheduleExamBinding
import com.pronted.dto.exam.ExamDropDownAction
import com.pronted.dto.exam.ExamDropDownModel
import com.pronted.dto.exam.ExamScheduleAction
import com.pronted.dto.exam.ExamScheduleModel
import com.pronted.presentation.listener.ItemClickListener
import com.pronted.utils.ApiStatusCode
import com.pronted.utils.extentions.gone
import com.pronted.utils.extentions.visible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import org.koin.androidx.viewmodel.ext.android.viewModel

class ParentScheduleExamFragment : BaseFragment<FragmentParentScheduleExamBinding>() {

    private lateinit var examDialog: Dialog
    private val parentExamScheduleViewModel: ParentExamScheduleViewModel by viewModel()
    private var examList = listOf<ExamDropDownModel>()
    private var examDropDownAdapter: ExamDropDownAdapter? = null
    private var examScheduleAdapter: ExamScheduleAdapter? = null
    private var selectedExam: ExamScheduleModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_parent_schedule_exam
        ) as FragmentParentScheduleExamBinding
        return binding.root
    }

    override fun init() {
        binding.run {
            context?.let {
                fetchExamList()
                filterExamSection.setOnClickListener {
                    openExamDialog()
                }
            }
        }
    }

    override fun resume() {
        (activity as BaseActivity<*>).title(getString(R.string.schedule_exam))
    }

    private fun fetchExamList() {
        lifecycleScope.launchWhenResumed {
            showLoader()
            parentExamScheduleViewModel.fetchExamDropDownForParent().flowOn(Dispatchers.IO)
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
                filterExamSection.text = item.examName
                examDialog.dismiss()
                fetchExamSchedule(item.examId)
            }
        }
    }

    private fun clearExamUI() {
        binding.run {
            filterExamSection.text = getString(R.string.select_exam)
            rvExam.gone()
            clEmptyView.visible()
        }
    }

    private fun fetchExamSchedule(examId: Int) {
        lifecycleScope.launchWhenResumed {
            showLoader()
            parentExamScheduleViewModel.fetchExamScheduleByExamId(examId).flowOn(Dispatchers.IO)
                .collectLatest {
                    dismissLoader()
                    when (it) {
                        is ExamScheduleAction.Success -> {
                            val examScheduleModel = it.examSchedule
                            selectedExam = examScheduleModel
                            updateExamScheduleUI(examScheduleModel)
                        }
                        is ExamScheduleAction.Fail -> {
                            if (it.errorResponse.status == ApiStatusCode.Status_406) {
                                binding.run {
                                    rvExam.gone()
                                    clEmptyView.visible()
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
                rvExam.setHasFixedSize(true)
                examScheduleAdapter = ExamScheduleAdapter(isParent = true)
                rvExam.adapter = examScheduleAdapter
                examScheduleAdapter?.list = examSchedule.scheduleModel as ArrayList
            } else {
                rvExam.gone()
                clEmptyView.visible()
            }
        }
    }
}