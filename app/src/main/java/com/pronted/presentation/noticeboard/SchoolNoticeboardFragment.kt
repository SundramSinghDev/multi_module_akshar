package com.pronted.presentation.noticeboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.base.BaseActivity
import com.base.BaseFragment
import com.base.inflateFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.pronted.R
import com.pronted.databinding.FragmentSchoolNoticeboardBinding
import com.pronted.presentation.home.ChildActivity
import com.pronted.presentation.home.SmartSchoolActivity
import com.pronted.presentation.timeline.AppDataViewModel
import com.pronted.presentation.timeline.birthday.seeall.AllBirthdaysViewPagerAdapter
import com.pronted.presentation.timeline.birthday.seeall.BirthdaysPagerFragment
import com.pronted.utils.SecurityResponseLabel
import com.pronted.utils.extentions.*
import io.paperdb.Paper

class SchoolNoticeboardFragment : BaseFragment<FragmentSchoolNoticeboardBinding>() {
    private lateinit var noticeboardViewPagerAdapter: NoticeboardViewPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_school_noticeboard
        ) as FragmentSchoolNoticeboardBinding
        return binding.root
    }

    override fun init() {
        binding.run {
            noticeboardViewPagerAdapter = createNoticeViewPagerAdapter()
            nbViewPager.adapter = noticeboardViewPagerAdapter

            TabLayoutMediator(
                nbTabLayout, nbViewPager
            ) { tab, position ->
                tab.text = getTabTitle(position)
            }.attach()
            nbViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    if (state == ViewPager2.SCROLL_STATE_IDLE) {
                        fetchNotices()
                    }
                }
            })

            Paper.book().read<ArrayList<String>>(SECURITY_GROUP_LIST)
                ?.let { securityGroupList ->
                    fabAddNotice.visibility =
                        if (securityGroupList.contains(SecurityResponseLabel.ME_NOTICE_BOARD_ADD)) View.VISIBLE else View.GONE
                }
            fabAddNotice.setOnClickListener {
                context?.let {
                    startNewActivity(
                        it, ChildActivity::class.java, bundle = bundleOf(
                            Pair(NAV_DESTINATION, R.id.createNotice)
                        )
                    )
                }
            }
        }
    }

    private fun getTabTitle(position: Int): String {
        return when (position) {
            0 -> getString(R.string.active)
            1 -> getString(R.string.expired)
            else -> getString(
                R.string.upcoming
            )
        }
    }

    private fun fetchNotices() {
        val fragment =
            noticeboardViewPagerAdapter.getRegisteredFragment(binding.nbViewPager.currentItem) as NoticeboardPagerFragment
        fragment.refreshNotices()
    }

    private fun createNoticeViewPagerAdapter(): NoticeboardViewPagerAdapter {
        return NoticeboardViewPagerAdapter(
            context as BaseActivity<*>
        )
    }

    override fun resume() {
        (activity as BaseActivity<*>).title(getString(R.string.noticeboard))
    }
}