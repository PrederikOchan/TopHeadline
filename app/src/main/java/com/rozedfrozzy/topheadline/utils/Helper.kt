package com.rozedfrozzy.topheadline.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
fun dateFormatter(date: String): String {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val formatter = SimpleDateFormat("dd MMMM yyyy")
    return formatter.format(parser.parse(date))
}