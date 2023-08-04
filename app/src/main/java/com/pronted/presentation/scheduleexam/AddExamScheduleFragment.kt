package com.pronted.presentation.scheduleexam

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.base.BaseActivity
import com.base.BaseFragment
import com.base.inflateFragment
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.pronted.R
import com.pronted.databinding.FragmentAddExamScheduleBinding
import com.pronted.dto.exam.*
import com.pronted.presentation.home.ChildActivity
import com.pronted.presentation.timeline.AppDataViewModel
import com.pronted.utils.DateUtil
import com.pronted.utils.convertToDate
import com.pronted.utils.extentions.*
import com.pronted.utils.formatHourOrMinute
import com.pronted.utils.toDateString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.HashMap

class AddExamScheduleFragment : BaseFragment<FragmentAddExamScheduleBinding>() {
    private var classRoomId: Int = 0
    private var testId: Int = 0
    private var startDateListener: DatePickerDialog.OnDateSetListener? = null
    private var calendar: Calendar? = null
    private val addExamScheduleViewModel: AddExamScheduleViewModel by viewModel()
    private var examDropDown: ExamDropDownModel? = null
    private val appDataViewModel: AppDataViewModel by activityViewModels()
    private var isEditMode: Boolean = false
    private var scheduleModel: ScheduleModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_add_exam_schedule
        ) as FragmentAddExamScheduleBinding
        return binding.root
    }

    override fun init() {
        super.init()

        binding.run {
            arguments?.run {
                tvClass.text = getString(NAV_OBJECT)
                classRoomId = getInt(NAV_OBJECT2)
                examDropDown = getSerializable(NAV_OBJECT3) as? ExamDropDownModel
                testId = getInt(NAV_OBJECT4)
                isEditMode = getBoolean(NAV_OBJECT5)
                scheduleModel = getSerializable(NAV_OBJECT6) as? ScheduleModel
            }
            calendar = Calendar.getInstance()
            tvExam.text = examDropDown?.examName
            (activity as BaseActivity<*>).title(
                if (isEditMode) getString(R.string.update_exam_schedule) else getString(
                    R.string.create_exam_schedule
                )
            )
            if (isEditMode) {
                scheduleModel?.run {
                    tvStartDate.text =
                        date.convertToDate(DateUtil.y4M2d2)?.toDateString(DateUtil.m3D2Y4)
                    tvStartTime.text =
                        startTime.convertToDate(DateUtil.H2m2)?.toDateString(DateUtil.H2m2)
                    edtDuration.setText(duration.toString())
                    btnScheduleExam.text = getString(R.string.update_schedule)
                }
            }
            setListeners()
        }
    }

    private fun setListeners() {
        binding.run {
            startDateListener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    calendar?.set(Calendar.YEAR, year)
                    calendar?.set(Calendar.MONTH, monthOfYear)
                    calendar?.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    tvStartDate.text = calendar?.time?.toDateString(DateUtil.m3D2Y4)
                    validateInputs()
                }

            tvStartDate.setOnClickListener {
                calendar?.let {
                    DatePickerDialog(
                        requireContext(), startDateListener,
                        it.get(Calendar.YEAR), it.get(Calendar.MONTH),
                        it.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            }

            tvStartTime.setOnClickListener {
                val materialTimePicker = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                val timePicker = materialTimePicker.build()
                timePicker.addOnPositiveButtonClickListener {
                    val selectedTime: String =
                        timePicker.hour.formatHourOrMinute() + ":" + timePicker.minute.formatHourOrMinute()
                    if (timePicker.hour in 7..18) {
                        tvStartTime.text = selectedTime
                    } else if (timePicker.hour == 19 && timePicker.minute == 0) {
                        tvStartTime.text = selectedTime
                    } else {
                        toast("Exam start time should be in between 7am to 7pm. ")
                    }
                    validateInputs()
                }
                timePicker.show(parentFragmentManager, "TimePicker")
            }

            edtDuration.onTextChanged {
                validateInputs()
            }

            btnScheduleExam.setOnClickListener {
                if (isEditMode) {
                    updateExamSchedule()
                } else {
                    createExamSchedule()
                }
            }
        }
    }

    private fun updateExamSchedule() {
        binding.run {
            lifecycleScope.launch {
                showLoader()
                scheduleModel?.apply {
                    date = calendar?.time?.toInstant().toString()
                    startTime = tvStartTime.text.toString().convertToDate(DateUtil.H2m2)
                        ?.toDateString(DateUtil.h2m2s2) ?: ""
                    duration = edtDuration.text.toString().toInt()
                }?.let {
                    addExamScheduleViewModel.updateExamSchedule(it).flowOn(Dispatchers.IO)
                        .collectLatest {
                            dismissLoader()
                            when (it) {
                                is UpdateExamScheduleAction.Success -> {
                                    (context as ChildActivity).onBack()
                                }
                                is UpdateExamScheduleAction.Fail -> {
                                    it.errorResponse.message?.let { it1 -> toast(it1) }
                                }
                            }
                        }
                }
            }
        }
    }

    private fun createExamSchedule() {
        binding.run {
            lifecycleScope.launch {
                showLoader()
                val examScheduleRequest = ExamScheduleRequest(
                    examId = examDropDown?.examId,
                    testId = testId,
                    classroomId = classRoomId,
                    startDate = calendar?.time?.toInstant().toString(),
                    startTime = tvStartTime.text.toString().convertToDate(DateUtil.H2m2)
                        ?.toDateString(DateUtil.h2m2s2),
                    duration = edtDuration.text.toString().toInt()
                )
                addExamScheduleViewModel.createExamSchedule(examScheduleRequest)
                    .flowOn(Dispatchers.IO)
                    .collectLatest {
                        dismissLoader()
                        when (it) {
                            is AddExamScheduleAction.Success -> {
                                (context as ChildActivity).onBack()
                            }
                            is AddExamScheduleAction.Fail -> {
                                it.errorResponse.message?.let { it1 -> toast(it1) }
                            }
                        }
                    }
            }
        }
    }

    private fun validateInputs() {
        binding.run {
            btnScheduleExam.isEnabled =
                tvStartDate.text.isNotBlank() && tvStartTime.text.isNotBlank() && edtDuration.text?.isNotBlank() == true
        }
    }
}