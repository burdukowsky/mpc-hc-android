package tk.burdukowsky.mpc_hc_android

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object GsonProvider {
    val instance: Gson = GsonBuilder().create()
}
