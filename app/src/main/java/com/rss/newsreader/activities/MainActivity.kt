package com.rss.newsreader.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rss.newsreader.R
import com.rss.newsreader.adapters.NewsAdapter
import com.rss.newsreader.common.*
import com.rss.newsreader.datas.News
import com.rss.newsreader.models.NewsModel
import com.rss.newsreader.models.NewsViewModel
import com.rss.newsreader.states.NewsModelState
import com.rss.newsreader.states.ViewState
import kotlinx.android.synthetic.main.activity_main.*

/*
 * Created by seongjinkim on 2020-03-28
 */

class MainActivity: AppCompatActivity() {


    private var newsListState: Parcelable? = null
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsViewModel: NewsViewModel
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        refresh_layout.setOnRefreshListener { refresh() }
        setNewsList()
        if (savedInstanceState == null) {
            refresh()
        } else {
            restoreNewsList(savedInstanceState)
        }
    }

    private fun setNewsList() {
        newsAdapter = NewsAdapter() { showNews(it) }
        rv_news_list.adapter = newsAdapter
        rv_news_list.layoutManager = LinearLayoutManager(this)
    }

    private fun restoreNewsList(bundle: Bundle) {
        newsListState = bundle.getParcelable(NEWS_LIST_STATE)
        newsAdapter.items = bundle.getParcelableArrayList(NEWS_LIST_ITEMS)
        rv_news_list.layoutManager?.onRestoreInstanceState(newsListState)
        showNewsList()
    }

    private fun refresh() {
        permissionCheck()
        if (!connectCheck()) {
            showToast(NETWORK_CHECK_MSG)
            return
        }
        newsAdapter.items.clear()
        newsAdapter.notifyDataSetChanged()

        if (::newsViewModel.isInitialized == false) {
            subscribeModel()
        }
        newsViewModel.onViewStateChange(ViewState.update)
    }

    private fun subscribeModel() {
        newsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        newsViewModel.liveData.observe(this, Observer { render(it) })
    }

    /*
     * Save state of recycler view in bundle
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(NEWS_LIST_ITEMS, newsAdapter.items)
        val state = rv_news_list.layoutManager?.onSaveInstanceState()!!
        outState.putParcelable(NEWS_LIST_STATE, state)
    }

    private fun permissionCheck() {
        val permissions = listOf(Manifest.permission.INTERNET,
                                 Manifest.permission.ACCESS_NETWORK_STATE)

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(permission), NETWORK_REQUEST)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            NETWORK_REQUEST -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    refresh()
                }
            }
        }
    }

    private fun connectCheck(): Boolean {
        val manager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network: NetworkInfo? = manager.activeNetworkInfo
        return network?.isConnected == true
    }

    /*
     * This function called whenever news model is change.
     * Change ui element by model state
     */
    fun render(model: NewsModel) {
        when (model.state) {
            is NewsModelState.progress  -> refresh_layout.isRefreshing = true
            is NewsModelState.publish   -> newsAdapter.items.add(model.data)
            is NewsModelState.completed -> {
                showNewsList()
                showToast(model.msg)
            }
        }
    }

    private fun showNewsList() {
        newsAdapter.items.sortBy { it.index }
        newsAdapter.notifyDataSetChanged()
        refresh_layout.isRefreshing = false
    }

    private fun showNews(news: News) {
        val newsIntent = Intent(this, NewsViewActivity::class.java)
        newsIntent.putExtra(NEWS_KEY, news)
        startActivity(newsIntent)
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() > backPressedTime + 2000) {
            backPressedTime = System.currentTimeMillis()
            showToast(QUITE_MSG)
            return
        }
        super.onBackPressed()
        finish()
    }
}