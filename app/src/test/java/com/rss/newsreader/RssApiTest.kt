package com.rss.newsreader

import com.rss.newsreader.utils.RSSApi
import kotlinx.coroutines.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.InputStream

class RssApiTest {

    private lateinit var rssApi: RSSApi

    @Before
    fun setUp(){
        rssApi = RSSApi
    }

    @Test
    fun readRssTest(){
        runBlocking {
            var rssStream: InputStream = rssApi.readRss()
            println(rssStream)
            Assert.assertNotNull(rssStream)
        }
    }
}