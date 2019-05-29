package com.rozedfrozzy.topheadline.data.response

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)