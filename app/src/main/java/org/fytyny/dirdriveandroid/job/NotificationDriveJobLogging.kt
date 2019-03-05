package org.fytyny.dirdriveandroid.job

import android.util.Log

class NotificationDriveJobLogging(val driveIntentJob: DriveIntentJob) : DriveJobLogging {


    override fun log(text: String) {
        Log.i(DriveIntentJob::class.java.name, text)
        driveIntentJob.showNotification(text)
    }
}