package com.conamobile.walpapers.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.conamobile.walpapers.R
import com.conamobile.walpapers.adapter.ViewPagerAdapter
import com.conamobile.walpapers.databinding.FragmentMainBinding

class MainFragment : BaseFragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)
        bottomBarManager()
    }

    private fun bottomBarManager() {
        binding.apply {
            viewPager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
            viewPager.currentItem = 0
            viewPager.isUserInputEnabled = false
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                @SuppressLint("NotifyDataSetChanged")
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    bottomBar.selectTabAt(position)
                    viewPager.isUserInputEnabled = position != 0
                }
            })

            bottomBar.selectTabAt(0)

            bottomBar.onTabSelected = {
                viewPager.currentItem = bottomBar.selectedIndex
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}