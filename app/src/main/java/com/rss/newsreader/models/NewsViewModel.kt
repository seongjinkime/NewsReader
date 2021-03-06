package com.rss.newsreader.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rss.newsreader.common.COMPLETE_MSG
import com.rss.newsreader.datas.News
import com.rss.newsreader.states.NewsModelState
import com.rss.newsreader.states.ViewState
import com.rss.newsreader.utils.NewsCrawler

import com.rss.newsreader.utils.NewsResultListener
import com.rss.newsreader.utils.RSSApi
import com.rss.newsreader.utils.XmlParser
import kotlinx.coroutines.*
import java.io.InputStream

/*
 * Created by seongjinkim on 2020-03-28
 */
class NewsViewModel: ViewModel() {

    private val mutableNewsModel = MutableLiveData<NewsModel>()
    val liveData: LiveData<NewsModel> get() = mutableNewsModel
    var taskCnt = 0
    var errCnt = 0
    var successCnt = 0

    /*
     * This is listener for interact with news api
     * Result is load result from news api (success, fail)
     */
    private val resultListener: NewsResultListener = object: NewsResultListener {
        override suspend fun onFail(error: String) {
            taskCnt--
            errCnt++
            completeCheck()
        }

        override suspend fun onSuccess(news: News) {
            successCnt++
            publishNews(news)
        }
    }

    private var newsModel = NewsModel()
        set(value) {
            field = value
            mutableNewsModel.setValue(value);
        }

    /*
     * This function is called by view
     * Run function by view state
     */
    fun onViewStateChange(state: ViewState) {
        when (state) {
            is ViewState.progress -> toProgress()
            is ViewState.update   -> toUpdate()
        }
    }

    private fun toProgress() {
        val progressModel = NewsModel(NewsModelState.progress)
        updateModel(progressModel)
    }

    private fun updateModel(model: NewsModel) {
        newsModel = newsModel.copy(model.state, model.data, model.msg)
    }
    /*
     * (1) Read rss news list from rss api
     * (2) Read all of news by news api
     */
    private fun toUpdate() {
        toProgress()
        GlobalScope.launch(Dispatchers.IO) {
            var rss: InputStream = RSSApi.readRss()
            var rssNewsList = XmlParser.parseRss(rss)
            taskCnt = rssNewsList.size
            initValue()
            NewsCrawler(resultListener).crawlingNewsAll(rssNewsList)
        }
    }

    private fun initValue() {
        successCnt = 0
        errCnt = 0
    }

    /*
     * This function called from result listener of news api
     * Send news to model
     */
    private suspend fun publishNews(news: News) {
        withContext(Dispatchers.Main) {
            val publishModel = NewsModel(NewsModelState.publish, news)
            updateModel(publishModel)
            taskCnt--
            completeCheck()
        }
    }

    private suspend fun completeCheck() {
        if (taskCnt == 0) {
            toComplete()
        }
    }

    private suspend fun toComplete() {
        withContext(Dispatchers.Main) {
            val msg = COMPLETE_MSG.format(successCnt, errCnt)
            val completeModel = NewsModel(NewsModelState.completed, msg = msg)
            updateModel(completeModel)
        }
    }

}