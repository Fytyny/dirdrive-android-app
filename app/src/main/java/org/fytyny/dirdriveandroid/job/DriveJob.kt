package org.fytyny.dirdriveandroid.job

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import org.fytyny.dirdrive.api.dto.DirectoryDTO
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

    override fun onStartJob(params: JobParameters?): Boolean {
        executor = Executors.newSingleThreadExecutor()
        val extras = params?.extras
        val dir : DirectoryDTO = DirectoryDTO()
        dir.path = extras!!.getString(DIR_SERVER_PATH)
        dir.label = extras!!.getString(DIR_SERVER_LABEL)
        val driveRunnable = DriveRunnable(dir, extras.getString(DIR_LOCAL_PATH))
        executor?.execute({driveRunnable.run(); jobFinished(params,false)})
        return true
    }
}