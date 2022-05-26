package com.conamobile.walpapers.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import com.conamobile.walpapers.R
import com.conamobile.walpapers.databinding.FragmentSelectThemeBinding

class SelectThemeFragment : BaseFragment() {
    private var _binding: FragmentSelectThemeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_select_theme, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSelectThemeBinding.bind(view)
        installSwitch()
        switchManager()
        nextButtonManager()
    }

    private fun nextButtonManager() {
        binding.apply {
            themeNextButton.setOnClickListener {
                themeNextTextView.isVisible = false
                themeLottieAnim.isVisible = true

                if (switchLightOrNight.isOn) mySharedPreferences.isRealThemeManager(false)
                else mySharedPreferences.isRealThemeManager(true)

                Handler(Looper.getMainLooper()).postDelayed({
                    startLoginFragment()
                }, 10)
            }

        }
    }

    private fun installSwitch() {
        binding.apply {
            switchLightOrNight.isOn = mySharedPreferences.getSwitchSaverBoolean()
        }
        binding.apply {
            when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    switchLightOrNight.isOn = false
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    switchLightOrNight.isOn = true
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    switchLightOrNight.isOn = true
                }
            }
        }

    }

    private fun switchManager() {
        binding.apply {
            switchLightOrNight.setOnToggledListener { _, isOn ->
                if (isOn) {
                    //Light Mode
                    switchLightOrNight.isOn = true
                    mySharedPreferences.isSwitchSaverBoolean(true)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                } else {
                    //Night Mode
                    switchLightOrNight.isOn = false
                    mySharedPreferences.isSwitchSaverBoolean(false)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}