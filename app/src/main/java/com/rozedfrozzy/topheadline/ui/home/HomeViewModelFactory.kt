package com.rozedfrozzy.topheadline.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rozedfrozzy.topheadline.data.network.SchedulerWrappers

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(val schedulerWrappers: SchedulerWrappers) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(schedulerWrappers) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}