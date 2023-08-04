package com.pronted.presentation.noticeboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.BaseActivity
import com.base.BaseFragment
import com.base.Trace
import com.base.inflateFragment
import com.pronted.R
import com.pronted.databinding.FragmentParentNoticeboardBinding
import com.pronted.dto.timeline.NoticeListAction
import com.pronted.presentation.timeline.notice.NoticeAdapter
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class ParentNoticeboardFragment : BaseFragment<FragmentParentNoticeboardBinding>() {
    private lateinit var noticeAdapter: ParentNoticeAdapter
    private val noticeboardViewModel: NoticeboardViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_parent_noticeboard
        ) as FragmentParentNoticeboardBinding
        return binding.root
    }

    override fun init() {
        binding.run {
            context?.let {
                //Noticeboard UI
                rvNoticeboard.setHasFixedSize(true)
                rvNoticeboard.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                noticeAdapter = ParentNoticeAdapter(it, showNotice = true)
                rvNoticeboard.adapter = noticeAdapter
                fetchNoticeList()
            }
        }
    }

    override fun resume() {
        (activity as BaseActivity<*>).title(getString(R.string.noticeboard))
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