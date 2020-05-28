package com.rss.newsreader.datas

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
 * Created by seongjinkim on 2020-03-28
 */

@Parcelize
data class News(
    val index: Int = 0,
    val title: String = "",
    val thumb: String = "",
    val link: String = "",
    val description: String = "",
    val keywords: List<String> = listOf()
): Parcelable