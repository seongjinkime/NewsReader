package com.rss.newsreader.adapters

import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.mrt.nasca.NascaViewListener
import com.rss.newsreader.R
import com.rss.newsreader.common.DEFAULT_THUMB

import com.rss.newsreader.datas.News
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.news_list_item.*

/*
 * Created by seongjinkim on 2020-03-28
 * Use Nasca View for load image
 * https://github.com/myrealtrip/nasca
 */
class NewsAdapter(val selectEvent: (News) -> Unit): RecyclerView.Adapter<NewsAdapter.NewsHolder>() {

    var items: ArrayList<News> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val newsView = LayoutInflater.from(parent.context).inflate(
            R.layout.news_list_item,
            parent,
            false
        )
        return NewsHolder(newsView)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        holder.bind(items[position])
        var lastPosition = -1

        if (position > lastPosition) {
            var animation = AnimationUtils.loadAnimation(holder.containerView.context, android.R.anim.fade_in)
            holder.itemView.startAnimation(animation)
        }
    }

    inner class NewsHolder(override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(news: News) {
            containerView.findViewById<TextView>(R.id.tv_title)
            tv_title.text = news.title
            tv_description.text = news.description
            bindKeywords(news.keywords)
            bindThumbnail(news.thumb)
            itemView.setOnClickListener { selectEvent(news) }
        }

        //https://github.com/myrealtrip/nasca
        private fun bindThumbnail(thumbUrl: String) {
            val thumb = if (thumbUrl != "") thumbUrl else DEFAULT_THUMB
            nv_thumb.loadImages(thumb)
            nv_thumb.listener = object: NascaViewListener() {
                override fun onLoadingFailed(url: String, errorCode: Int, errorMsg: CharSequence?) {
                    super.onLoadingFailed(url, errorCode, errorMsg)
                    nv_thumb.loadImages(DEFAULT_THUMB)
                }
            }
        }

        private fun bindKeywords(keywords: List<String>) {
            val keywordViews = listOf<TextView>(tv_keyword_first, tv_keyword_second, tv_keyword_third)
            keywordViews.forEach { it.visibility = View.GONE }
            for (idx in 0..keywords.size - 1) {
                keywordViews.get(idx).text = keywords.get(idx)
                keywordViews.get(idx).visibility = View.VISIBLE
            }
        }

    }
}