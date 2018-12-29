package cl.svasquezm.reignnewsapp.helpers

import cl.svasquezm.reignnewsapp.models.NewsModel
import io.realm.Realm
import io.realm.Sort
import org.json.JSONObject

class NewsHelper {

    // Default real used for this purpose
    private var realm = Realm.getDefaultInstance()

    /**
     * Save all news from jsonArray
     */
    fun saveNewsFromJSONArray(jsonObject: JSONObject) {
        realm.executeTransaction {
            it.createOrUpdateAllFromJson(NewsModel::class.java, jsonObject.getJSONArray(HITS_JSON_KEY))
        }
    }

    fun deleteNews(element: NewsModel){
        realm.executeTransaction { element.deleted = true }
    }

    /**
     * Returns all non deleted news sorted by time
     */
    fun getAllNonDeletedNews() = ArrayList(realm.where(NewsModel::class.java)
            .equalTo("deleted", false)
            .sort("created_at", Sort.DESCENDING)
            .findAll())

    companion object {

        // News 'hits' key for server's json response
        private const val HITS_JSON_KEY = "hits"
    }
}