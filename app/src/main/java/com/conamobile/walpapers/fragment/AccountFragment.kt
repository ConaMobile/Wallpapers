package com.conamobile.walpapers.fragment

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.conamobile.walpapers.R
import com.conamobile.walpapers.databinding.FragmentAccountBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds

class AccountFragment : BaseFragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountBinding.bind(view)
        signOutClickManager()
        nameCardClickManager()
        emailCardClickManager()
        installSwitch()
        switchManager()
        themeSaverMemory()
        switchCardManager()
        otherAppManager()
        rateAppManager()
        contactCardManager()
        bannerAds()
    }

    private fun themeSaverMemory() {
        if (binding.switchLightOrNight.isOn) mySharedPreferences.isRealThemeManager(false)
        else mySharedPreferences.isRealThemeManager(true)
    }

    private fun otherAppManager() {
        binding.otherAppCardView.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/dev?id=4658021362103574300")
            )
            startActivity(browserIntent)
        }
    }

    private fun contactCardManager() {
        binding.contactCardView.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://t.me/cona_mobile")
            )
            startActivity(browserIntent)
        }
    }

    private fun rateAppManager() {
        binding.rateAppCardView.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=com.conamobile.walpapers")
            )
            startActivity(browserIntent)
        }
    }

    private fun switchCardManager() {
        binding.apply {
            nightCardView.setOnClickListener {
                if (switchLightOrNight.isOn) {
                    switchLightOrNight.isOn = false
                    mySharedPreferences.isSwitchSaverBoolean(false)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    switchLightOrNight.isOn = true
                    mySharedPreferences.isSwitchSaverBoolean(true)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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

    private fun installSwitch() {
        binding.apply {
            switchLightOrNight.isOn = mySharedPreferences.getSwitchSaverBoolean()
        }
        binding.apply {
            when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    switchLightOrNight.isOn = false
                    binding.nightModeTextView.text = getString(R.string.light_mode)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    switchLightOrNight.isOn = true
                    binding.nightModeTextView.text = getString(R.string.night_mode)
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    switchLightOrNight.isOn = true
                    binding.nightModeTextView.text = getString(R.string.night_mode)
                }
            }
        }

    }

    private fun nameCardClickManager() {
        binding.apply {
            nameText.isSelected = true
            if (mySharedPreferences.getSavedUserName() != "")
                nameText.text = mySharedPreferences.getSavedUserName()
            else nameText.text = getString(R.string.empty)
            nameCardView.setOnClickListener {
                toast(nameText.text.toString())
            }
        }
    }

    private fun emailCardClickManager() {
        binding.apply {
            emailText.isSelected = true
            if (mySharedPreferences.getSavedUserEmail() != "")
                emailText.text = mySharedPreferences.getSavedUserEmail()
            else emailText.text = getString(R.string.empty)
            emailCardView.setOnClickListener {
                toast(emailText.text.toString())
            }
        }
    }

    private fun signOutClickManager() {
        binding.signOut.setOnClickListener {
            showAlertDialog()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bannerAds() {
        MobileAds.initialize(requireContext()) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

}