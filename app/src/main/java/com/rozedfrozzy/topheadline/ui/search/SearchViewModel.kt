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
    val resultListObservable = MutableLiveData<List<Article>>()
    val resultListErrorObservable = MutableLiveData<HttpException>()

    fun getResultListObservable(): LiveData<List<Article>> = resultListObservable
    fun getResultListErrorObservable(): LiveData<HttpException> = resultListErrorObservable

    lateinit var networkService: NetworkService

    @SuppressLint("CheckResult")
    fun getQueryNews(query: String, page: Int = 1) {
        networkService.fetchNews(query, page)!!
            .subscribeOn(schedulerWrappers.io())
            .observeOn(schedulerWrappers.main())
            .subscribeWith(object: DisposableSingleObserver<NewsResponse?>() {
                override fun onSuccess(t: NewsResponse) {
                    resultListObservable.postValue(fetchItemFromResult(t))
                }

                override fun onError(e: Throwable) {
                    resultListErrorObservable.postValue(e as HttpException)
                }

            })
    }

    private fun fetchItemFromResult(it: NewsResponse): ArrayList<Article> {
        val list = ArrayList<Article>()
        list.addAll(it.articles)
        return list
    }
}