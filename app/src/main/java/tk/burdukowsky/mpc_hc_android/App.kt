package tk.burdukowsky.mpc_hc_android

import android.app.Application

class App : Application() {

    companion object {
        lateinit var instance: App
    }

    init {
        instance = this
    }

}
