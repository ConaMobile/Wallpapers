package com.conamobile.walpapers.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.conamobile.walpapers.R
import com.conamobile.walpapers.adapter.HomeViewPagerAdapter
import com.conamobile.walpapers.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout

class HomeFragment : BaseFragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var homeAdapter: HomeViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        bottomBarManager()
    }

    private fun bottomBarManager() {
        binding.apply {
            homeAdapter = HomeViewPagerAdapter(childFragmentManager, lifecycle)
            viewPagerHome.adapter = homeAdapter

            tabLayoutHome.addTab(tabLayoutHome.newTab().setText("All"))
            tabLayoutHome.addTab(tabLayoutHome.newTab().setText("Dark"))
            tabLayoutHome.addTab(tabLayoutHome.newTab().setText("4K"))
            tabLayoutHome.addTab(tabLayoutHome.newTab().setText("Nature"))
            tabLayoutHome.addTab(tabLayoutHome.newTab().setText("Gaming"))
            tabLayoutHome.addTab(tabLayoutHome.newTab().setText("Cars"))
            tabLayoutHome.addTab(tabLayoutHome.newTab().setText("Moon"))

            tabLayoutHome.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    viewPagerHome.currentItem = tab!!.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })

            viewPagerHome.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    tabLayoutHome.selectTab(tabLayoutHome.getTabAt(position))
                }
            })

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}