package com.conamobile.walpapers.fragment

import android.app.AlertDialog
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.conamobile.walpapers.R
import com.conamobile.walpapers.activity.MainActivity
import com.conamobile.walpapers.activity.SplashActivity
import com.conamobile.walpapers.dialog.ViewDialog
import com.conamobile.walpapers.memory.MySharedPreferences
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


open class BaseFragment : Fragment() {
    lateinit var mySharedPreferences: MySharedPreferences
    lateinit var viewDialog: ViewDialog
    lateinit var animation: Animation
    lateinit var fastAlphaAnimation: Animation
    private var mInterstitialAd: InterstitialAd? = null
    var mAuth: FirebaseAuth? = null
    lateinit var mGoogleSignInClient: GoogleSignInClient
    var firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mySharedPreferences = MySharedPreferences(requireContext())
        viewDialog = ViewDialog(requireContext())
        viewDialog.loadDialog()
        installAlphaAnimation()
        installFirebase()
        loadInterstitialAd()
    }

    fun installFirebase() {
        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    fun startSearchFragment() {
        findNavController().navigate(R.id.searchFragment)
    }

    fun showDialog() {
        viewDialog.showDialog()
    }

    fun hideDialog() {
        viewDialog.hideDialog()
    }

    private fun installAlphaAnimation() {
        animation = AnimationUtils.loadAnimation(context, R.anim.alpha_anim)
        fastAlphaAnimation = AnimationUtils.loadAnimation(context, R.anim.alpha_anim_fast)
    }

    fun startMainActivity() {
        mySharedPreferences.isLoginPageBoolean(true)
        startActivity(Intent(context, MainActivity::class.java))
        requireActivity().finish()
    }

    fun toast(message: String) {
        if (context != null)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun startLoginFragment() {
        findNavController().navigate(R.id.action_selectThemeFragment_to_loginFragment)
    }

    fun startDetailFragment() {
        findNavController().navigate(R.id.detailFragment)
    }

    fun hideNoLimitSystemBars() {
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    fun showNoLimitSystemBars() {
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    fun saveImage(bitmap: Bitmap, title: String): Uri {
        val savedImageURL = MediaStore.Images.Media.insertImage(
            requireActivity().contentResolver,
            bitmap,
            title,
            "Image of $title"
        )
        return Uri.parse(savedImageURL)
    }

    fun setWallpaper(bitmap: Bitmap) {
        val wallpaperManager = WallpaperManager.getInstance(context)
        wallpaperManager.setBitmap(bitmap)
    }

    fun showKeyboard(editText: EditText) {
        editText.requestFocus()
        val content =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        content.showSoftInput(editText, 0)
        content.toggleSoftInput(InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun firebaseSignOut() {
        mySharedPreferences.isLoginPageBoolean(false)
        FirebaseAuth.getInstance().signOut()
        mGoogleSignInClient.signOut()
        restart()
    }

    open fun restart() {
        val intent = Intent(context, SplashActivity::class.java)
        this.startActivity(intent)
        requireActivity().finishAffinity()
    }

    fun showAlertDialog() {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)

        val view = LayoutInflater.from(context)
            .inflate(R.layout.customview_dialog, requireView().findViewById(R.id.layoutDialog))
        builder.setView(view)
        view.findViewById<TextView>(R.id.textTitle).text = getString(R.string.exit)
        view.findViewById<TextView>(R.id.textMessage).text = getString(R.string.message)
        view.findViewById<Button>(R.id.buttonCancelAction).text = getString(R.string.cancel)
        view.findViewById<Button>(R.id.buttonExitAction).text = getString(R.string.yes)

        val alertDialog: AlertDialog = builder.create()

        view.findViewById<Button>(R.id.buttonCancelAction).setOnClickListener {
            alertDialog.dismiss()
        }

        view.findViewById<Button>(R.id.buttonExitAction).setOnClickListener {
            alertDialog.dismiss()
            firebaseSignOut()
        }

        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }

        alertDialog.setCancelable(true)
        alertDialog.show()

    }


    fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(),
            resources.getString(R.string.interstitial_ads),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(p0: InterstitialAd) {
                    super.onAdLoaded(p0)
                    mInterstitialAd = p0
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    mInterstitialAd = null
                }
            })
    }

//    fun showInterstitialAd() {
//
//    }

    val interstitialShow = Runnable {
        if (mInterstitialAd != null) {
            mInterstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    super.onAdFailedToShowFullScreenContent(p0)
                    mInterstitialAd = null
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    mInterstitialAd = null
                    loadInterstitialAd()
                }

            }
            if (isAdded)
                mInterstitialAd!!.show(requireActivity())
        }
    }
}