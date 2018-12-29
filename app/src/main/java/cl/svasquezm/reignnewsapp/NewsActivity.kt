package cl.svasquezm.reignnewsapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import cl.svasquezm.reignnewsapp.fragments.NewsDetailsFragment
import cl.svasquezm.reignnewsapp.fragments.NewsFragment
import cl.svasquezm.reignnewsapp.models.NewsModel
import kotlinx.android.synthetic.main.activity_news.*

/**
 * Main activity. It contains two fragments. The first one is the news list which contains a click listener per
 * item which lead user to webview (the second one) with selected news' details.
 */
class NewsActivity : AppCompatActivity(), NewsFragmentNavigator {
    var isShowingWebView = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        setSupportActionBar(findViewById(R.id.toolbar))

        // Set news list as first fragment
        setNewsListFragment()
    }

    /**
     * Sets news' list
     */
    override fun setNewsListFragment() {
        isShowingWebView = false
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContent, NewsFragment.newInstance())
                .commit()
    }

    /**
     * Replaces fragment for webview news' details
     */
    override fun setNewsDetailsFragment(element: NewsModel) {
        // Check if has url
        if(element.story_url != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_action_name)
            toolbar.setNavigationOnClickListener { onBackPressed() }
            isShowingWebView = true
            val bundle = Bundle().apply {
                putString(NewsDetailsFragment.URL, element.story_url)
            }

            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContent, NewsDetailsFragment.newInstance(bundle))
                    .commit()
        } else {
            Toast.makeText(this, getString(R.string.no_url_provided), Toast.LENGTH_SHORT).show()
        }

    }

    override fun onBackPressed() {
        // Show news's list when webview is showing
        if (isShowingWebView) {
            setNewsListFragment()
        } else {
            super.onBackPressed()
        }
    }
}

interface NewsFragmentNavigator {
    fun setNewsListFragment()
    fun setNewsDetailsFragment(element: NewsModel)
}
