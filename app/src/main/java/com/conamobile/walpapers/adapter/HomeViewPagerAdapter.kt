package com.conamobile.walpapers.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.conamobile.walpapers.fragment.*

class HomeViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeAllFragment()
            1 -> HomeBlackFragment()
            2 -> Home4KFragment()
            3 -> HomeNatureFragment()
            4 -> HomeGamingFragment()
            5 -> HomeCarsFragment()
            6 -> HomeMoonFragment()
            else -> Fragment()
        }
    }
}