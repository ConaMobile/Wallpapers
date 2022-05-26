package com.conamobile.walpapers.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.conamobile.walpapers.R
import com.conamobile.walpapers.databinding.FragmentSignUpBinding

class SignUpFragment : BaseFragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignUpBinding.bind(view)
        welcomeTextManager()
        editTextStartIconManager()
        loginNextButtonManager()
        installFirebase()
        signInButtonClickManager()
    }

    private fun signInButtonClickManager() {
        binding.loginButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    //sign-up-email
    private fun emailSignUp() {
        binding.apply {
            firebaseAuth.createUserWithEmailAndPassword(loginEditTextEmail.text.toString(),
                loginEditTextPassword.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        mySharedPreferences.isSavedUserName("")
                        mySharedPreferences.isSavedUserEmail(loginEditTextEmail.text.toString())
                        startMainActivity()
                    }
                }
                .addOnFailureListener { fail ->
                    toast(fail.message.toString())
                }
        }
    }

    private fun editTextStartIconManager() {
        binding.apply {
            loginEditTextEmail.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    filledLoginTextField1.boxStrokeColor =
                        ContextCompat.getColor(requireContext(),
                            R.color.text_input_icon_tint_focus)
                    filledLoginTextField1.setStartIconTintList(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.text_input_icon_tint_focus
                            )
                        )
                    )
                } else {
                    filledLoginTextField1.setStartIconTintList(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.text_input_icon_tint_not_focus
                            )
                        )
                    )
                }
            }
            loginEditTextPassword.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    filledLoginTextField2.boxStrokeColor =
                        ContextCompat.getColor(requireContext(),
                            R.color.text_input_icon_tint_focus)

                    filledLoginTextField2.setStartIconTintList(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.text_input_icon_tint_focus
                            )
                        )
                    )
                } else {

                    filledLoginTextField2.setStartIconTintList(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.text_input_icon_tint_not_focus
                            )
                        )
                    )
                }
            }
        }
    }

    private fun loginNextButtonManager() {
        binding.apply {
            loginNextButton.setOnClickListener {
                if (notEmpty()) {
                    emailSignUp()
                } else {
                    loginEditTextEmail.error = getString(R.string.empty)
                    loginEditTextPassword.error = getString(R.string.empty)
                }
            }
        }
    }

    private fun welcomeTextManager() {
        binding.apply {
            loginWritableTextView.apply {
                setCharacterDelay(100)
                animateText(getString(R.string.sign_up))
                setOnClickListener {
                    if (isAnimationRunning) {
                        stopAnimation()
                        animateText(getString(R.string.sign_up))
                    } else animateText(getString(R.string.sign_up))
                }
            }
        }
    }

    private fun notEmpty(): Boolean =
        binding.loginEditTextEmail.text.toString().trim().isNotEmpty() &&
                binding.loginEditTextPassword.text.toString().trim().isNotEmpty()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}