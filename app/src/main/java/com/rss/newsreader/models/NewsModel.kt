package com.rss.newsreader.models

import com.rss.newsreader.datas.News
import com.rss.newsreader.states.NewsModelState

/*
 * Created by seongjinkim on 2020-03-28
 */
data class NewsModel(
    val state: NewsModelState = NewsModelState.progress,
    val data: News = News(),
    val msg: String = ""
)