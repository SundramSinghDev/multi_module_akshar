package com.pronted.presentation.timeline.birthday.seeall

import android.util.SparseArray
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pronted.presentation.listener.ItemsCountListener
import com.pronted.presentation.timeline.birthday.BirthdayFragment


class AllBirthdaysViewPagerAdapter(@NonNull fragmentActivity: FragmentActivity?,
                                   private val dayFilter:String) :
    FragmentStateAdapter(fragmentActivity!!) {
    private var counter = 0L
    private var itemIds: List<Long>
    private val registeredFragments: SparseArray<Fragment> = SparseArray<Fragment>()

    init {
        itemIds = generateListOfItemIds()

        val adapterDataObserver = object : RecyclerView.AdapterDataObserver() {

            override fun onChanged() {
                itemIds = generateListOfItemIds()
            }
        }

        registerAdapterDataObserver(adapterDataObserver)
    }

    @NonNull
    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment = BirthdaysPagerFragment.newInstance(position, dayFilter)
        registeredFragments.put(position, fragment)
        return fragment
    }


    override fun getItemCount(): Int {
        return itemIds.size
    }

    override fun getItemId(position: Int): Long {
        return itemIds[position]
    }

    override fun containsItem(itemId: Long): Boolean {
        return itemIds.contains(itemId)
    }

    private fun generateListOfItemIds() = (1..2).map { counter++ }

    fun getRegisteredFragment(position: Int): Fragment{
        return registeredFragments.get(position);
    }
}