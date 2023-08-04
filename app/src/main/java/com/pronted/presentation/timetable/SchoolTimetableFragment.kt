package com.pronted.presentation.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.base.BaseActivity
import com.base.BaseFragment
import com.base.Trace
import com.base.inflateFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.pronted.R
import com.pronted.databinding.FragmentParentTimelineBinding
import com.pronted.databinding.FragmentParentTimetableBinding
import com.pronted.databinding.FragmentSchoolTimelineBinding
import com.pronted.databinding.FragmentSchoolTimetableBinding
import com.pronted.presentation.home.ChildActivity
import com.pronted.presentation.home.SmartParentActivity
import com.pronted.presentation.timeline.birthday.seeall.AllBirthdaysViewPagerAdapter
import com.pronted.presentation.timeline.birthday.seeall.BirthdaysPagerFragment
import com.pronted.utils.DateUtil
import com.pronted.utils.extentions.EMPLOYEES
import com.pronted.utils.extentions.STUDENTS
import com.pronted.utils.toDateString
import com.shrikanthravi.collapsiblecalendarview.data.Day
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import java.util.*

class SchoolTimetableFragment : BaseFragment<FragmentSchoolTimetableBinding>() {
    private lateinit var timetableViewPagerAdapter: TimetableViewPagerAdapter
    private lateinit var selectedDate: String
    private var tabPosition = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_school_timetable
        ) as FragmentSchoolTimetableBinding
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

            timetableViewPagerAdapter = createTimetableVIewPagerAdapter()
            ttViewPager.adapter = timetableViewPagerAdapter

            TabLayoutMediator(
                tabLayout, ttViewPager
            ) { tab, position ->
                tab.text =
                    if (position == 0) getString(R.string.my_time_table) else getString(R.string.class_time_table)
            }.attach()
            ttViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    if (state == ViewPager2.SCROLL_STATE_IDLE) {
                        refreshTimeTable(selectedDate)
                    }
                }
            })
        }
    }

    private fun createTimetableVIewPagerAdapter(): TimetableViewPagerAdapter {
        return TimetableViewPagerAdapter(
            context as BaseActivity<*>,
            selectedDate
        )
    }

    private fun refreshTimeTable(selectedDate: String) {
        val fragment =
            timetableViewPagerAdapter.getRegisteredFragment(binding.ttViewPager.currentItem) as TimetablePagerFragment
        fragment.refreshTimetable(selectedDate)
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
                refreshTimeTable(selectedDate)
            }
        }

        override fun onItemClick(v: View) {}
        override fun onDataUpdate() {}
        override fun onMonthChange() {}
        override fun onWeekChange(position: Int) {}
    }

}