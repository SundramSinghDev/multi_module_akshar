package com.pronted.presentation.timeline

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.*
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.pronted.R
import com.pronted.databinding.FragmentParentTimelineBinding
import com.pronted.dto.attendance.AttendanceAction
import com.pronted.dto.attendance.AttendanceChart
import com.pronted.dto.attendance.AttendanceChartAction
import com.pronted.dto.feepayments.data.FeesDetailModel
import com.pronted.dto.login.ImageAction
import com.pronted.dto.login.SecurityGroupAction
import com.pronted.dto.login.UserAppList
import com.pronted.dto.student.StudentProfileAction
import com.pronted.dto.timeline.FeeDetailsAction
import com.pronted.dto.timeline.NoticeListAction
import com.pronted.dto.timeline.TimetableAction
import com.pronted.presentation.attendance.AttendanceViewModel
import com.pronted.presentation.home.ChildActivity
import com.pronted.presentation.home.SmartParentActivity
import com.pronted.presentation.listener.ItemClickListener
import com.pronted.presentation.noticeboard.NoticeboardViewModel
import com.pronted.presentation.profile.StudentViewModel
import com.pronted.presentation.timeline.notice.NoticeAdapter
import com.pronted.presentation.userapps.ImagesViewModel
import com.pronted.utils.DateUtil
import com.pronted.utils.DateUtil.getDateByTimeFilter
import com.pronted.utils.databinding.loadImage
import com.pronted.utils.extentions.*
import com.pronted.utils.toDateString
import io.paperdb.Paper
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ParentTimelineFragment : BaseFragment<FragmentParentTimelineBinding>() {
    private var securityGroupList = arrayListOf<String>()
    private val imagesViewModel: ImagesViewModel by viewModel()
    private val appDataViewModel: AppDataViewModel by activityViewModels()
    private val studentViewModel: StudentViewModel by viewModel()
    private val timeLineViewModel: TimeLineViewModel by viewModel()

    private val noticeboardViewModel: NoticeboardViewModel by viewModel()
    private val attendance: AttendanceViewModel by viewModel()
    private lateinit var childrenAdapter: ChildrenTimeLineAdapter
    private lateinit var timeTableAdapter: TimeTableAdapter
    private lateinit var noticeAdapter: NoticeAdapter
    private var studentProfileId: Int = 0
    private var childrenImagesLoaded: Boolean = false
    private val currentDate by lazy {
        Calendar.getInstance().time.toDateString(DateUtil.y4M2d2)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_parent_timeline
        ) as FragmentParentTimelineBinding
        return binding.root
    }

    override fun init() {
        binding.run {
            context?.let { context ->
                Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.let {
                    Trace.e("Selected Role:$it")
                    studentProfileId = it.userUniqueId
                    tvChildrenName.text = it.studentName
                    tvClassSection.text = it.className
                    fetchSecurityGroupList(it.appName)
                    fetchStudentProfile()
                }

                //Children UI
                rvStudentList.setHasFixedSize(true)
                rvStudentList.layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                childrenAdapter = ChildrenTimeLineAdapter(context, childrenItemClickListener)
                rvStudentList.adapter = childrenAdapter
                imgToggleDownArrow.setOnClickListener {
                    showChildren = true
                }
                imgToggleUpArrow.setOnClickListener {
                    showChildren = false
                }

                //Timetable UI
                rvClassRooms.setHasFixedSize(true)
                rvClassRooms.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                timeTableAdapter = TimeTableAdapter(context)
                rvClassRooms.adapter = timeTableAdapter
                fetchChildrenTimeTable()

                //Noticeboard UI
                rvNoticeBoard.setHasFixedSize(true)
                rvNoticeBoard.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                noticeAdapter = NoticeAdapter(context)
                rvNoticeBoard.adapter = noticeAdapter

                tvSeeAllNotices.setOnClickListener {
                    startNewActivity(
                        context, ChildActivity::class.java, bundle = bundleOf(
                            Pair(NAV_SOURCE, R.id.navigation_time_line),
                            Pair(NAV_DESTINATION, R.id.parentNoticeboard),
                        )
                    )
                }
                fetchNoticeList()

                //Attendance UI
                filterAttendanceTime.setOnClickListener {
                    openAttendanceFilterMenu()
                }
                tvSeeAllFees.setOnClickListener {
                    val bundle = bundleOf(
                        Pair(NAV_DESTINATION, R.id.studentFeePayments),
                        Pair(NAV_OBJECT, Paper.book().read(STUDENT_PROFILE)),
                    )
                    startNewActivity(
                        context, ChildActivity::class.java, bundle = bundle
                    )
                }
                initPieChart(pieChartAttendance, true)
                initPieChart(pieChart, false)
                fetchTodayAttendance()
                filterAttendance()
                fetchFeeDetails()
                processChildrenImages()
            }
        }
    }

    override fun resume() {
        (activity as BaseActivity<*>).title(getString(R.string.dashboard))
    }

    private val childrenItemClickListener = object : ItemClickListener<UserAppList> {
        @SuppressLint("NotifyDataSetChanged")
        override fun onClicked(item: UserAppList, positoin: Int) {
            Trace.i("Selected Children id: $item")
            Paper.book().write(USER_SELECTED_ROLE, item)
            (context as SmartParentActivity).refreshActivity()
        }
    }

    private fun initPieChart(pieChart: PieChart, usePercent: Boolean = false) {
        //using percentage as values instead of amount
        pieChart.setUsePercentValues(usePercent)
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

    private fun openAttendanceFilterMenu() {
        context?.let {
            val popup = PopupMenu(it, binding.filterAttendanceTime)
            //Inflating the Popup using xml file
            popup.menuInflater.inflate(R.menu.filter_attendance_time, popup.menu)
            popup.setOnMenuItemClickListener { menuItem ->
                menuItem.title?.let { title ->
                    binding.filterAttendanceTime.text = title
                    filterAttendance()
                }
                true
            }
            popup.show()
        }
    }

    private fun filterAttendance() {
        if (binding.filterAttendanceTime.text.toString() == getString(R.string.this_year)) {
            fetchAttendanceStatsForYear()
        } else {
            context?.let {
                fetchAttendanceForTime(
                    getDateByTimeFilter(
                        it,
                        binding.filterAttendanceTime.text.toString()
                    ), currentDate
                )
            }
        }
    }

    private fun updateChildrenAdapter() {
        Paper.book().read<List<UserAppList>>(CHILDREN_LIST)
            ?.let { list ->
                childrenAdapter.list = list
            }
    }

    private fun loadStudentProfileImage(imageUrl: String? = null) {
        binding.run {
            context?.let { context ->
                if (imageUrl.isNullOrBlank()) {
                    ivProfile.visibility = View.GONE
                    ivTextProfile.visibility = View.VISIBLE
                    tvShortName.text = Paper.book()
                        .read<UserAppList>(USER_SELECTED_ROLE)?.studentName?.substring(0, 2)
                        ?.uppercase()
                    ivTextBg.setImageResource(R.color.light_pink)
                    tvShortName.setTextColor(context.getColor(R.color.pink))

                } else {
                    ivProfile.visibility = View.VISIBLE
                    ivTextProfile.visibility = View.GONE
                    AppCompatResources.getDrawable(context, R.drawable.ic_user)
                        ?.let { loadImage(ivProfile, imageUrl, it) }
                }
            }
        }
    }


    private fun loadAttendanceData(response: ArrayList<AttendanceChart>) {
        if (response.isNotEmpty()) {
            var totalPercentage = 0.0
            var presentPercentage = 0.0
            var absentPercentage = 0.0
            var latePercentage = 0.0
            for (model in response) {
                totalPercentage += model.value
            }
            if (totalPercentage > 0) {
                for (i in 0 until response.size) {
                    when (response[i].key) {
                        "P" -> {
                            presentPercentage = (((response[i].value / totalPercentage) * 100))
                            (String.format(
                                "%.0f",
                                presentPercentage
                            ) + " %").also { binding.tvPresentPercent.text = it }
                        }
                        "A" -> {
                            absentPercentage = (((response[i].value / totalPercentage) * 100))
                            (String.format(
                                "%.0f",
                                absentPercentage
                            ) + " %").also { binding.tvAbsentPercent.text = it }
                        }
                        "L" -> {
                            latePercentage = (((response[i].value / totalPercentage) * 100))
                            (String.format(
                                "%.0f",
                                latePercentage
                            ) + " %").also { binding.tvOnLatePercent.text = it }
                        }
                    }
                }

                Trace.i("Total Per: $totalPercentage, Present: $presentPercentage, absent: $absentPercentage, late: $latePercentage")
                val pieEntries: ArrayList<PieEntry> = ArrayList()
                pieEntries.add(PieEntry(presentPercentage.toFloat(), getString(R.string.present)))
                pieEntries.add(PieEntry(absentPercentage.toFloat(), getString(R.string.absent)))
                pieEntries.add(PieEntry(latePercentage.toFloat(), getString(R.string.on_late)))
                //collecting the entries with label name
                val pieDataSet = PieDataSet(pieEntries, "Attendance")
                //setting text size of the value
                pieDataSet.valueTextSize = 12f

                val colorArray = resources.getIntArray(R.array.colors_chart_attendance)
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

                binding.pieChartAttendance.data = pieData
                binding.pieChartAttendance.invalidate()

            }
        }

    }

    private fun loadFeeDetails(feeDetails: ArrayList<FeesDetailModel>) {
        if (feeDetails.isNotEmpty()) {
            var paidAmount = 0F
            var dueAmount = 0F
            var overDueAmount = 0F
            for (data in feeDetails[0].feeHeadData) {
                for (price in data.feeTerms) {
                    paidAmount += price.paidAmount
                    dueAmount += price.dueAmount
                    overDueAmount += price.overdueAmount
                }
            }

            String.format(
                getString(R.string.rupee_symbol_format),
                paidAmount.toInt().getFormattedAmount()
            ).run {
                binding.tvPaidAmount.text = this
            }
            String.format(
                getString(R.string.rupee_symbol_format),
                dueAmount.toInt().getFormattedAmount()
            ).run {
                binding.tvDueAmount.text = this
            }
            String.format(
                getString(R.string.rupee_symbol_format),
                overDueAmount.toInt().getFormattedAmount()
            ).run {
                binding.tvOverDueAmount.text = this
            }

            val pieEntries: ArrayList<PieEntry> = ArrayList()
            pieEntries.add(PieEntry(paidAmount, getString(R.string.paid)))
            pieEntries.add(PieEntry(dueAmount, getString(R.string.due)))
            pieEntries.add(PieEntry(overDueAmount, getString(R.string.over_due)))
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

            binding.pieChart.data = pieData
            binding.pieChart.invalidate()
        }

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
                                }
                        }
                    }
                }
        }
    }

    /**
     * Fetch student profile
     */
    private fun fetchStudentProfile() {
        lifecycleScope.launchWhenResumed {
            studentViewModel.fetchStudentProfile()
                .collectLatest { profileAction ->
                    when (profileAction) {
                        is StudentProfileAction.Success -> {
                            Trace.e("${profileAction.studentProfileResponse}")
                            Paper.book()
                                .write(STUDENT_PROFILE, profileAction.studentProfileResponse)
                            appDataViewModel.setStudentProfile(profileAction.studentProfileResponse)
                            fetchUserProfileImage(
                                profileAction.studentProfileResponse.studentProfileId,
                                STUDENTS
                            )
                        }
                        is StudentProfileAction.Fail -> {
                            Trace.i(
                                profileAction.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                        }
                    }
                }
        }
    }

    /**
     * Fetch employee details
     */
    private fun fetchUserProfileImage(employeeProfileId: Int, module: String = EMPLOYEES) {
        lifecycleScope.launchWhenResumed {
            imagesViewModel.fetchProfileImage(headers = null, module, employeeProfileId.toString())
                .collectLatest { empImageAction ->
                    when (empImageAction) {
                        is ImageAction.Success -> {
                            empImageAction.imageResponse.imageUrl?.let { it1 ->
                                // Paper.book().write(EMPLOYEE_IMAGE, it1)
                                //binding.profileImage = it1
                                appDataViewModel.setStudentProfileImage(it1)
                                loadStudentProfileImage(it1)
                            }
                        }
                        is ImageAction.Fail -> {
                            Trace.i(
                                empImageAction.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                            loadStudentProfileImage()
                        }
                    }
                }
        }
    }

    /**
     * Fetch time table of Employee
     */
    private fun fetchChildrenTimeTable() {
        Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.let {
            lifecycleScope.launchWhenResumed {
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
                timeLineViewModel.fetchTimetable(it.userUniqueId, currentDate, false, headers)
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
     * Fetch time table of Employee
     */
    private fun fetchNoticeList(status: String = "ACTIVE") {
        lifecycleScope.launchWhenResumed {
            noticeboardViewModel.fetchAllNotices(status)
                .collectLatest { action ->
                    when (action) {
                        is NoticeListAction.Success -> {
                            Trace.e("Emp Timetable : " + action.response)
                            noticeAdapter.list = action.response.content
                            binding.hasNotices = action.response.content.isNotEmpty()
                            binding.tvSeeAllNotices.visibility =
                                if (action.response.content.isNotEmpty()) View.VISIBLE else View.GONE
                        }
                        is NoticeListAction.Fail -> {
                            Trace.i(
                                action.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                            binding.hasNotices = false
                            binding.tvSeeAllNotices.visibility = View.GONE
                        }
                    }
                }
        }
    }

    /**
     * Fetch time table of Employee
     */
    private fun fetchFeeDetails() {
        lifecycleScope.launchWhenResumed {
            timeLineViewModel.fetchFeeDetails(studentProfileId)
                .collectLatest { action ->
                    when (action) {
                        is FeeDetailsAction.Success -> {
                            Trace.e("Emp Timetable : " + action.response)
                            binding.hasFeeData = action.response.isNotEmpty()
                            binding.tvSeeAllFees.visibility =
                                if (action.response.isNotEmpty()) View.VISIBLE else View.GONE
                            loadFeeDetails(action.response)
                        }
                        is FeeDetailsAction.Fail -> {
                            Trace.i(
                                action.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                            binding.hasFeeData = false
                            binding.tvSeeAllFees.visibility = View.GONE
                        }
                    }
                }
        }
    }

    private fun fetchAttendanceForTime(fromDate: String, toDate: String) {
        lifecycleScope.launchWhenResumed {
            attendance.fetchAttendanceStatsByTime(fromDate, toDate, studentProfileId)
                .collectLatest { action ->
                    when (action) {
                        is AttendanceChartAction.Success -> {
                            Trace.e("Child attendance : " + action.response)
                            binding.hasAttendance = action.response.isNotEmpty()
                            loadAttendanceData(action.response)
                        }
                        is AttendanceChartAction.Fail -> {
                            Trace.i(
                                action.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                            binding.hasAttendance = false
                        }
                    }
                }
        }
    }


    private fun fetchAttendanceStatsForYear() {
        lifecycleScope.launchWhenResumed {
            attendance.fetchAttendanceStatsByYearly(studentProfileId)
                .collectLatest { action ->
                    when (action) {
                        is AttendanceChartAction.Success -> {
                            Trace.e("Child attendance : " + action.response)
                            binding.hasAttendance = action.response.isNotEmpty()
                            loadAttendanceData(action.response)
                        }
                        is AttendanceChartAction.Fail -> {
                            Trace.i(
                                action.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                            binding.hasAttendance = false
                        }
                    }
                }
        }
    }

    private fun fetchTodayAttendance() {
        lifecycleScope.launchWhenResumed {
            attendance.fetchTodayAttendance(studentProfileId)
                .collectLatest { action ->
                    when (action) {
                        is AttendanceAction.Success -> {
                            Trace.e("Child attendance : " + action.response)
                            binding.tvAttendanceLabel.text = String.format(
                                getString(
                                    R.string.attendance_status,
                                    action.response.attendanceStatus
                                )
                            )
                        }
                        is AttendanceAction.Fail -> {
                            Trace.i(
                                action.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                            binding.tvAttendanceLabel.text = "N/A"
                        }
                    }
                }
        }
    }

    //profile image processing for children
    private var childImageCounter = 0
    private var totalChildImageCount = 0
    private var childrenList: List<UserAppList> = listOf()

    private fun processChildrenImages() {
        if (!childrenImagesLoaded)
            Paper.book().read<List<UserAppList>>(CHILDREN_LIST)
                ?.let { list ->
                    childrenList = list
                    totalChildImageCount = list.size
                    fetchChildrenProfileImage(list[childImageCounter])
                    childrenImagesLoaded = true
                }
    }

    private fun saveAndProcessNextChildImage() {
        if (childImageCounter < totalChildImageCount - 1) {
            childImageCounter++
            fetchChildrenProfileImage(childrenList[childImageCounter])
        } else {
            childImageCounter = 0
            totalChildImageCount = 0
            Paper.book().write(CHILDREN_LIST, childrenList)
            updateChildrenAdapter()
        }

    }


    private fun fetchChildrenProfileImage(userAppList: UserAppList) {
        lifecycleScope.launchWhenResumed {
            val headers = mapOf(
                Pair("Authorization", Preference.instance.getString(ACCESS_TOKEN) ?: ""),
                Pair("APP_NAME", "SmartParent"),
                Pair("SCHOOL_CODE", userAppList.schoolCode),
                Pair("STUDENT_PROFILE_ID", userAppList.userUniqueId.toString()),
            )
            Preference.instance.putBoolean(DYNAMIC_HEADERS, true)
            imagesViewModel.fetchProfileImage(
                headers,
                STUDENTS,
                userAppList.userUniqueId.toString()
            )
                .collectLatest { imageAction ->
                    Preference.instance.putBoolean(DYNAMIC_HEADERS, false)
                    when (imageAction) {
                        is ImageAction.Success -> {
                            childrenList[childImageCounter].profileImage =
                                imageAction.imageResponse.imageUrl
                            Paper.book().write(CHILDREN_LIST, childrenList)
                            saveAndProcessNextChildImage()
                        }
                        is ImageAction.Fail -> {
                            Trace.i(
                                imageAction.errorResponse.message
                                    ?: getString(R.string.something_went_wrong_try_again)
                            )
                            saveAndProcessNextChildImage()
                        }
                    }
                }
        }
    }

}