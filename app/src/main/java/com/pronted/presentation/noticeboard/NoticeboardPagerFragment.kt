package com.pronted.presentation.noticeboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.BaseFragment
import com.base.Trace
import com.base.inflateFragment
import com.pronted.R
import com.pronted.databinding.FragmentNoticeboardPagerBinding
import com.pronted.dto.noticeboard.NoticeBoard
import com.pronted.dto.timeline.DeleteNoticeAction
import com.pronted.dto.timeline.NoticeListAction
import com.pronted.presentation.home.ChildActivity
import com.pronted.presentation.listener.NoticeListener
import com.pronted.presentation.timeline.AppDataViewModel
import com.pronted.utils.DateUtil
import com.pronted.utils.extentions.*
import com.pronted.utils.nextWeekDay
import com.pronted.utils.toDateString
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class NoticeboardPagerFragment : BaseFragment<FragmentNoticeboardPagerBinding>() {
    // Store instance variables
    private var tabPosition = 0
    private val noticeboardViewModel: NoticeboardViewModel by viewModel()
    private val currentDate by lazy {
        Calendar.getInstance().time.toDateString(DateUtil.y4M2d2)
    }

    // Using the activityViewModels() Kotlin property delegate from the
    // fragment-ktx artifact to retrieve the ViewModel in the activity scope
    private val appDataViewModel: AppDataViewModel by activityViewModels()
    private lateinit var noticeViewAdapter: NoticeViewAdapter

    companion object {
        fun newInstance(page: Int): Fragment {
            val fragment = NoticeboardPagerFragment()
            val bundle = Bundle()
            bundle.putInt("var_arg", page)
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
            inflater, container, R.layout.fragment_noticeboard_pager
        ) as FragmentNoticeboardPagerBinding
        return binding.root
    }

    override fun init() {
        tabPosition = arguments?.getInt("var_arg") ?: 0
        Trace.i("$tabPosition")
        val date = Calendar.getInstance()
        Trace.e("week day: " + date.time.nextWeekDay().toDateString(DateUtil.y4M2d2))
        binding.run {
            hasNotices = false
            rvNoticeboard.setHasFixedSize(true)
            rvNoticeboard.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            context?.let {
                noticeViewAdapter = NoticeViewAdapter(it, tabPosition, noticeListener)
                rvNoticeboard.adapter = noticeViewAdapter
            }
        }
        refreshNotices()

    }

    override fun resume() {
        refreshNotices()
    }

    fun refreshNotices() {
        noticeViewAdapter.selectedItemPos = -1
        when (tabPosition) {
            0 -> {
                fetchNotices(ACTIVE)
            }
            1 -> {
                fetchNotices(EXPIRED)
            }
            else -> {
                fetchNotices(UPCOMING)
            }
        }
    }

    private val noticeListener = object : NoticeListener<NoticeBoard> {
        override fun onEdit(item: NoticeBoard) {
            Trace.e("On Edit notice: $item")
            noticeViewAdapter.selectedItemPos = -1
            context?.let {
                startNewActivity(
                    it, ChildActivity::class.java, bundle = bundleOf(
                        Pair(NAV_DESTINATION, R.id.createNotice),
                        Pair(NAV_OBJECT, item)
                    )
                )
            }
        }

        override fun onDelete(item: NoticeBoard) {
            Trace.e("On Delete Notice: $item")
            deleteNotice(item)
        }
    }

    private fun deleteNotice(item: NoticeBoard) {
        lifecycleScope.launchWhenResumed {
            showLoader()
            noticeboardViewModel.deleteNotice(item.id)
                .collectLatest { action ->
                    dismissLoader()
                    when (action) {
                        is DeleteNoticeAction.Success -> {
                            Trace.e("Delete Notice : " + action.response)
                            refreshNotices()
                        }
                        is DeleteNoticeAction.Fail -> {
                            Trace.i(
                                action.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                            binding.hasNotices = false
                        }
                    }
                }
        }
    }

    private fun fetchNotices(status: String) {

        lifecycleScope.launchWhenResumed {
            noticeboardViewModel.fetchAllNotices(status)
                .collectLatest { action ->
                    when (action) {
                        is NoticeListAction.Success -> {
                            Trace.e("Emp Timetable : " + action.response)
                            noticeViewAdapter.list = action.response.content
                            binding.hasNotices = action.response.content.isNotEmpty()
                        }
                        is NoticeListAction.Fail -> {
                            Trace.i(
                                action.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                            binding.hasNotices = false
                        }
                    }
                }
        }

    }
}