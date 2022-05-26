package com.conamobile.walpapers.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.conamobile.walpapers.R
import com.conamobile.walpapers.adapter.RetrofitGetSearchAdapter
import com.conamobile.walpapers.databinding.FragmentSearchBinding
import com.conamobile.walpapers.models.SearchModels.SearchHome
import com.conamobile.walpapers.models.SearchModels.SearchResult
import com.conamobile.walpapers.networking.RetrofitHttp
import com.conamobile.walpapers.util.ApiKeyList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : BaseFragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    var photosList = ArrayList<SearchResult>()
    private var pageCount = 1
    private var perPageCount = 30
    private lateinit var retrofitGetSearchAdapter: RetrofitGetSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)
        binding.searchEditText.requestFocus()
        showKeyboard(binding.searchEditText)
        editTextManager()
        searchButtonClickManager()
        recyclerViewManager()
    }

    private fun searchButtonClickManager() {
        binding.apply {
            searchButton.setOnClickListener {
                if (searchButton.text == getString(R.string.clear)) {
                    searchEditText.text?.clear()
                    searchButton.text = getString(R.string.search_button)
                    showKeyboard(searchEditText)
                } else {
                    showDialog()
                    photosList.clear()
                    loadItemImagesAll(pageCount, perPageCount, searchEditText.text.toString())
                    searchButton.text = getString(R.string.clear)
                    hideKeyboard()
                }
            }
        }
    }

    private fun editTextManager() {
        binding.apply {
            searchEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    searchButton.text = getString(R.string.search_button)
                    if (searchEditText.text!!.isEmpty()) {
                        notFoundTextView.text = ""
                    }
                }
            })

            searchEditText.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    showDialog()
                    photosList.clear()
                    loadItemImagesAll(pageCount, perPageCount, searchEditText.text.toString())
                    searchButton.text = getString(R.string.clear)
                    return@OnEditorActionListener true
                }
                false
            })
        }
    }

    private fun refreshAllItemLoadAgain() {
        loadItemImagesAll(pageCount, perPageCount, binding.searchEditText.text.toString())
    }

    private fun refreshPageCountAdder() {
        loadItemImagesAll(pageCount++, perPageCount, binding.searchEditText.text.toString())
    }


    private fun loadItemImagesAll(page: Int, per_page: Int, searchName: String) {
        RetrofitHttp.posterService.searchPhotos(page, per_page, searchName)
            .enqueue(object : Callback<SearchHome> {
                override fun onResponse(
                    call: Call<SearchHome>,
                    response: Response<SearchHome>,
                ) {
                    hideDialog()
                    if (response.body() != null) {
                        photosList.addAll(response.body()!!.results!!)
                        adapterNotify()
                    } else {
                        if (ApiKeyList.countGetInt != 3) {
                            when (ApiKeyList.countGetInt) {
                                0 -> {
                                    ApiKeyList.countGetInt = 1
                                    refreshAllItemLoadAgain()
                                    adapterNotify()
                                }
                                1 -> {
                                    ApiKeyList.countGetInt = 2
                                    refreshAllItemLoadAgain()
                                    adapterNotify()
                                }
                                2 -> {
                                    ApiKeyList.countGetInt = 3
                                    refreshAllItemLoadAgain()
                                    adapterNotify()
                                }
                            }
                        } else {
                            toast(getString(R.string.server_not_response))
                            toast(getString(R.string.please_try_later))
                            adapterNotify()
                        }
                    }
                }

                override fun onFailure(call: Call<SearchHome>, t: Throwable) {
                    toast(getString(R.string.server_not_response))
                    toast(getString(R.string.please_try_later))
                    hideDialog()
                    adapterNotify()
                }
            })

    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun adapterNotify() {
        binding.apply {
            if (photosList.size > 0) {
                notFoundLottieAnim.isVisible = false
                notFoundTextView.text = ""
            } else {
                if (searchEditText.text!!.isNotEmpty()) {
                    notFoundLottieAnim.isVisible = true
                    notFoundTextView.text =
                        getString(R.string.no_result) + " '${searchEditText.text}'"
                    showKeyboard(searchEditText)
                }
            }
        }
        retrofitGetSearchAdapter.notifyDataSetChanged()
    }


    private fun recyclerViewManager() {
        retrofitGetSearchAdapter = RetrofitGetSearchAdapter(requireContext(), photosList)
        binding.searchRecyclerView.adapter = retrofitGetSearchAdapter

        binding.searchRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    viewDialog.showDialog()
                    refreshPageCountAdder()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard()
        _binding = null
    }
}