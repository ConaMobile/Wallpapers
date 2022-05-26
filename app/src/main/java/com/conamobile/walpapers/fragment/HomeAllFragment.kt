package com.conamobile.walpapers.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.RecyclerView
import com.conamobile.walpapers.R
import com.conamobile.walpapers.adapter.RetrofitGetAdapter
import com.conamobile.walpapers.databinding.FragmentHomeAllBinding
import com.conamobile.walpapers.models.RandomAllModels.RandomAllModels
import com.conamobile.walpapers.networking.RetrofitHttp
import com.conamobile.walpapers.util.ApiKeyList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeAllFragment : BaseFragment() {
    private var _binding: FragmentHomeAllBinding? = null
    private val binding get() = _binding!!
    var photosList = ArrayList<RandomAllModels>()
    private lateinit var retrofitGetAdapter: RetrofitGetAdapter
    private var loadCount = 30

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_home_all, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeAllBinding.bind(view)
        recyclerViewManager()
        loadItemImagesAll(loadCount)
        recyclerItemClickManager()
        searchCardClickManager()
    }

    private fun searchCardClickManager() {
        binding.searchCardView.setOnClickListener {
            startSearchFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        motionManager()
    }

    private fun motionManager() {
        binding.apply {
            val transitionListener = object : MotionLayout.TransitionListener {
                override fun onTransitionStarted(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int,
                ) {
                    binding.searchCardView.startAnimation(animation)
                }

                override fun onTransitionChange(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int,
                    progress: Float,
                ) {
                }

                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {}

                override fun onTransitionTrigger(
                    motionLayout: MotionLayout?,
                    triggerId: Int,
                    positive: Boolean,
                    progress: Float,
                ) {
                }
            }
            motionLayout.setTransitionListener(transitionListener)
        }
    }

    private fun refreshAllItemLoadAgain() {
        loadItemImagesAll(loadCount)
    }

    private fun recyclerItemClickManager() {
        retrofitGetAdapter.itemCLick = {
            startDetailFragment()
        }
    }

    private fun loadItemImagesAll(loadCount: Int) {
        RetrofitHttp.posterService.getAllRandomPhotos(loadCount)
            .enqueue(object : Callback<ArrayList<RandomAllModels>> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ArrayList<RandomAllModels>>,
                    response: Response<ArrayList<RandomAllModels>>,
                ) {
                    hideDialog()
                    if (response.body() != null) {
                        photosList.addAll(response.body()!!)
                        adapterNotify()
                    } else {
                        if (ApiKeyList.countGetInt != 3) {
                            when (ApiKeyList.countGetInt) {
                                0 -> {
                                    ApiKeyList.countGetInt = 1
                                    refreshAllItemLoadAgain()
                                }
                                1 -> {
                                    ApiKeyList.countGetInt = 2
                                    refreshAllItemLoadAgain()
                                }
                                2 -> {
                                    ApiKeyList.countGetInt = 3
                                    refreshAllItemLoadAgain()
                                }
                            }
                        } else {
                            connectFirebaseServer()
                        }
                    }
                }

                override fun onFailure(call: Call<ArrayList<RandomAllModels>>, t: Throwable) {
                    toast(getString(R.string.server_not_response))
                    hideDialog()
                    connectFirebaseServer()
                }
            })
    }

    private fun connectFirebaseServer() {
        toast("All Server Not Response Please try later!")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun adapterNotify() {
        retrofitGetAdapter.notifyDataSetChanged()
    }

    private fun recyclerViewManager() {
        retrofitGetAdapter = RetrofitGetAdapter(requireContext(), photosList)
        binding.homeAllRecyclerView.adapter = retrofitGetAdapter

        binding.homeAllRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    viewDialog.showDialog()
                    refreshAllItemLoadAgain()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}