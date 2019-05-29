package com.rozedfrozzy.topheadline.ui.search

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rozedfrozzy.topheadline.data.network.NetworkService
import com.rozedfrozzy.topheadline.data.network.SchedulerWrappers
import com.rozedfrozzy.topheadline.data.response.Article
import com.rozedfrozzy.topheadline.data.response.NewsResponse
import io.reactivex.observers.DisposableSingleObserver
import retrofit2.HttpException

class SearchViewModel(val schedulerWrappers: SchedulerWrappers) : ViewModel() {


}