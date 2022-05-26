package com.conamobile.walpapers.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.conamobile.walpapers.R
import com.conamobile.walpapers.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider


class LoginFragment : BaseFragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val requestCode: Int = 123

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)
        welcomeTextManager()
        editTextStartIconManager()
        loginNextButtonManager()
        loginContinueWithGoogleManager()
        installFirebase()
        signUpButtonClickManager()
    }

    private fun signUpButtonClickManager() {
        binding.signUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }

    //google-sign-in
    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, requestCode)
    }

    //google-sign-in
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCode) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    //google-sign-in
    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                updateUI(account)
            }
        } catch (e: ApiException) {
        }
    }

    //google-sign-in
    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mySharedPreferences.isSavedUserEmail(account.email.toString())
                mySharedPreferences.isSavedUserName(account.displayName.toString())
                binding.apply {
                    loginNextTextView.isVisible = false
                    loginLottieAnim.isVisible = true

                    Handler(Looper.getMainLooper()).postDelayed({
                        startMainActivity()
                    }, 800)
                }
            }
        }
    }

    private fun welcomeTextManager() {
        binding.apply {
            loginWritableTextView.apply {
                setCharacterDelay(100)
                animateText(getString(R.string.login))
                setOnClickListener {
                    if (isAnimationRunning) {
                        stopAnimation()
                        animateText(getString(R.string.login))
                    } else animateText(getString(R.string.login))
                }
            }
        }
    }

    //google-sign-in
    private fun loginContinueWithGoogleManager() {
        binding.loginContinueWithGoogle.setOnClickListener {
            signInGoogle()
        }
    }

    private fun loginNextButtonManager() {
        binding.apply {
            loginNextButton.setOnClickListener {
                emailSignIn()
            }
        }
    }

    //sign-in-email
    private fun emailSignIn() {
        binding.apply {
            if (notEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(loginEditTextEmail.text.toString(),
                    loginEditTextPassword.text.toString())
                    .addOnCompleteListener { signIn ->
                        if (signIn.isSuccessful) {
                            mySharedPreferences.isSavedUserName("")
                            mySharedPreferences.isSavedUserEmail(loginEditTextEmail.text.toString())
                            startMainActivity()
                        }
                    }
                    .addOnFailureListener { fail ->
                        toast(fail.message.toString())
                    }
            } else {
                loginEditTextEmail.error = getString(R.string.empty)
                loginEditTextPassword.error = getString(R.string.empty)
            }
        }
    }

    //sign-in-email
    private fun notEmpty(): Boolean =
        binding.loginEditTextEmail.text.toString().trim().isNotEmpty() &&
                binding.loginEditTextPassword.text.toString().trim().isNotEmpty()

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}