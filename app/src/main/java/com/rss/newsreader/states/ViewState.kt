package com.rss.newsreader.states

/*
 * Created by seongjinkim on 2020-03-28
 */
sealed class ViewState {
    object progress: ViewState()
    object update: ViewState()
}