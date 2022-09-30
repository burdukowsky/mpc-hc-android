package tk.burdukowsky.mpc_hc_android

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class CommandNotificationService : Service() {

    class CommandInfo(val resourceId: Int, val requestCode: Int)

    private val notificationId = 42
    private val notificationChannelIdValue = "commandNotificationChannelId"
    private lateinit var notificationChannelId: String
    private val stopServiceReceiverIntentFilterAction = "stopServiceReceiver"
    private val commandReceiverIntentFilterAction = "commandReceiver"
    private val commandReceiverIntentExtra = "command"
    private val commandInfoMap: Map<Command, CommandInfo> = mapOf(
        Command.play to CommandInfo(R.id.notification_play, 0),
        Command.pause to CommandInfo(R.id.notification_pause, 1),
        Command.volume_down to CommandInfo(R.id.notification_volume_down, 2),
        Command.volume_up to CommandInfo(R.id.notification_volume_up, 3)
    )

    override fun onCreate() {
        notificationChannelId = getNotificationChannelId()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_DETACH)
        } else {
            stopForeground(true)
        }
        unregisterReceiver(stopServiceReceiver)
        unregisterReceiver(commandReceiver)
        super.onDestroy()
    }

    private fun start() {
        registerReceiver(stopServiceReceiver, IntentFilter(stopServiceReceiverIntentFilterAction))
        registerReceiver(commandReceiver, IntentFilter(commandReceiverIntentFilterAction))

        val remoteViews = RemoteViews(packageName, R.layout.notification)

        remoteViews.setOnClickPendingIntent(
            R.id.notification_close,
            PendingIntent.getBroadcast(
                this,
                -1,
                Intent(stopServiceReceiverIntentFilterAction),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )

        for ((command, commandInfo) in commandInfoMap) {
            remoteViews.setOnClickPendingIntent(
                commandInfo.resourceId,
                PendingIntent.getBroadcast(
                    this,
                    commandInfo.requestCode,
                    Intent(commandReceiverIntentFilterAction)
                        .apply { putExtra(commandReceiverIntentExtra, command.value) },
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

    private var stopServiceReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            stopSelf()
        }
    }

    private var commandReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                DoAsync {
                    try {
                        CommandService.send(intent.getIntExtra(commandReceiverIntentExtra, 0))
                    } catch (t: Throwable) {
                    }
                }
            }
        }
    }

    private fun getNotificationChannelId(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannelAndGetId()
        } else {
            notificationChannelIdValue
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannelAndGetId(): String {
        val channel =
            NotificationChannel(
                notificationChannelIdValue,
                "MPC-HC Remote Control Service",
                NotificationManager.IMPORTANCE_NONE
            )
        channel.lightColor = Color.DKGRAY
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
        return notificationChannelIdValue
    }

}
