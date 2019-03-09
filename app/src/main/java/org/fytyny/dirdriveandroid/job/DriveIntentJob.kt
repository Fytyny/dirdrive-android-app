package org.fytyny.dirdriveandroid.job

import android.app.Notification.GROUP_ALERT_SUMMARY
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.AsyncTask
import android.os.Build
import android.support.v4.app.JobIntentService
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import kotlinx.android.synthetic.*
import org.fytyny.dirdrive.api.dto.DirectoryDTO
import org.fytyny.dirdriveandroid.MainActivity
import org.fytyny.dirdriveandroid.R
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DriveIntentJob : JobIntentService() {
    companion object {
        fun enqueue(context: Context, intent: Intent, id: Int) {
            enqueueWork(context, DriveIntentJob::class.java, id, intent)
        }

        val CHANNEL: String = "MyChannelDirDrive"

        var created: Boolean = false
    }

    var executor: ExecutorService? = null
    override fun onCreate() {
        super.onCreate()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        showNotification("Job started")
    }

    fun showNotification(text: String) {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL + "id")
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        if (!created) {
            createNotificationChannel()
            created = true
        }


        val notification = notificationBuilder.setOngoing(true)
                .setContentTitle("DIRDRIVE")
                //    .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setContentText(text)
                //  .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setVibrate(longArrayOf())
                .setSmallIcon(android.R.drawable.stat_notify_sync)
                .setGroupAlertBehavior(GROUP_ALERT_SUMMARY)
                .setGroup("My group")
                .setGroupSummary(false)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(12, notification)
        }
    }

    override fun onHandleWork(intent: Intent) {
        Log.i(this.javaClass.name, "intent Job starting");
        showNotification("Downloading")

        executor = Executors.newSingleThreadExecutor()
        val extras = intent.extras
        val dir: DirectoryDTO = DirectoryDTO()
        dir.path = extras!!.getString(DriveJob.DIR_SERVER_PATH)
        dir.label = extras!!.getString(DriveJob.DIR_SERVER_LABEL)
        val driveRunnable = DriveRunnable(dir, extras.getString(DriveJob.DIR_LOCAL_PATH), NotificationDriveJobLogging(this))
        AsyncTask.execute({ driveRunnable.run(); stopSelf() })

    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL
            val descriptionText = "Connection info"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL + "id", name, importance).apply {
                description = descriptionText
            }
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf()
            // Register the CHANNEL with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true); //true will remove notification
        }
        Log.i(this.javaClass.name, "intent Job destroyed");

    }
}