package org.fytyny.dirdriveandroid.job

import android.util.Log

class LogDriveJobLogging : DriveJobLogging {
    override fun log(text: String) {
        Log.i(DriveJob::class.java.name, text)
    }
}