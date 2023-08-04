package com.pronted.presentation.timeline


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import com.base.*
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.tabs.TabLayoutMediator
import com.pronted.R
import com.pronted.databinding.FragmentSchoolTimelineBinding
import com.pronted.dto.login.SecurityGroupAction
import com.pronted.dto.login.UserAppList
import com.pronted.dto.timeline.*
import com.pronted.presentation.home.ChildActivity
import com.pronted.presentation.home.SmartSchoolActivity
import com.pronted.presentation.home.ViewPagerAdapter
import com.pronted.presentation.noticeboard.NoticeboardViewModel
import com.pronted.presentation.timeline.birthday.BirthdayFragment
import com.pronted.presentation.timeline.finance.CollectionAdapter
import com.pronted.presentation.userapps.ImagesViewModel
import com.pronted.utils.*
import com.pronted.utils.DateUtil.getDateByTimeFilter
import com.pronted.utils.SecurityResponseLabel.Companion.ME_DASHBOARD_BIRTHDAYS_VIEW
import com.pronted.utils.SecurityResponseLabel.Companion.ME_DASHBOARD_FINANCE_VIEW
import com.pronted.utils.extentions.*
import io.paperdb.Paper
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList


class SchoolTimelineFragment : BaseFragment<FragmentSchoolTimelineBinding>() {
    private var securityGroupList = arrayListOf<String>()
    private lateinit var timeTableAdapter: TimeTableAdapter
    private val timeLineViewModel: TimeLineViewModel by viewModel()
    private val appDataViewModel: AppDataViewModel by activityViewModels()
    private val imagesViewModel: ImagesViewModel by viewModel()
    private lateinit var birthdayViewPagerAdapter: ViewPagerAdapter
    private lateinit var collectionAdapter: CollectionAdapter
    private lateinit var expensesAdapter: CollectionAdapter
    private val date: Date by lazy {
        Calendar.getInstance().time
    }
    private val currentDate: String by lazy {
        date.toDateString(DateUtil.y4M2d2)
    }

