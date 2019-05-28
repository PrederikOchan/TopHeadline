package com.rozedfrozzy.topheadline.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rozedfrozzy.topheadline.R
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(appBarSearch)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupSearchView()
    }

    private fun setupSearchView() {
        searchBar.requestFocus()
    }
}
