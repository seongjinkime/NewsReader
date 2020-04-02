package com.myrealtrip.newsreader

import com.myrealtrip.newsreader.datas.News
import com.myrealtrip.newsreader.utils.NewsApi
import com.myrealtrip.newsreader.utils.NewsResultListener
import com.myrealtrip.newsreader.utils.RSSApi
import com.myrealtrip.newsreader.utils.XmlParser
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.InputStream

class NewsApiTest {

    private lateinit var newsList: ArrayList<News>
    private lateinit var newsApi: NewsApi
    private var resultListener = object : NewsResultListener{
        override suspend fun onSuccess(news: News) {}
        override suspend fun onFail(error: String) {}
    }

    @Before
    fun setUp(){
        newsApi = NewsApi(resultListener)
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
                val keywords = newsApi.extractKeyword(news.description)
                println(keywords)
                Assert.assertTrue(keywords.size >= 3)
            }
        }
    }

    suspend fun loadNewsContent(news: News): News {
        val doc = newsApi.loadDocument(news.link)
        val news = newsApi.createNewsObject(news.index, news.title, news.link, doc)
        return news
    }

}