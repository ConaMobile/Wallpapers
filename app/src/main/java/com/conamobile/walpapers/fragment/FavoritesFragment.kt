package com.conamobile.walpapers.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.conamobile.walpapers.R
import com.conamobile.walpapers.adapter.RetrofitGetFavoritesAdapter
import com.conamobile.walpapers.databinding.FragmentFavoritesBinding
import com.conamobile.walpapers.models.RoomModel.UserModel
import com.conamobile.walpapers.room.DatabaseClass
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds

class FavoritesFragment : BaseFragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    var photosList: ArrayList<UserModel>? = null
    lateinit var retrofitGetFavoritesAdapter: RetrofitGetFavoritesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoritesBinding.bind(view)
        bannerAds()
    }

    private fun refreshAll() {
        loadItemImagesAll()
        setupRecycler()
        itemClickManager()
        swipeRefreshManager()
        totalTextManager()
        notFoundLottieManager()
    }

    private fun bannerAds() {
        MobileAds.initialize(requireContext()) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun notFoundLottieManager() {
        binding.notFoundLottieAnim.isVisible = photosList?.size == 0
    }

    @SuppressLint("SetTextI18n")
    private fun totalTextManager() {
        binding.totalTextView.text =
            "${getString(R.string.totalText)} ${photosList?.size.toString()}"
    }

    override fun onResume() {
        super.onResume()
        refreshAll()
    }

    private fun swipeRefreshManager() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshAll()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun itemClickManager() {
        retrofitGetFavoritesAdapter.itemCLick = {
            findNavController().navigate(R.id.favoritesDetailFragment)
        }
    }

    private fun loadItemImagesAll() {
        photosList =
            DatabaseClass.getDatabase(context)?.getDao()?.getAllData() as ArrayList<UserModel>
    }

    private fun setupRecycler() {
        photosList?.reverse()
        retrofitGetFavoritesAdapter = RetrofitGetFavoritesAdapter(requireContext(), photosList!!)
        binding.favoritesRecyclerView.adapter = retrofitGetFavoritesAdapter
        adapterNotify()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun adapterNotify() {
        retrofitGetFavoritesAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}