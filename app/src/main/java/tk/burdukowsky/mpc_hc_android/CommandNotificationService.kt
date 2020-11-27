package tk.burdukowsky.mpc_hc_android

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat

class CommandNotificationService : Service() {

    class CommandInfo(val resourceId: Int, val requestCode: Int)

    private val notificationId = 42
    private val notificationChannelId = "commandNotificationChannelId"
    private val commandInfoMap: Map<Command, CommandInfo> = mapOf(
        Command.play to CommandInfo(R.id.notification_play, 0),
        Command.pause to CommandInfo(R.id.notification_pause, 1),
        Command.volume_down to CommandInfo(R.id.notification_volume_down, 2),
        Command.volume_up to CommandInfo(R.id.notification_volume_up, 3)
    )

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        stopForeground(true)
        super.onDestroy()
    }

    private fun start() {
        val remoteViews = RemoteViews(packageName, R.layout.notification)

        for ((command, commandInfo) in commandInfoMap) {
            remoteViews.setOnClickPendingIntent(
                commandInfo.resourceId,
                PendingIntent.getBroadcast(
                    applicationContext,
                    commandInfo.requestCode,
                    Intent("tk.burdukowsky.mpc_hc_android.NOTIFICATION_COMMAND")
                        .apply { putExtra(CommandReceiver.intentExtra, command.value) },
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
        }

        val notification: Notification =
            NotificationCompat.Builder(this, notificationChannelId).apply {
                setSmallIcon(R.mipmap.ic_launcher)
                setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                setOngoing(true)
                setCustomContentView(remoteViews)
                setStyle(NotificationCompat.DecoratedCustomViewStyle())
            }.build()
        startForeground(notificationId, notification)
    }

}
