package com.conamobile.walpapers.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.conamobile.walpapers.R
import com.conamobile.walpapers.databinding.FragmentLanguageBinding
import java.util.*

class LanguageFragment : BaseFragment() {
    private var _binding: FragmentLanguageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLanguageBinding.bind(view)
        welcomeTextManager()
        selectLangTextManager()
        langCardsManager()
    }

    private fun langCardsManager() {
        binding.apply {

            englishLangBtn.setOnClickListener {
                setLocale("en")
                mySharedPreferences.isSavedAppLang("en")
                findNavController().navigate(R.id.action_languageFragment_to_selectThemeFragment)
            }
            russianLangBtn.setOnClickListener {
                setLocale("ru")
                mySharedPreferences.isSavedAppLang("ru")
                findNavController().navigate(R.id.action_languageFragment_to_selectThemeFragment)
            }
            uzbekLangBtn.setOnClickListener {
                setLocale("uz")
                mySharedPreferences.isSavedAppLang("uz")
                findNavController().navigate(R.id.action_languageFragment_to_selectThemeFragment)
            }

        }
    }

    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context?.resources?.updateConfiguration(
            config,
            requireContext().resources.displayMetrics
        )
    }

    private fun selectLangTextManager() {
        binding.apply {
            selectLangTextView.apply {
                setCharacterDelay(100)
                avoidTextOverflowAtEdge(false)
                animateText(getString(R.string.select_lang))
                setOnClickListener {
                    if (isAnimationRunning) {
                        stopAnimation()
                        animateText(getString(R.string.select_lang))
                    } else animateText(getString(R.string.select_lang))
                }
            }
        }
    }

    private fun welcomeTextManager() {
        binding.apply {
            loginWritableTextView.apply {
                setCharacterDelay(100)
                animateText(getString(R.string.welcome))
                setOnClickListener {
                    if (isAnimationRunning) {
                        stopAnimation()
                        animateText(getString(R.string.welcome))
                    } else animateText(getString(R.string.welcome))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
