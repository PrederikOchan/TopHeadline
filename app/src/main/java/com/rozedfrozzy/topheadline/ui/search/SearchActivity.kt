package com.rozedfrozzy.topheadline.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.rozedfrozzy.topheadline.R
import com.rozedfrozzy.topheadline.data.network.NetworkService
import com.rozedfrozzy.topheadline.data.network.SchedulerWrappers
import com.rozedfrozzy.topheadline.ui.adapter.NewsItemAdapter
import com.rozedfrozzy.topheadline.utils.InfiniteScrollListener
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.longToast

class SearchActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchViewModel
    private var adapter = NewsItemAdapter(this)

    private var currentPage = 1
    private var searchKey = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(appBarSearch)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProviders.of(this, SearchViewModelFactory(SchedulerWrappers())).get(SearchViewModel::class.java)
        viewModel.networkService = NetworkService()

        setupSearchView()
        configureRecyclerView()
        observableListener()
    }

    private fun observableListener() {
        viewModel.getResultListObservable().observe(this, Observer {
            adapter.updateList(it)
            hideProgressBar()
        })

        viewModel.getResultListErrorObservable().observe(this, Observer {
            longToast(getString(R.string.something_wrong))
            hideProgressBar()
        })
    }

    private fun setupSearchView() {
        searchBar.requestFocus()
        searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                searchBar.clearFocus()
                return if (isOnline()) {
                    showProgressBar()
                    adapter.clearData()
                    searchKey = query
                    currentPage = 1
                    viewModel.getQueryNews(searchKey)
                    true
                } else {
                    longToast(getString(R.string.no_internet_connection))
                    false
                }
            }
        })
    }

    @SuppressLint("WrongConstant")
    private fun configureRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerViewSearchResults.apply {
            recyclerViewSearchResults.layoutManager = layoutManager
            addOnScrollListener(InfiniteScrollListener({ loadData() }, layoutManager))
        }
        recyclerViewSearchResults.adapter = adapter
    }

    fun loadData() {
        currentPage++
        viewModel.getQueryNews(searchKey, currentPage)
        showProgressBar()
    }

    private fun showProgressBar() {
        searchProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        searchProgressBar.visibility = View.GONE
    }

    private fun isOnline(): Boolean {
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
