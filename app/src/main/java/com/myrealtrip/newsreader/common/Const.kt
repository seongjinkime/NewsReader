package com.myrealtrip.newsreader.common

const val RSS_LINK = "https://news.google.com/rss?hl=ko&gl=KR&ceid=KR:ko"

const val SPLASH_TIME_OUT: Long = 1300

const val NEWS_LIST_STATE = "rv_news_list_state"
const val NEWS_LIST_ITEMS = "news_list_items"
const val NETWORK_REQUEST = 13234

const val DEFAULT_THUMB = "https://image.flaticon.com/icons/svg/124/124033.svg"

const val COMPLETE_MSG = "뉴스 로딩 완료!\n성공: %d 오류: %d"
const val NETWORK_CHECK_MSG = "인터넷에 연결되어 있지 않습니다"
const val QUITE_MSG = "뒤로 가기 버튼을 한번 더 누르시면 종료 됩니다"

const val NEWS_KEY = "NEWS"

val EXCEPT_CHAR = setOf(',', '.', '\'', '‘', '’', '\"', '“', '[', ']', '(', ')')