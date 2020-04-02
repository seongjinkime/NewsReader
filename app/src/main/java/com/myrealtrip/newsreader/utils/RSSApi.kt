package com.myrealtrip.newsreader.utils

import com.myrealtrip.newsreader.common.RSS_LINK
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/*
 * Created by seongjinkim on 2020-03-28
 */
object RSSApi {

    private fun getConnection(): HttpURLConnection {
        val connection = URL(RSS_LINK).openConnection() as HttpURLConnection
        connection.readTimeout = 8000
        connection.connectTimeout = 8000
        connection.requestMethod = "GET"
        return connection
    }

    private fun getInputStream(connection: HttpURLConnection): InputStream {
        connection.connect()
        return connection.inputStream
    }

    suspend fun readRss(): InputStream {
        val connection = getConnection()
        val inputStream = getInputStream(connection)
        return inputStream
    }
}