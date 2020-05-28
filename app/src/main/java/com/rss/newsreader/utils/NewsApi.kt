package com.rss.newsreader.utils

import com.rss.newsreader.common.EXCEPT_CHAR


import com.rss.newsreader.datas.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.Exception
import java.util.*

/*
 * Created by seongjinkim on 2020-03-28
 * Usw Jsoup Library for load html
 * https://jsoup.org/download
 */
class NewsApi(val newsResultListener: NewsResultListener) {

    fun extractKeyword(content: String): ArrayList<String> {
        var keywords = ArrayList<String>()
        val splits = content.filter { !EXCEPT_CHAR.contains(it) }.split(" ").sorted() //split text by " " and sort
        val frequencyMap = splits.filter { it.length >= 2 }.asSequence().groupingBy { it }.eachCount() //grouping by split text and make map with count
        val keywordList = frequencyMap.toList().sortedByDescending { (_, v) -> v } //sort map descending by count

        //add only three keyword in list
        for (keyword in keywordList) {
            if (keywords.size >= 3) {
                break
            }
            keywords.add(keyword.first)
        }
        return keywords
    }

    private fun queryElementContent(element: String, doc: Document): String {
        val query = doc.select(element)
        return if (query.size > 0) query.first().attr("content") else ""
    }

    fun createNewsObject(index: Int, title: String, link: String, doc: Document): News {
        var description = queryElementContent("meta[property=og:description]", doc)
        var image = queryElementContent("meta[property=og:image]", doc)
        var keywords = extractKeyword(description)
        return News(index, title, image, link, description, keywords)
    }

    suspend fun loadDocument(link: String): Document{
        return Jsoup.connect(link).method(Connection.Method.GET).get() //https://jsoup.org/download
    }

    private suspend fun readNewsAsync(index: Int, title: String, link: String) {
        GlobalScope.async(Dispatchers.IO) {
            try {
                val doc = loadDocument(link)
                var news = createNewsObject(index, title, link, doc)
                newsResultListener.onSuccess(news)
            } catch (e: Exception) {
                e.printStackTrace()
                newsResultListener.onFail(e.message.toString())
            }
        }
    }

    /*
     * This function read all of news in list
     */
    suspend fun readNewsAll(newsList: ArrayList<News>) {
        newsList.forEach { readNewsAsync(it.index, it.title, it.link) }
    }
}