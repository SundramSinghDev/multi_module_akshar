package com.pronted.presentation.noticeboard

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.base.BaseActivity
import com.base.BaseFragment
import com.base.Trace
import com.base.inflateFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.pronted.R
import com.pronted.databinding.FragmentCreateNoticeBinding
import com.pronted.databinding.FragmentStudentMarksBinding
import com.pronted.dto.noticeboard.NoticeBoard
import com.pronted.dto.student.UpdateStudentProfileAction
import com.pronted.dto.timeline.EditNoticeAction
import com.pronted.presentation.home.ChildActivity
import com.pronted.presentation.profile.EditStudentModel
import com.pronted.presentation.profile.StudentViewModel
import com.pronted.utils.*
import com.pronted.utils.extentions.NAV_OBJECT
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class CreateNoticeFragment : BaseFragment<FragmentCreateNoticeBinding>() {
    private var noticeBoard: NoticeBoard? = null
    private val noticeModel: NoticeModel by lazy {
        NoticeModel()
    }
    private val noticeboardViewModel: NoticeboardViewModel by viewModel()
    private val materialDateBuilder by lazy {
        MaterialDatePicker.Builder.datePicker()
    }
    private var selectedDateView: Int = 0
    private var isCreation = true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_create_notice
        ) as FragmentCreateNoticeBinding
        return binding.root
    }

    override fun init() {
        binding.run {
            //tvTitle.text = getString(R.string.marks_report)
            noticeBoard = if (Build.VERSION.SDK_INT >= 33) {
                arguments?.getParcelable(NAV_OBJECT, NoticeBoard::class.java)
            } else {
                arguments?.getParcelable(NAV_OBJECT)
            }
            noticeBoard?.let {
                noticeModel.setData(it)
                when (it.visibility) {
                    "All" -> {
                        cbParents.isChecked = true
                        cbEmployees.isChecked = true
                    }
                    "Parent" -> {
                        cbParents.isChecked = true
                        cbEmployees.isChecked = false
                    }
                    "Employee" -> {
                        cbParents.isChecked = false
                        cbEmployees.isChecked = true
                    }
                }
            }
            notice = noticeModel
            saveNotice.setOnClickListener {
                context?.let {
                    if (noticeModel.isValidNotice(it) && isAudienceSelected()) {
                        val noticeId = if(!isCreation) noticeBoard?.id ?: 0 else 0
                        Trace.i("Valid notice" + noticeModel.getNotice(noticeId))
                        saveNoticeData(noticeModel.getNotice(noticeId))
                    }
                }
            }
            setDateTouchListener(startDate)
            setDateTouchListener(endDate)
        }
    }

    private fun saveNoticeData(notice: NoticeBoard) {
        lifecycleScope.launchWhenResumed {
            showLoader()
            noticeboardViewModel.updateNotice(notice, isCreation).collectLatest {
                dismissLoader()
                when (it) {
                    is EditNoticeAction.Success -> {
                        toast("Notice is updated Successfully")
                        (context as ChildActivity).onBack()
                    }
                    is EditNoticeAction.Fail -> {
                        toast(
                            it.errorResponse.message
                                ?: getString(R.string.something_went_wrong_try_again)
                        )
                    }
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setDateTouchListener(v: TextInputEditText) {
        v.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                selectedDateView = v.id
                showDatePicker()
            }
            false
        }
    }

    private fun showDatePicker() {
        var calendar = Calendar.getInstance()
        val materialDateBuilder = MaterialDatePicker.Builder.datePicker()
        materialDateBuilder.setTitleText("Select a Date")
        if (selectedDateView == R.id.start_date) {
            val dateValidator: CalendarConstraints.DateValidator =
                DateValidatorPointForward.now()
            materialDateBuilder.setCalendarConstraints(
                CalendarConstraints.Builder().setValidator(dateValidator).build()
            )
            noticeBoard?.startDate?.let {
                calendar = it.convertToDate(DateUtil.y4M2d2)?.toCalender() ?: Calendar.getInstance()
                calendar.add(Calendar.DATE, 1)
            }
            materialDateBuilder.setSelection(calendar.timeInMillis)
            val materialDatePicker = materialDateBuilder.build()
            materialDatePicker.addOnPositiveButtonClickListener {
                binding.startDate.setText(
                    DateUtil.y4M2d2.format(
                        it.toDate(
                            convert = true
                        )
                    )
                )
            }
            materialDatePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER")
        } else {
            calendar = Calendar.getInstance()
            binding.run {
                Trace.i("Date before convert:" + calendar.getDay() + "and: " + startDate.text)
                if (startDate.text.toString() != getString(R.string.start_date)) {
                    calendar =
                        startDate.text.toString().convertToDate(DateUtil.y4M2d2)?.toCalender()
                            ?: Calendar.getInstance()
                }
            }
            Trace.i("Date after convert:" + calendar.getDay())
            val dateValidator: CalendarConstraints.DateValidator =
                DateValidatorPointForward.from(calendar.timeInMillis)
            materialDateBuilder.setCalendarConstraints(
                CalendarConstraints.Builder().setValidator(dateValidator).build()
            )
            binding.run {
                if (endDate.text.toString() != getString(R.string.end_date)) {
                    calendar =
                        startDate.text.toString().convertToDate(DateUtil.y4M2d2)?.toCalender()
                            ?: Calendar.getInstance()
                    calendar.add(Calendar.DATE, 1)
                }
            }
            materialDateBuilder.setSelection(calendar.timeInMillis)
            val materialDatePicker = materialDateBuilder.build()
            materialDatePicker.addOnPositiveButtonClickListener {
                binding.endDate.setText(
                    DateUtil.y4M2d2.format(
                        it.toDate(
                            convert = true
                        )
                    )
                )
            }
            materialDatePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER")
        }
    }

    private fun isAudienceSelected(): Boolean {
        return if (!binding.cbParents.isChecked && !binding.cbEmployees.isChecked) {
            toast(getString(R.string.audience_check_error))
            false
        } else {
            if (binding.cbEmployees.isChecked && binding.cbParents.isChecked)
                noticeModel.setVisibility("All")
            else if (binding.cbParents.isChecked)
                noticeModel.setVisibility("Parent")
            else
                noticeModel.setVisibility("Employee")
            true
        }

    }

    override fun resume() {
        if (noticeBoard == null)
            (activity as BaseActivity<*>).title(getString(R.string.create_notice))
        else {
            isCreation = false
            (activity as BaseActivity<*>).title(getString(R.string.edit_notice))
        }
    }
}