package com.pronted.presentation.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.*
import com.pronted.R
import com.pronted.databinding.FragmentParentTimelineBinding
import com.pronted.databinding.FragmentParentTimetableBinding
import com.pronted.dto.login.UserAppList
import com.pronted.dto.timeline.TimetableAction
import com.pronted.presentation.home.SmartParentActivity
import com.pronted.presentation.timeline.TimeLineViewModel
import com.pronted.utils.DateUtil
import com.pronted.utils.extentions.ACCESS_TOKEN
import com.pronted.utils.extentions.DYNAMIC_HEADERS
import com.pronted.utils.extentions.SPECTRUM_ROLE
import com.pronted.utils.extentions.USER_SELECTED_ROLE
import com.pronted.utils.toDateString
import com.shrikanthravi.collapsiblecalendarview.data.Day
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import io.paperdb.Paper
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ParentTimetableFragment : BaseFragment<FragmentParentTimetableBinding>() {
    private lateinit var selectedDate: String
    lateinit var timeTableViewAdapter: TimeTableViewAdapter
    private val timeLineViewModel: TimeLineViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_parent_timetable
        ) as FragmentParentTimetableBinding
        return binding.root
    }

    override fun init() {
        binding.run {
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
            hasTimetable = false
            rvTimetable.setHasFixedSize(true)
            rvTimetable.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            context?.let {
                timeTableViewAdapter = TimeTableViewAdapter(it, false)
                rvTimetable.adapter = timeTableViewAdapter
            }
        }
        refreshTimetable()
    }

    override fun resume() {
        (activity as BaseActivity<*>).title(getString(R.string.time_table))
    }

    private val collapsibleCalendarListener = object : CollapsibleCalendar.CalendarListener {
        override fun onDayChanged() {}
        override fun onClickListener() {}
        override fun onDaySelect() {
            binding.calendarView.selectedDay?.let {
                selectedDate = "${it.year}-${(it.month + 1).toString().padStart(2, '0')}-${
                    (it.day)
                        .toString()
                        .padStart(2, '0')
                }"
                Trace.i("Selected date:$selectedDate")
                refreshTimetable()
            }
        }

        override fun onItemClick(v: View) {}
        override fun onDataUpdate() {}
        override fun onMonthChange() {}
        override fun onWeekChange(position: Int) {}
    }

    private fun refreshTimetable() {
        Trace.i("Selected date:$selectedDate")
        Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.let {
            fetchTimeTable(it.userUniqueId)
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
                Pair("APP_NAME", SPECTRUM_ROLE),
                Pair(
                    "SCHOOL_CODE",
                    Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.schoolCode ?: ""
                ),
                Pair(
                    "STUDENT_PROFILE_ID",
                    Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.userUniqueId.toString()
                )
            )
            Preference.instance.putBoolean(DYNAMIC_HEADERS, true)
            timeLineViewModel.fetchTimetable(requestId, selectedDate, isEmployee, headers)
                .collectLatest { timetableAction ->
                    dismissLoader()
                    when (timetableAction) {
                        is TimetableAction.Success -> {
                            Trace.e("Class Timetable : " + timetableAction.response)
                            if (timetableAction.response.size > 0) {
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