package tk.burdukowsky.mpc_hc_android

import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

object CommandService {

    private const val protocol = "http://"
    private const val controlRoute = "/command.html"

    fun send(cmd: Int) {
        val host = AppPreferences.getCurrentHost()
        val body = "wm_command=$cmd"
        val postData: ByteArray = body.toByteArray()

        val url = URL("$protocol$host$controlRoute")
        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.connectTimeout = 2000
        urlConnection.doOutput = true
        urlConnection.requestMethod = "POST"
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        urlConnection.setRequestProperty("Content-Length", postData.size.toString())
        DataOutputStream(urlConnection.outputStream).use { wr -> wr.write(postData) }
        urlConnection.inputStream
        urlConnection.disconnect()
    }

}
