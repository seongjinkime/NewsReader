package com.myrealtrip.newsreader.utils

import com.myrealtrip.newsreader.datas.News
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream

/*
 * Created by seongjinkim on 2020-03-28
 */
object XmlParser {

    private fun getXmlFactory(): XmlPullParserFactory {
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true
        return factory
    }

    private fun getXmlParser(input: InputStream): XmlPullParser {
        var parser = getXmlFactory().newPullParser()
        parser.setInput(input, null)
        return parser
    }

    /*
     * Parse xml of rss
     * Returns the given input as a news list
     */
    fun parseRss(rss: InputStream): ArrayList<News> {
        var index: Int = 0
        var parser = getXmlParser(rss)
        var newsList = ArrayList<News>()
        var eventType = parser.eventType
        var text: String? = null
        var title = ""
        while (eventType != XmlPullParser.END_DOCUMENT) {
            val tagname = parser.name
            if (eventType == XmlPullParser.TEXT) {
                text = parser.text
            }
            if (eventType == XmlPullParser.END_TAG) {
                if (tagname.equals("title", ignoreCase = true)) {
                    title = text.toString()
                } else if (tagname.equals("link", ignoreCase = true)) {
                    var link = text.toString()
                    newsList.add(News(index = index, title = title, link = link))
                    index++
                }
            }
            eventType = parser.next()
        }
        newsList.removeAt(0)
        return newsList
    }
}