package com.rozedfrozzy.topheadline.ui.home

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

class HomeViewModel(val schedulerWrappers: SchedulerWrappers) : ViewModel() {
    val resultTopHeadlineListObservable = MutableLiveData<List<Article>>()
    val resultTopHeadlineListErrorObservable = MutableLiveData<HttpException>()

    fun getResultTopHeadlineListObservable(): LiveData<List<Article>> = resultTopHeadlineListObservable
    fun getResultTopHeadlineListErrorObservable(): LiveData<HttpException> = resultTopHeadlineListErrorObservable

    lateinit var networkService: NetworkService

    @SuppressLint("CheckResult")
    fun getHeadline(page: Int = 1) {
        networkService.fetchTopHeadline(page)!!
            .subscribeOn(schedulerWrappers.io())
            .observeOn(schedulerWrappers.main())
            .subscribeWith(object: DisposableSingleObserver<NewsResponse?>() {
                override fun onSuccess(t: NewsResponse) {
                    resultTopHeadlineListObservable.postValue(fetchItemFromResult(t))
                }

                override fun onError(e: Throwable) {
                    resultTopHeadlineListErrorObservable.postValue(e as HttpException)
                }

            })
    }

    private fun fetchItemFromResult(it: NewsResponse): ArrayList<Article> {
        val list = ArrayList<Article>()
        list.addAll(it.articles)
        return list
    }
}