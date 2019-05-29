package com.rozedfrozzy.topheadline.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.rozedfrozzy.topheadline.R
import com.rozedfrozzy.topheadline.data.network.NetworkService
import com.rozedfrozzy.topheadline.data.network.SchedulerWrappers
import com.rozedfrozzy.topheadline.ui.adapter.NewsItemAdapter
import com.rozedfrozzy.topheadline.ui.search.SearchActivity
import com.rozedfrozzy.topheadline.utils.InfiniteScrollListener
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel
    private var adapter = NewsItemAdapter(this)

    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this, HomeViewModelFactory(SchedulerWrappers())).get(HomeViewModel::class.java)
        viewModel.networkService = NetworkService()

        getNewsData()

        configureRecyclerView()
        observableListener()

    }

    private fun getNewsData() {
        showProgressBar()
        if (isOnline()) {
            viewModel.getHeadline()
        }
        else {
            longToast("No Internet Connections")
            hideProgressBar()
            retryButton()
        }
    }

    private fun observableListener() {
        viewModel.getResultTopHeadlineListObservable().observe(this, Observer {
            adapter.updateList(it)
            hideProgressBar()
        })

        viewModel.getResultTopHeadlineListErrorObservable().observe(this, Observer {
            longToast("Something went wrong...")
            hideProgressBar()
        })
    }

    @SuppressLint("WrongConstant")
    private fun configureRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerViewTopNews.apply {
            recyclerViewTopNews.layoutManager = layoutManager
            addOnScrollListener(InfiniteScrollListener({ loadData() }, layoutManager))
        }
        recyclerViewTopNews.adapter = adapter
    }

    private fun loadData() {
        if (isOnline()) {
            currentPage++
            viewModel.getHeadline(currentPage)
            showProgressBar()
        } else {
            longToast("No Internet Connections")
        }
    }

    private fun showProgressBar() {
        homeProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        homeProgressBar.visibility = View.GONE
    }

    private fun retryButton() {
        homeRetryBtn.visibility = View.VISIBLE
        homeRetryBtn.setOnClickListener {
            if (isOnline()) {
                viewModel.getHeadline()
                showProgressBar()
                it.visibility = View.GONE
            } else {
                longToast("No Internet Connections")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.searchBtn -> {
            startActivity<SearchActivity>()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun isOnline(): Boolean {
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
