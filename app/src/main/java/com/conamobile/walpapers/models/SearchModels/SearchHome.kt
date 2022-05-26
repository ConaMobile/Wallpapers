package com.conamobile.walpapers.models.SearchModels

data class SearchHome(
    val total: Long,
    val totalPages: Long,
    val results: List<SearchResult>? = null
)
