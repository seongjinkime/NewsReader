package com.myrealtrip.newsreader.models

import com.myrealtrip.newsreader.datas.News
import com.myrealtrip.newsreader.states.NewsModelState

/*
 * Created by seongjinkim on 2020-03-28
 */
data class NewsModel(
    val state: NewsModelState = NewsModelState.progress,
    val data: News = News(),
    val msg: String = ""
)