package com.rozedfrozzy.topheadline.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rozedfrozzy.topheadline.data.network.SchedulerWrappers

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(val schedulerWrappers: SchedulerWrappers) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            SearchViewModel(schedulerWrappers) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}