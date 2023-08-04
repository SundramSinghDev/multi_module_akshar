package com.pronted.presentation.timeline.birthday.seeall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.base.BaseActivity
import com.base.BaseFragment
import com.base.Trace
import com.base.inflateFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.pronted.R
import com.pronted.databinding.FragmentBirthdaysBinding
import com.pronted.presentation.home.ChildActivity
import com.pronted.presentation.home.ViewPagerAdapter
import com.pronted.presentation.timeline.AppDataViewModel
import com.pronted.presentation.timeline.birthday.BirthdayFragment
import com.pronted.utils.extentions.EMPLOYEES
import com.pronted.utils.extentions.NAV_OBJECT
import com.pronted.utils.extentions.NAV_OBJECT2
import com.pronted.utils.extentions.STUDENTS

class BirthDaysFragment : BaseFragment<FragmentBirthdaysBinding>() {
    private lateinit var birthdayViewPagerAdapter: AllBirthdaysViewPagerAdapter
    private val appDataViewModel: AppDataViewModel by activityViewModels()
    private var selectedTab = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_birthdays
        ) as FragmentBirthdaysBinding
        return binding.root
    }
    override fun init() {
        binding.run {
            binding.tvBirthdayFilter.text = arguments?.getString(NAV_OBJECT)?: getString(R.string.today)
            selectedTab = arguments?.getInt(NAV_OBJECT2) ?: 0
            birthdayViewPagerAdapter = createBirthdayAdapter()
            bdViewPager.adapter = birthdayViewPagerAdapter
            tvBirthdayFilter.setOnClickListener {
                openFilterMenuPopup()
            }
            TabLayoutMediator(
                bdTabLayout, bdViewPager
            ) { tab, position ->
                tab.text = if (position == 0) STUDENTS else EMPLOYEES
            }.attach()
            bdViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    if (state == ViewPager2.SCROLL_STATE_IDLE) {
                        filterBirthdays(tvBirthdayFilter.text.toString())
                    }
                }
            })
            bdTabLayout.getTabAt(selectedTab)?.select()

            appDataViewModel.birthDaysCount.observe(context as ChildActivity) {
                Trace.e("Child Fragment Birthday count:$it")
                setBirthdayTabBadge(it)
            }
        }
    }
    override fun resume() {
        (activity as BaseActivity<*>).title(getString(R.string.birthdays))
    }

    private fun createBirthdayAdapter(): AllBirthdaysViewPagerAdapter {
        return AllBirthdaysViewPagerAdapter(
            context as ChildActivity,
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
            birthdayViewPagerAdapter.getRegisteredFragment(binding.bdViewPager.currentItem) as BirthdaysPagerFragment
        fragment.refreshBirthDays(dayFilter)
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
}