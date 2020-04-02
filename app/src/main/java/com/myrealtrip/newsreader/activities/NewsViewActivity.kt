package com.myrealtrip.newsreader.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import com.myrealtrip.newsreader.R
import com.myrealtrip.newsreader.common.NEWS_KEY
import com.myrealtrip.newsreader.datas.News
import kotlinx.android.synthetic.main.activity_news_view.*
import kotlinx.android.synthetic.main.news_list_item.*
import java.io.File
import java.lang.Exception

/*
 * Created by seongjinkim on 2020-03-28
 */
class NewsViewActivity: AppCompatActivity() {
    private lateinit var news: News

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_view)
        news = intent.getParcelableExtra(NEWS_KEY)
        setInfoUi()
        loadNews()
    }

    /*
     * Set information ui for news title, keywords
     */
    private fun setInfoUi() {
        news_view_tv_title.text = news.title
        var keywordViews = listOf<TextView>(news_view_keyword_first,
                                            news_view_keyword_second,
                                            news_view_keyword_third)
        keywordViews.forEach { it.visibility = View.GONE }
        for (idx in 0..news.keywords.size - 1) {
            keywordViews.get(idx).text = news.keywords.get(idx)
            keywordViews.get(idx).visibility = View.VISIBLE
        }
    }

    /*
     * Load news link in web view
     */
    private fun loadNews() {
        val link = news.link
        wv_news.settings.javaScriptEnabled = true
        wv_news.settings.loadWithOverviewMode = true
        wv_news.settings.useWideViewPort = true
        wv_news.settings.domStorageEnabled = true
        wv_news.webViewClient = WebViewClient()
        wv_news.loadUrl(link)
    }

    override fun onDestroy() {
        super.onDestroy()
        layout_news_view.removeAllViews()
        wv_news.clearCache(true)
        wv_news.destroy()
    }
}
