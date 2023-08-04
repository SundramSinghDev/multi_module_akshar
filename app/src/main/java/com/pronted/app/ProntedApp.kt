package com.pronted.app

import android.app.Application
import androidx.multidex.MultiDex
import com.base.BaseApp
import com.google.firebase.FirebaseApp
import com.pronted.di.aksharOneModule
import com.pronted.di.apiModule
import com.pronted.di.networkModule
import com.pronted.di.repositoryModule
import com.pronted.di.viewModelModule
import dagger.hilt.android.HiltAndroidApp
import io.paperdb.Paper
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

@HiltAndroidApp
class ProntedApp : Application() {

    var main_filters: HashMap<String, ArrayList<String>>? = null
    var seller_main_filters: HashMap<String, ArrayList<String>>? = null
    var dealResponse = ""
    var categoryresponse = ""
    var productresponse = ""
    var loadeddeal = false
    var loadedproducts = false

    override fun onCreate() {
        super.onCreate()
        init(this)
        Paper.init(this)
        BaseApp.init(this)
        FirebaseApp.initializeApp(this)
        main_filters = HashMap<String, ArrayList<String>>()
        seller_main_filters = HashMap<String, ArrayList<String>>()
        MultiDex.install(this)
        startKoin {
            androidContext(this@ProntedApp)
            modules(
                listOf(
                    aksharOneModule, networkModule, apiModule, viewModelModule, repositoryModule
                )
            )
        }
    }

    companion object {
        lateinit var application: Application
        fun init(app: Application) {
            application = app
        }
    }
}