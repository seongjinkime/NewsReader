package com.rss.newsreader.states

/*
 * Created by seongjinkim on 2020-03-28
 */
sealed class NewsModelState {
    object progress: NewsModelState()
    object publish: NewsModelState()
    object completed: NewsModelState()
}