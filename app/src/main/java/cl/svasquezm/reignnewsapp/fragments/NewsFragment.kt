package cl.svasquezm.reignnewsapp.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import cl.svasquezm.reignnewsapp.NewsFragmentNavigator
import cl.svasquezm.reignnewsapp.R
import cl.svasquezm.reignnewsapp.helpers.NetworkHelper
import cl.svasquezm.reignnewsapp.helpers.NewsHelper
import cl.svasquezm.reignnewsapp.helpers.NewsSwipeCallback
import cl.svasquezm.reignnewsapp.models.NewsModel

class NewsFragment : Fragment() {

    // Fragments navigator
    private var navigator: NewsFragmentNavigator? = null

    // All non deleted news to be shown
    private var nonDeletedNews = NewsHelper().getAllNonDeletedNews()

    // Swipe refresh layout that contains recyclerview with news
    private var refreshLayout: SwipeRefreshLayout? = null

    // Main recycler view
    private var recyclerView: RecyclerView? = null

    // Item touch layout
    var itemTouchHelper: ItemTouchHelper? = null

    companion object {
        // Only request server news for the first time the application was launched
        var SERVER_WAS_REQUESTED = false

        fun newInstance() = NewsFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navigator = activity as NewsFragmentNavigator
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            LayoutInflater.from(context).inflate(R.layout.fragment_news_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configure news' list
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView?.layoutManager = LinearLayoutManager(context)

        if(!SERVER_WAS_REQUESTED) {
            SERVER_WAS_REQUESTED = true
            requestNewsList()
        } else {
            configureRecyclerView()
        }

        // Set refresh layout
        refreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        refreshLayout?.setOnRefreshListener {
            requestNewsList()
        }
    }

    /**
     * Retrieve news from server
     */
    private fun requestNewsList(){
        NetworkHelper(context!!).doNewsRequest({
            NewsHelper().saveNewsFromJSONArray(it)

            // (Re)load saved news
            nonDeletedNews = NewsHelper().getAllNonDeletedNews()

            // Sets adapter for recyclerview
            configureRecyclerView()

            refreshLayout?.isRefreshing = false
        }, {
            // Failed, show a toast
            Toast.makeText(context!!, getString(R.string.server_error), Toast.LENGTH_SHORT).show()

            refreshLayout?.isRefreshing = false
        })
    }

    private fun configureRecyclerView(){

        if(recyclerView?.adapter == null) {
            recyclerView?.adapter = NewsAdapter(NewsHelper().getAllNonDeletedNews())
        } else {
            (recyclerView?.adapter as NewsAdapter).news = NewsHelper().getAllNonDeletedNews()
        }
        recyclerView?.adapter?.notifyDataSetChanged()
        if(itemTouchHelper == null) {
            itemTouchHelper = ItemTouchHelper(NewsSwipeCallback(recyclerView?.adapter as NewsAdapter))
            itemTouchHelper?.attachToRecyclerView(recyclerView)
        }
    }

    inner class NewsAdapter(var news: ArrayList<NewsModel>) : RecyclerView.Adapter<NewsViewHolder>(){

        // Item click listener
        var clickListener: (NewsModel) -> Unit = {}

        override fun onCreateViewHolder(container: ViewGroup, position: Int): NewsViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.item_news, container, false)
            return NewsViewHolder(view)
        }

        override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
            holder.apply {
                itemClickListener = clickListener
            }.bind(news[position])
        }

        override fun getItemCount() = news.size

        fun deleteItem(position: Int) {
            NewsHelper().deleteNews(news[position])
            notifyItemRemoved(position)
            news.removeAt(position)
        }
    }

    /**
     * View holder used for previous adapter
     */
    inner class NewsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView         = view.findViewById(R.id.title)
        val authorAndTime: TextView = view.findViewById(R.id.author_and_time)
        var itemClickListener: (NewsModel) -> Unit = {}

        /**
         * Binds title, author and time values. If any of previous values does not exist (null)
         * then use a string placeholder
         */
        fun bind(element: NewsModel){

            // Add Click Listener
            view.setOnClickListener { itemClickListener.invoke(element) }

            title.text = element.story_title?: view.context.getString(R.string.news_title_placeholder)

            // Prepare author and time strings
            val stringBuilder = StringBuilder()
            element.author?.let {
                stringBuilder.append(it)
                stringBuilder.append(" - ")
            }

            element.created_at?.let {
                stringBuilder.append(element.getRelativeCreatedAtDateString())
            }

            authorAndTime.text = stringBuilder.toString()
        }
    }
}