package tk.burdukowsky.mpc_hc_android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class CommandReceiver : BroadcastReceiver() {

    companion object {
        const val intentExtra = "command"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            DoAsync {
                try {
                    CommandService.send(intent.getIntExtra(intentExtra, 0))
                } catch (t: Throwable) {
                }
            }
        }
    }

}
