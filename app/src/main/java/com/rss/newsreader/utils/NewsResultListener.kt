package com.rss.newsreader.utils

import com.rss.newsreader.datas.News

/*
 * Created by seongjinkim on 2020-03-28
 */
interface NewsResultListener {
    suspend fun onSuccess(news: News)
    suspend fun onFail(error: String)
}