    private val thisYear: String by lazy {
        Calendar.getInstance().firstDayOfCurrentYear().toDateString(DateUtil.y4M2d2)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_school_timeline
        ) as FragmentSchoolTimelineBinding
        return binding.root
    }


    override fun resume() {
        (activity as BaseActivity<*>).title(getString(R.string.dashboard))
    }


    override fun init() {

        binding.run {
            setTimeTableAdapter()
            Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.let {
                fetchSecurityGroupList(it.appName)
            }
            fetchEmployeeTimeTable()
            //Birthday
            birthdayViewPagerAdapter = createBirthdayAdapter()
            bdViewPager.adapter = birthdayViewPagerAdapter
            tvBirthdayFilter.setOnClickListener {
                openFilterMenuPopup()
            }
            tvSeeAllBirthdays.setOnClickListener {
                context?.let {
                    startNewActivity(
                        it, ChildActivity::class.java, bundle = bundleOf(
                            Pair(NAV_SOURCE, R.id.navigation_time_line),
                            Pair(NAV_DESTINATION, R.id.birthdays),
                            Pair(NAV_OBJECT, tvBirthdayFilter.text.toString()),
                            Pair(NAV_OBJECT2,  binding.bdTabLayout.selectedTabPosition)
                        )
                    )
                }
            }
            TabLayoutMediator(
                bdTabLayout, bdViewPager
            ) { tab, position ->
                tab.text = if (position == 0) STUDENTS else EMPLOYEES
            }.attach()
            bdViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    if (state == SCROLL_STATE_IDLE) {
                        filterBirthdays(tvBirthdayFilter.text.toString())

                    }
                }
            })

            appDataViewModel.birthDaysCount.observe(context as SmartSchoolActivity) {
                Trace.e("Fragment Birthday count:$it")
                setBirthdayTabBadge(it)
                binding.tvSeeAllBirthdays.visibility = if (it > 0) View.VISIBLE else View.GONE
            }

            //finance UI
            filterCollectionTime.setOnClickListener {
                openTimeFilter()
            }
            filterCollectionCategory.setOnClickListener {
                openCollectionCategoryFilter()
            }
            filterExpensesTime.setOnClickListener {
                openTimeFilter(false)
            }
            filterExpensesCategory.setOnClickListener {
                openExpensesCategoryFilter()
            }
        }
    }

    private fun validateUI() {
        binding.run {
            securityGroupList =
                Paper.book().read<ArrayList<String>>(SECURITY_GROUP_LIST) ?: arrayListOf()

            securityGroupList.contains(ME_DASHBOARD_BIRTHDAYS_VIEW).run {
                clBirthdays.visibility = if (this) View.VISIBLE else View.GONE
                cardBirthdays.visibility = if (this) View.VISIBLE else View.GONE
            }
            Trace.e(
                "******* Got security list" + securityGroupList.contains(
                    ME_DASHBOARD_FINANCE_VIEW
                ) + "and " + securityGroupList.contains(ME_DASHBOARD_FINANCE_VIEW)
            )
            if (securityGroupList.contains(ME_DASHBOARD_FINANCE_VIEW)) {
                clFinance.visibility = View.VISIBLE
                clCollection.visibility = View.VISIBLE
                clExpenses.visibility = View.VISIBLE
                initCollectionAdapter()
                initPieChart(pieChart)
                initPieChart(pieChartCollection)
                initPieChart(pieChartExpense)
                fetchFinanceSummary()
                filterExpensesData()
                filterCollectionData()
            } else {
                clFinance.visibility = View.GONE
                clCollection.visibility = View.GONE
                clExpenses.visibility = View.GONE
            }
        }
    }

    private fun initCollectionAdapter() {
        binding.run {
            context?.let {
                rvCollection.setHasFixedSize(true)
                rvCollection.layoutManager =
                    LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
                collectionAdapter = CollectionAdapter(it)
                rvCollection.adapter = collectionAdapter

                rvExpenses.setHasFixedSize(true)
                rvExpenses.layoutManager =
                    LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
                //Log.e("size array",expenceList.size.toString())
                expensesAdapter = CollectionAdapter(it)
                rvExpenses.adapter = expensesAdapter
            }

        }
    }

    /**
     * Show birthdays count badge for selected tab only. hides other tab badge
     */
    private fun setBirthdayTabBadge(count: Int = 0) {
        //set the badge
        binding.bdTabLayout.run {
            val badgeDrawable = getTabAt(selectedTabPosition)?.orCreateBadge
            badgeDrawable?.let {
                it.isVisible = count > 0
                it.number = count
            }
            val otherTabPosition: Int = if (selectedTabPosition == 1) 0 else 1
            val otherBadgeDrawable = getTabAt(otherTabPosition)?.orCreateBadge
            otherBadgeDrawable?.let {
                it.isVisible = false
                it.number = 0
            }
        }
    }

    private fun setTimeTableAdapter() {
        context?.let {
            binding.rvClassRooms.setHasFixedSize(true)
            binding.rvClassRooms.layoutManager =
                LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)
            timeTableAdapter = TimeTableAdapter(it)
            binding.rvClassRooms.adapter = timeTableAdapter
        }
    }

    private fun createBirthdayAdapter(): ViewPagerAdapter {
        return ViewPagerAdapter(
            context as SmartSchoolActivity,
            binding.tvBirthdayFilter.text.toString()
        )
    }

    private fun openFilterMenuPopup() {
        context?.let {
            val popup = PopupMenu(it, binding.tvBirthdayFilter)
            //Inflating the Popup using xml file
            popup.menuInflater.inflate(R.menu.birthday_popup, popup.menu)
            popup.setOnMenuItemClickListener { menuItem ->
                menuItem.title?.let { title ->
                    binding.tvBirthdayFilter.text = title
                    filterBirthdays(title.toString())
                }
                true
            }
            popup.show()
        }
    }

    private fun filterBirthdays(dayFilter: String) {
        val fragment =
            birthdayViewPagerAdapter.getRegisteredFragment(binding.bdViewPager.currentItem) as BirthdayFragment
        fragment.refreshBirthDays(dayFilter)
    }

    private fun openTimeFilter(isCollection: Boolean = true) {
        context?.let {
            val popup = PopupMenu(
                it,
                if (isCollection) binding.filterCollectionTime else binding.filterExpensesTime
            )
            //Inflating the Popup using xml file
            popup.menuInflater.inflate(R.menu.filter_collection_time, popup.menu)
            popup.setOnMenuItemClickListener { menuItem ->
                menuItem.title?.let { title ->
                    if (isCollection)
                        binding.filterCollectionTime.text = title
                    else
                        binding.filterExpensesTime.text = title
                    filterFinanceDataByTimeAndType(isCollection)
                }
                true
            }
            popup.show()
        }
    }

    private fun openCollectionCategoryFilter() {
        context?.let {
            val popup = PopupMenu(it, binding.filterCollectionCategory)
            //Inflating the Popup using xml file
            popup.menuInflater.inflate(R.menu.filter_collection_category, popup.menu)
            popup.setOnMenuItemClickListener { menuItem ->
                menuItem.title?.let { title ->
                    binding.filterCollectionCategory.text = title
                    filterCollectionData()
                }
                true
            }
            popup.show()
        }
    }

    private fun openExpensesCategoryFilter() {
        context?.let {
            val popup = PopupMenu(it, binding.filterExpensesCategory)
            //Inflating the Popup using xml file
            popup.menuInflater.inflate(R.menu.filter_expense_category, popup.menu)
            popup.setOnMenuItemClickListener { menuItem ->
                menuItem.title?.let { title ->
                    binding.filterExpensesCategory.text = title
                    filterExpensesData()
                }
                true
            }
            popup.show()
        }
    }

    private fun filterFinanceDataByTimeAndType(isCollection: Boolean) {
        if (isCollection)
            filterCollectionData()
        else
            filterExpensesData()
    }

    private fun getGroupByString(groupBy: String): String {
        return when (groupBy) {
            getString(R.string.by_fee_head) -> {
                "FEE_HEAD"
            }
            getString(R.string.by_payment_method) -> {
                "PAYMENT_METHOD"
            }
            else -> {
                "CATEGORY"
            }
        }
    }


    private fun filterCollectionData() {
        context?.let {
            fetchCollectionSummary(
                getGroupByString(binding.filterCollectionCategory.text.toString()),
                getDateByTimeFilter(it, binding.filterCollectionTime.text.toString()),
                if (binding.filterCollectionTime.text.toString() == it.getString(R.string.yesterday)
                    || binding.filterCollectionTime.text.toString() == it.getString(R.string.tomorrow)) getDateByTimeFilter(
                    it,
                    binding.filterCollectionTime.text.toString()
                ) else currentDate
            )
        }
    }


    private fun filterExpensesData() {
        context?.let {
            fetchExpensesSummary(
                getGroupByString(binding.filterExpensesCategory.text.toString()),
                getDateByTimeFilter(it, binding.filterExpensesTime.text.toString()),
                if (binding.filterExpensesTime.text.toString() == it.getString(R.string.yesterday)
                    || binding.filterExpensesTime.text.toString() == it.getString(R.string.tomorrow)) getDateByTimeFilter(
                    it,
                    binding.filterExpensesTime.text.toString()
                ) else currentDate
            )
        }
    }

    /**
     * Fetch time table of Employee
     */
    private fun fetchEmployeeTimeTable() {
        Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.let {
            lifecycleScope.launchWhenResumed {
                val headers = mapOf(
                    Pair("Authorization", Preference.instance.getString(ACCESS_TOKEN) ?: ""),
                    Pair("APP_NAME", Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.appName ?: ""),
                    Pair(
                        "SCHOOL_CODE",
                        Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.schoolCode ?: ""
                    )
                )
                Preference.instance.putBoolean(DYNAMIC_HEADERS, true)
                timeLineViewModel.fetchTimetable(it.userUniqueId, currentDate, true, headers)
                    .collectLatest { timetableAction ->
                        when (timetableAction) {
                            is TimetableAction.Success -> {
                                Trace.e("Emp Timetable : " + timetableAction.response)
                                if (timetableAction.response.isNotEmpty()) {
                                    timetableAction.response[0].periodWiseTimetableList?.let {
                                        timeTableAdapter.list = it
                                    }
                                    showTimetableView(true)
                                }
                            }
                            is TimetableAction.Fail -> {
                                Trace.i(
                                    timetableAction.errorResponse.message ?: getString(
                                        R.string.something_went_wrong_try_again
                                    )
                                )
                                showTimetableView()
                            }
                        }
                    }
            }
        }
    }

    private fun showTimetableView(listAvailable: Boolean = false) {
        binding.rvClassRooms.visibility = if (listAvailable) View.VISIBLE else View.GONE
        binding.clNoTimeline.visibility = if (listAvailable) View.GONE else View.VISIBLE
    }

    /**
     * Fetch security group list
     */
    private fun fetchSecurityGroupList(appName: String) {
        lifecycleScope.launchWhenResumed {
            showLoader()
            imagesViewModel.fetchSecurityGroupList(appName)
                .collectLatest { securityGroupAction ->
                    dismissLoader()
                    when (securityGroupAction) {
                        is SecurityGroupAction.Success -> {
                            securityGroupAction.securityGroupList.let { it1 ->
                                securityGroupList = it1
                                Paper.book().write(SECURITY_GROUP_LIST, it1)
                                validateUI()
                                appDataViewModel.setSecurityGroups(it1)
                            }
                        }
                        is SecurityGroupAction.Fail -> {
                            Trace.i(
                                securityGroupAction.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                            Paper.book().read<ArrayList<String>>(SECURITY_GROUP_LIST)
                                ?.let {
                                    securityGroupList = it
                                    validateUI()
                                }
                        }
                    }
                }
        }
    }

    private fun fetchFinanceSummary() {
        lifecycleScope.launchWhenResumed {
            timeLineViewModel.fetchFinanceSummary(thisYear, currentDate)
                .collectLatest { action ->
                    when (action) {
                        is FinanceSummaryAction.Success -> {
                            Trace.e("Finance summary" + action.response)
                            loadFinanceData(action.response)
                            val feeSummary = action.response.feeSummary
                            binding.hasFinance =
                                !(feeSummary == null || (feeSummary.amountPaid == 0 && feeSummary.dueAmount == 0 && feeSummary.overdueAmount == 0))
                        }
                        is FinanceSummaryAction.Fail -> {
                            Trace.i(
                                action.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                            binding.hasFinance = false
                        }
                    }
                }
        }
    }

    private fun fetchCollectionSummary(groupBy: String, fromDate: String, toDate: String) {
        lifecycleScope.launchWhenResumed {

            Trace.e("from date:$fromDate, to date:$toDate")
            timeLineViewModel.fetchCollectionSummary(groupBy, fromDate, toDate)
                .collectLatest { action ->
                    when (action) {
                        is CollectionSummaryAction.Success -> {
                            Trace.e("Collection summary" + action.response)
                            loadCollectionsOrExpensesData(action.response)
                            binding.hasCollections = action.response.isNotEmpty()
                        }
                        is CollectionSummaryAction.Fail -> {
                            Trace.i(
                                action.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                            binding.hasCollections = false
                        }
                    }
                }
        }
    }

    private fun fetchExpensesSummary(groupBy: String, fromDate: String, toDate: String) {
        lifecycleScope.launchWhenResumed {
            Trace.e("from date:$fromDate, to date:$toDate")
            timeLineViewModel.fetchExpensesSummary(groupBy, fromDate, toDate)
                .collectLatest { action ->
                    when (action) {
                        is ExpensesSummaryAction.Success -> {
                            Trace.e("Expenses summary" + action.response)
                            binding.hasExpenses = action.response.isNotEmpty()
                            loadCollectionsOrExpensesData(action.response, isCollection = false)
                        }
                        is ExpensesSummaryAction.Fail -> {
                            Trace.i(
                                action.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                            binding.hasExpenses = false
                        }
                    }
                }
        }
    }

    private fun loadFinanceData(financeModel: FinanceModel) {
        binding.run {
            val pieEntries: ArrayList<PieEntry> = ArrayList()
            financeModel.feeSummary?.let {
                String.format(
                    getString(R.string.rupee_symbol_format),
                    it.amountPaid.getFormattedAmount()
                ).run {
                    tvPaidAmount.text = this
                    tvTotalFinanceAmount.text = this
                }
                String.format(
                    getString(R.string.rupee_symbol_format),
                    it.dueAmount.getFormattedAmount()
                ).run {
                    tvDueAmount.text = this
                }
                String.format(
                    getString(R.string.rupee_symbol_format),
                    it.overdueAmount.getFormattedAmount()
                ).run {
                    tvOverDueAmount.text = this
                }
                pieEntries.add(PieEntry(it.amountPaid.toFloat(), getString(R.string.paid)))
                pieEntries.add(PieEntry(it.dueAmount.toFloat(), getString(R.string.due)))
                pieEntries.add(PieEntry(it.overdueAmount.toFloat(), getString(R.string.over_due)))
                //collecting the entries with label name
                val pieDataSet = PieDataSet(pieEntries, "Overall Fee")
                //setting text size of the value
                pieDataSet.valueTextSize = 12f

                val colorArray = resources.getIntArray(R.array.colors_chart_fee_amount)
                    .copyOfRange(0, pieEntries.size)
                val colors: ArrayList<Int> = ArrayList()
                for (color in colorArray) {
                    colors.add(color)
                }
                //providing color list for coloring different entries
                pieDataSet.colors = colors
                //grouping the data set from entry to chart
                val pieData = PieData(pieDataSet)
                //showing the value of the entries, default true if not set
                pieData.setDrawValues(false)

                pieChart.data = pieData
                pieChart.invalidate()

            }

        }
    }

    @Synchronized
    private fun loadCollectionsOrExpensesData(
        feePayments: ArrayList<FeePayment>,
        isCollection: Boolean = true
    ) {
        binding.run {
            if (isCollection) {
                collectionAdapter.list = feePayments
                tvSeeAllCollections.visibility =
                    if (feePayments.size > 4) View.VISIBLE else View.GONE
            } else {
                expensesAdapter.list = feePayments
                tvSeeAllExpenses.visibility =
                    if (feePayments.size > 4) View.VISIBLE else View.GONE
            }

            val pieEntries: ArrayList<PieEntry> = ArrayList()
            val colorArray = resources.getIntArray(R.array.colors_chart_collections)
            val colors: ArrayList<Int> = ArrayList()
            var totalAmount = 0.0
            for (i in 0 until feePayments.size) {
                pieEntries.add(PieEntry(feePayments[i].value.toFloat(), feePayments[i].key))
                colors.add(colorArray[i % 12])
                totalAmount += feePayments[i].value.toDouble()
            }
            Trace.e("chart colors size: " + colors.size)
            String.format(
                getString(R.string.rupee_symbol_format),
                totalAmount.toInt().getFormattedAmount()
            ).run {
                if (isCollection)
                    tvTotalCollectionAmount.text = this
                else
                    tvTotalExpenseAmount.text = this
            }
            //collecting the entries with label name
            val pieDataSet = PieDataSet(pieEntries, if (isCollection) "collection" else "Expenses")
            //setting text size of the value
            pieDataSet.valueTextSize = 12f


            //providing color list for coloring different entries
            pieDataSet.colors = colors
            //grouping the data set from entry to chart
            val pieData = PieData(pieDataSet)
            //showing the value of the entries, default true if not set
            pieData.setDrawValues(false)
            if (isCollection) {
                pieChartCollection.data = pieData
                pieChartCollection.invalidate()
            } else {
                pieChartExpense.data = pieData
                pieChartExpense.invalidate()
            }
        }

    }

    private fun initPieChart(pieChart: PieChart) {
        //using percentage as values instead of amount
        pieChart.setUsePercentValues(false)
        //remove the description label on the lower left corner, default true if not set
        pieChart.description.isEnabled = false
        //enabling the user to rotate the chart, default true
        pieChart.isRotationEnabled = true
        //adding friction when rotating the pie chart
        pieChart.dragDecelerationFrictionCoef = 0.9f
        //setting the first entry start from right hand side, default starting from top
        pieChart.rotationAngle = 0F
        //highlight the entry when it is tapped, default true if not set
        pieChart.isHighlightPerTapEnabled = true
        //adding animation so the entries pop up from 0 degree
        pieChart.animateY(1400, Easing.EaseInOutQuad)
        //setting the color of the hole in the middle, default white
        pieChart.isDrawHoleEnabled = true
        pieChart.setTransparentCircleAlpha(0)
        pieChart.setTransparentCircleColor(R.color.white)
        pieChart.setDrawEntryLabels(false)
        pieChart.legend.isEnabled = false
        pieChart.setNoDataText("")
    }

}