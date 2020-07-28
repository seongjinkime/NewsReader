import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.rss.newsreader.R
import com.rss.newsreader.common.NEWS_KEY
import com.rss.newsreader.datas.News
import kotlinx.android.synthetic.main.activity_news_view.*

/**
 * NewsViewActivity Of Application.
 *
 * This Activity show news content
 */

class NewsViewActivity: AppCompatActivity() {
    private lateinit var news: News

    /**
     * This is a function for creating an activity view
     *
     * @param savedInstanceState Any [Bundle] object that have data for initializing
     * Receive news data from bundle object
     * Set news data to text widgets
     * Start web view loading
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_view)
        news = intent.getParcelableExtra(NEWS_KEY)
        setInfoUi()
        loadNews()
    }

    /**
     * This function shows the title and keyword of news data in text view widgets.
     *
     * Display the title of the news in the text widget for the title
     * The keyword of news data does not contain more than 3 keywords
     * All text widgets for keyword are hidden before the function execution
     * Add text view to list and display keyword of news data
     */
    private fun setInfoUi() {
        news_view_tv_title.text = news.title
        var keywordViews = listOf<TextView>(news_view_keyword_first,
                                            news_view_keyword_second,
                                            news_view_keyword_third)
        for (idx in 0..news.keywords.size - 1) {
            keywordViews.get(idx).text = news.keywords.get(idx)
            keywordViews.get(idx).visibility = View.VISIBLE
        }
    }

    /**
     * This function loads the body of the news into the web view.
     *
     * wv_news is the web view widget specified in the layout
     * After setting various settings necessary for web view, it starts to load url.
     */
    private fun loadNews() {
        val link = news.link
        wv_news.settings.javaScriptEnabled = true
        wv_news.settings.loadWithOverviewMode = true
        wv_news.settings.useWideViewPort = true
        wv_news.settings.domStorageEnabled = true
        wv_news.webViewClient = WebViewClient()
        wv_news.loadUrl(link)
    }

    /**
     * This function is called when the activity is destroyed during the Android life cycle.
     *
     * Delete all views included in the layout
     * Delete the cache stored in the web view and then destroy.
     */
    override fun onDestroy() {
        super.onDestroy()
        layout_news_view.removeAllViews()
        wv_news.clearCache(true)
        wv_news.destroy()
    }
}
