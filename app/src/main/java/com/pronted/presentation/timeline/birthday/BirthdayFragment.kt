package com.pronted.presentation.timeline.birthday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.BaseFragment
import com.base.Trace
import com.base.inflateFragment
import com.pronted.R
import com.pronted.databinding.FragmentBirthdayBinding
import com.pronted.dto.timeline.BirthdaysAction
import com.pronted.presentation.timeline.AppDataViewModel
import com.pronted.presentation.timeline.TimeLineViewModel
import com.pronted.utils.DateUtil
import com.pronted.utils.nextDay
import com.pronted.utils.nextWeekDay
import com.pronted.utils.toDateString
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class BirthdayFragment : BaseFragment<FragmentBirthdayBinding>() {
    // Store instance variables
    private var dayFilter: String = "Today"
    private var tabPosition = 0
    private val currentDate by lazy {
        Calendar.getInstance().time.toDateString(DateUtil.y4M2d2)
    }

    // Using the activityViewModels() Kotlin property delegate from the
    // fragment-ktx artifact to retrieve the ViewModel in the activity scope
    private val timeLineViewModel: TimeLineViewModel by viewModel()
    private val appDataViewModel: AppDataViewModel by activityViewModels()
    private lateinit var birthdayAdapter: BirthdayAdapter
    // Using the activityViewModels() Kotlin property delegate from the
    // fragment-ktx artifact to retrieve the ViewModel in the activity scope

    companion object {
        fun newInstance(page: Int, dayFilter: String): Fragment {
            val fragment = BirthdayFragment()
            val bundle = Bundle()
            bundle.putInt("var_arg", page)
            bundle.putString("day_filter", dayFilter)
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
            inflater, container, R.layout.fragment_birthday
        ) as FragmentBirthdayBinding
        return binding.root
    }

    override fun init() {
        tabPosition = arguments?.getInt("var_arg") ?: 0
        dayFilter = arguments?.getString("day_filter") ?: getString(R.string.today)
        Trace.i("$tabPosition")
        val date = Calendar.getInstance()
        Trace.e("week day: " + date.time.nextWeekDay().toDateString(DateUtil.y4M2d2))
        binding.run {
            hasBirthdays = false
            rvBirthday.setHasFixedSize(true)
            rvBirthday.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            context?.let {
                birthdayAdapter = BirthdayAdapter(it, tabPosition)
                rvBirthday.adapter = birthdayAdapter
            }
        }
        refreshBirthDays(dayFilter)

    }

    fun refreshBirthDays(dayFilter: String) {
        when (dayFilter) {
            getString(R.string.today) -> {
                Trace.i("Today")
                fetchBirthdays(currentDate, currentDate)
            }
            getString(R.string.tomorrow) -> {
                Trace.i("Tomorrow")

                fetchBirthdays(
                    currentDate,
                    Calendar.getInstance().time.nextDay().toDateString(DateUtil.y4M2d2)
                )
            }
            else -> {
                Trace.i("Week")
                fetchBirthdays(
                    currentDate,
                    Calendar.getInstance().time.nextWeekDay().toDateString(DateUtil.y4M2d2)
                )
            }
        }
    }

    private fun fetchBirthdays(fromDate: String, toDate: String, size: Int = 10, page: Int = 0) {

        lifecycleScope.launchWhenResumed {
            timeLineViewModel.fetchBirthdays(fromDate, toDate, size, page, tabPosition == 1)
                .collectLatest {
                    when (it) {
                        is BirthdaysAction.Success -> {
                            Trace.e("App Birthdays : " + it.response)
                            birthdayAdapter.list = it.response.content
                            binding.hasBirthdays = it.response.content.isNotEmpty()
                            appDataViewModel.setBirthdaysCount(it.response.content.size)
                        }
                        is BirthdaysAction.Fail -> {
                            binding.hasBirthdays = false
                        }
                    }
                }
        }

    }
}