package com.rss.newsreader

import com.rss.newsreader.datas.News
import com.rss.newsreader.utils.NewsCrawler
import com.rss.newsreader.utils.NewsResultListener
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class NewsReceiverTest {

    private lateinit var newsList: ArrayList<News>
    private lateinit var newsCrawler: NewsCrawler
    private var resultListener = object : NewsResultListener{
        override suspend fun onSuccess(news: News) {}
        override suspend fun onFail(error: String) {}
    }

    @Before
    fun setUp(){
        newsCrawler = NewsCrawler(resultListener)
        newsList = ArrayList()
        newsList.add(News(link = "http://www.hani.co.kr/arti/society/society_general/935368.html"))
        newsList.add(News(link = "https://biz.chosun.com/site/data/html_dir/2020/04/02/2020040204402.html"))
    }

    @Test
    fun readNewsTest(){
        runBlocking {
            for (newsBasic in newsList) {
                val news = loadNewsContent(newsBasic)
                println(news)
                Assert.assertNotNull(news)
            }
        }
    }

    @Test
    fun keywordTest(){
        runBlocking {
            for (newsBasic in newsList) {
                val news = loadNewsContent(newsBasic)
                val keywords = newsCrawler.extractKeyword(news.description)
                println(keywords)
                Assert.assertTrue(keywords.size >= 3)
            }
        }
    }

    suspend fun loadNewsContent(news: News): News {
        val doc = newsCrawler.loadDocument(news.link)
        val news = newsCrawler.createNewsObject(news.index, news.title, news.link, doc)
        return news
    }

}