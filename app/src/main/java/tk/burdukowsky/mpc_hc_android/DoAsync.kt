package tk.burdukowsky.mpc_hc_android

import android.os.AsyncTask

class DoAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {

    init {
        execute()
    }

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Void?): Void? {
        handler()
        return null
    }

}
