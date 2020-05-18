package org.fytyny.dirdriveandroid.job

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import org.fytyny.dirdrive.api.dto.DirectoryDTO
import org.fytyny.dirdriveandroid.MainActivity
import java.time.LocalDateTime
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DriveJob constructor(): JobService() {

    companion object {
        val DIR_SERVER_PATH = "dir_server_path"
        val DIR_SERVER_LABEL = "dir_server_label"
        val DIR_LOCAL_PATH = "dir_local_path"
    }

    var executor : ExecutorService? = null

    override fun onStopJob(params: JobParameters?): Boolean {
        executor?.shutdownNow()
        return true
    }
/*
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.i(this.javaClass.name,"Job starting");

        executor = Executors.newSingleThreadExecutor()
        val extras = params?.extras
        val dir : DirectoryDTO = DirectoryDTO()
        dir.path = extras!!.getString(DIR_SERVER_PATH)
        dir.label = extras!!.getString(DIR_SERVER_LABEL)
        val driveRunnable = DriveRunnable(dir, extras.getString(DIR_LOCAL_PATH), LogDriveJobLogging())
        executor?.execute({driveRunnable.run(); })
        jobFinished(params,false)

        return true
    }
*/

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.i(this.javaClass.name,"Job starting");

        val extras = params?.extras
        val dir : DirectoryDTO = DirectoryDTO()
        dir.path = extras!!.getString(DIR_SERVER_PATH)
        dir.label = extras!!.getString(DIR_SERVER_LABEL)
        val destinationPath = extras.getString(DIR_LOCAL_PATH)

        val currentSsid = MainActivity.getCurrentSsid(this)?.toLowerCase()
        val contains = currentSsid?.contains("szyna")
        if (contains != null && contains) {
            val persistableBundle: Bundle = Bundle()

            persistableBundle.putString(DriveJob.DIR_SERVER_LABEL, dir.label)
            persistableBundle.putString(DriveJob.DIR_SERVER_PATH, dir.path)
            persistableBundle.putString(DriveJob.DIR_LOCAL_PATH, destinationPath)

            val intent = Intent(this, DriveIntentJob::class.java)
            intent.putExtras(persistableBundle)
            ContextCompat.startForegroundService(this, intent)
            DriveIntentJob.enqueue(this, intent, params.jobId + 9999)
        }else {
            Log.i(javaClass.name, "Wifi was not found")
        }
        jobFinished(params, false)

        return true
    }
}