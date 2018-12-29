package cl.svasquezm.reignnewsapp.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import android.widget.Toast
import cl.svasquezm.reignnewsapp.R
import cl.svasquezm.reignnewsapp.helpers.NetworkHelper
import cl.svasquezm.reignnewsapp.helpers.NewsHelper
import cl.svasquezm.reignnewsapp.models.NewsModel

class NewsDetailsFragment : Fragment() {

    // All non deleted news to be shown
    private var webView: WebView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            LayoutInflater.from(context).inflate(R.layout.fragment_news_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = arguments!!.getString(URL)

        // Configure WebView
        webView = view.findViewById(R.id.webview)
        webView?.loadUrl(url)
    }

    companion object {
        // Bundle key used to get news' story_url
        const val URL = "story_url"

        fun newInstance(bundle: Bundle) = NewsDetailsFragment().apply { arguments = bundle }
    }
}