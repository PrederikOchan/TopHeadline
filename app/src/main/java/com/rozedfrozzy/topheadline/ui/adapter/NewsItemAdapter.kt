package com.rozedfrozzy.topheadline.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rozedfrozzy.topheadline.R
import com.rozedfrozzy.topheadline.data.response.Article
import com.rozedfrozzy.topheadline.utils.dateFormatter
import kotlinx.android.synthetic.main.item_news.view.*
import org.jetbrains.anko.toast

class NewsItemAdapter(val context: Context) : RecyclerView.Adapter<NewsItemAdapter.ViewHolder>() {

    private var newsList: ArrayList<Article> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =  LayoutInflater.from(context).inflate(R.layout.item_news, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = newsList[position]
        holder.setModel(model)
    }

    fun updateList(list: List<Article>) {
        newsList.addAll(list)
        if (newsList.isEmpty()) context.toast("Not Found")
        notifyDataSetChanged()
    }

    fun clearData() {
        newsList.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setModel(news: Article) {
            news?.title?.let { itemView.newsTitle.text = news.title }
            news?.publishedAt?.let { itemView.newsDate.text = dateFormatter(news.publishedAt) }
            news?.description?.let { itemView.newsDescription.text = news.description }
            news?.urlToImage?.let {
                Glide.with(context)
                    .load(news.urlToImage)
                    .into(itemView.newsImage)
            }
        }
    }
}