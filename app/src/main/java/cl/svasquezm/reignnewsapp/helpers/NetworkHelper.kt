package cl.svasquezm.reignnewsapp.helpers

import android.content.Context
import cl.svasquezm.reignnewsapp.R
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class NetworkHelper(private val context: Context) {
    private val requestQueue = Volley.newRequestQueue(context)

    /**
     * Makes main request to news' endpoint
     */
    fun doNewsRequest(onSuccess: (JSONObject) -> Unit, onError: () -> Unit = {}){
        val request = JsonObjectRequest(Request.Method.GET,
                context.getString(R.string.news_url),
                null,
                Response.Listener<JSONObject> {
                    onSuccess.invoke(it)
                },
                Response.ErrorListener {
                    onError.invoke()
                }
        )

        // Add request to queue
        requestQueue.add(request)
    }
}