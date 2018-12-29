package cl.svasquezm.reignnewsapp.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*
import java.util.concurrent.TimeUnit

open class NewsModel(
        @PrimaryKey var objectID: Int = 0,
        var created_at: Date? = null,
        var story_title: String? = null,
        var story_url: String? = null,
        var author: String? = null,

        // It turns true when user swipes a news' cell
        var deleted: Boolean = false
) : RealmObject() {

    /**
     * Returns short relative time in years, months, days, etc
     */
    fun getRelativeCreatedAtDateString(): String {

        // For simplicity purposes, this map has hardcoded strings
        val timeMap = mapOf(
                TimeUnit.DAYS.toMillis(365)  to "year",     // a year
                TimeUnit.DAYS.toMillis(30)   to "month",    // a month
                TimeUnit.DAYS.toMillis(1)    to "day",      // a day
                TimeUnit.HOURS.toMillis(1)   to "hour",     // an hour
                TimeUnit.MINUTES.toMillis(1) to "minute",   // a minute
                TimeUnit.SECONDS.toMillis(1) to "second")   // a second

        val str = StringBuffer()
        for (i in 0 until timeMap.size) {
            val timeUnit = timeMap.entries.elementAt(i).key

            // Fraction has to be greater than zero to be considered a match
            val createdAtMillis = System.currentTimeMillis() - Calendar.getInstance().apply { time = created_at }.timeInMillis
            val frac = createdAtMillis / timeUnit
            if (frac > 0) {
                str.append(frac)
                        .append(" ")
                        .append(timeMap.entries.elementAt(i).value)

                // Append 's' for non singular values
                if(frac > 1L){
                    str.append("s")
                }

                str.append(" ago")
                break
            }
        }

        return str.toString()
    }
}