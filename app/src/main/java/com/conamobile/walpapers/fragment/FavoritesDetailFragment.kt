package com.conamobile.walpapers.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.conamobile.walpapers.R
import com.conamobile.walpapers.databinding.FragmentFavoritesDetailBinding
import com.conamobile.walpapers.room.DatabaseClass
import com.conamobile.walpapers.util.TransferFavoriteImage
import com.conamobile.walpapers.util.TransferImage
import pub.devrel.easypermissions.EasyPermissions

class FavoritesDetailFragment : BaseFragment(), EasyPermissions.PermissionCallbacks {
    private var _binding: FragmentFavoritesDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var glideLoad: Runnable
    private lateinit var glideLoadFailedRunnable: Runnable
    private lateinit var glideLoadSuccessRunnable: Runnable
    private var startClickable = true
    private var imageGallerySaved = false
    private val storagePermissionCode = 23

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoritesDetailBinding.bind(view)
        loadImageBitmap()
        loadFullImage()
        editFullScreen()
        backBtnManager()
        retryLoadHdImagesBtnClickManager()
        installWallpaperCardClickManager()
        saveGalleryCardClickManager()
        saveFavoritesCardClickManager()
        changeScaleTypeCardViewClickManager()
        setupStart()
    }

    private fun setupStart() {
        binding.saveFavoritesLottieStart.apply {
            speed = 1f
            playAnimation()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun changeScaleTypeCardViewClickManager() {
        binding.apply {
            detailImageView.scaleType = ImageView.ScaleType.CENTER_CROP
            changeScaleTypeCardView.setOnClickListener {
                when (detailImageView.scaleType) {
                    ImageView.ScaleType.CENTER_CROP -> {
                        detailImageView.scaleType = ImageView.ScaleType.FIT_CENTER
                        changeScaleTypeCardView.background.setTint(ContextCompat.getColor(
                            requireContext(),
                            R.color.black))
                        changeScaleTypeImageView.setImageDrawable(resources.getDrawable(R.drawable.ic_scale_xy,
                            requireContext().theme))
                    }
                    ImageView.ScaleType.FIT_CENTER -> {
                        detailImageView.scaleType = ImageView.ScaleType.FIT_XY
                        changeScaleTypeImageView.setImageDrawable(resources.getDrawable(R.drawable.ic_scale_fit_start,
                            requireContext().theme))
                        changeScaleTypeCardView.background.setTint(ContextCompat.getColor(
                            requireContext(),
                            R.color.back_color_25))
                    }
                    ImageView.ScaleType.FIT_XY -> {
                        detailImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                        changeScaleTypeImageView.setImageDrawable(resources.getDrawable(R.drawable.ic_match_scale,
                            requireContext().theme))
                        changeScaleTypeCardView.background.setTint(ContextCompat.getColor(
                            requireContext(),
                            R.color.back_color_25))
                    }
                    else -> {
                        detailImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                        changeScaleTypeImageView.setImageDrawable(resources.getDrawable(R.drawable.ic_match_scale,
                            requireContext().theme))
                        changeScaleTypeCardView.background.setTint(ContextCompat.getColor(
                            requireContext(),
                            R.color.back_color_25))
                    }
                }
            }
        }
    }

    private fun saveFavoritesCardClickManager() {
        binding.apply {
            saveFavoritesCard.setOnClickListener {
                saveFavoritesLottieStart.apply {
                    startClickable = if (!startClickable) {
                        speed = 1f
                        playAnimation()
                        saveFavoritesTextView.text = getString(R.string.remove_favorites)
                        Handler(Looper.getMainLooper()).postDelayed({
                            interstitialShow.run()
                        }, 3000)
                        true
                    } else {
                        speed = -1.5f
                        playAnimation()
                        saveFavoritesTextView.text = getString(R.string.add_favorites)
                        Handler(Looper.getMainLooper()).postDelayed({
                            interstitialShow.run()
                        }, 3000)
                        false
                    }
                }
            }
        }
    }

    private fun saveGalleryCardClickManager() {
        binding.saveGalleryCard.apply {
            setOnClickListener {
                if (imageGallerySaved) {
                    toast(getString(R.string.already_gallery))
                } else {
                    requestPermissions()
                }
            }
        }
    }

    private fun requestPermissions() {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.please_allow_storage_permission),
            storagePermissionCode,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        saveImage(binding.detailImageView.drawable.toBitmap(),
            "Play Store conamobile , id ${System.currentTimeMillis()}")
        binding.successLottieAnim.isVisible = true
        binding.successLottieAnim.playAnimation()
        Handler(Looper.getMainLooper()).postDelayed({
            try {
                gallerySavedEnd.run()
            } catch (e: NullPointerException) {
            }
        }, 3400)
        imageGallerySaved = true
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        toast(getString(R.string.please_allow_storage_permission))
    }


    private val gallerySavedEnd = kotlinx.coroutines.Runnable {
        binding.successLottieAnim.isVisible = false
        toast(getString(R.string.saved))
        interstitialShow.run()
    }

    private val wallpaperSavedEnd = kotlinx.coroutines.Runnable {
        binding.successLottieAnim.isVisible = false
        toast(getString(R.string.wallpaper_set))
        interstitialShow.run()
    }

    private fun installWallpaperCardClickManager() {
        binding.apply {
            installWallpapersCard.setOnClickListener {
                setWallpaper(detailImageView.drawable.toBitmap())
                binding.successLottieAnim.isVisible = true
                binding.successLottieAnim.playAnimation()
                Handler(Looper.getMainLooper()).postDelayed({
                    try {
                        wallpaperSavedEnd.run()
                    } catch (e: NullPointerException) {
                    }
                }, 3400)
            }
        }
    }

    private fun retryLoadHdImagesBtnClickManager() {
        binding.apply {
            loadingHDLayout.setOnClickListener {
                if (loadingHDTextView.text == getString(R.string.retry_load)) {
                    loadFullImage()
                }
            }
        }
    }

    private fun backBtnManager() {
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun editFullScreen() {
        hideNoLimitSystemBars()
    }

    private fun loadFullImage() {
        binding.apply {
            loadingHDLayout.isVisible = true
            loadingHDLottieAnim.isVisible = true
            loadingHDTextView.text = getString(R.string.loading_hd)
        }

        glideLoadFailedRunnable = kotlinx.coroutines.Runnable {
            binding.apply {
                loadingHDLottieAnim.isVisible = false
                loadingHDTextView.text = getString(R.string.retry_load)
            }
        }

        glideLoadSuccessRunnable = kotlinx.coroutines.Runnable {
            binding.apply {
                detailImageView.startAnimation(fastAlphaAnimation)
                installWallpapersCard.startAnimation(fastAlphaAnimation)
                saveGalleryCard.startAnimation(fastAlphaAnimation)
                saveFavoritesCard.startAnimation(fastAlphaAnimation)
                loadingHDLayout.isVisible = false
                installWallpapersCard.isVisible = true
                saveGalleryCard.isVisible = true
                saveFavoritesCard.isVisible = true
                detailImageView.scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }

        glideLoad = kotlinx.coroutines.Runnable {
            Glide.with(requireContext())
                .load(TransferFavoriteImage.bigLink)
                .timeout(10000)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(binding.detailImageView.drawable)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        glideLoadFailedRunnable.run()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        glideLoadSuccessRunnable.run()
                        return false
                    }
                })
                .into(binding.detailImageView)
        }
        glideLoad.run()
    }

    private fun loadImageBitmap() {
        binding.apply {
            if (TransferFavoriteImage.transferImage != null) {
                detailImageView.setImageBitmap(TransferFavoriteImage.transferImage)
                loadingLottieAnim.isVisible = false
            } else loadingLottieAnim.isVisible = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showNoLimitSystemBars()
        clearImageView()
        if (!startClickable) removeRoomFromId(TransferFavoriteImage.id!!)
        if (!binding.loadingHDLayout.isVisible)
            _binding = null
    }

    private fun removeRoomFromId(id: Int) {
        DatabaseClass.getDatabase(context)!!.getDao().deleteData(id)
    }

    private fun clearImageView() {
        binding.detailImageView.setImageResource(0)
        binding.detailImageView.setImageBitmap(null)
        TransferImage.transferImage = null
        TransferImage.imageLink = null
        imageGallerySaved = false
    }

}