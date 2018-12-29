package cl.svasquezm.reignnewsapp.base

import android.app.Application
import io.realm.Realm

/**
 * Custom Application class used to start Realm database
 */
class ReignNewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}