package org.fytyny.dirdriveandroid.service

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import org.fytyny.dirdrive.api.dto.DirectoryDTO
import org.fytyny.dirdriveandroid.MainActivity
import org.fytyny.dirdriveandroid.annotation.Mockable
import org.fytyny.dirdriveandroid.component.DaggerDefaultComponent
import org.fytyny.dirdriveandroid.component.DefaultComponent
import org.fytyny.dirdriveandroid.job.DriveIntentJob
import org.fytyny.dirdriveandroid.job.DriveJob
import org.fytyny.dirdriveandroid.job.DriveRunnable
import org.fytyny.dirdriveandroid.util.DirectoryMapper
import java.io.File

@Mockable
class MainActivityServiceImpl constructor(val mainActivity: MainActivity) : MainActivityService {

    protected var component: DefaultComponent? = null
    private var map: MutableMap<DirectoryDTO, String?>? = null

    init {
        component = DaggerDefaultComponent.create()
    }

    companion object {
        val CONFIG_NAME: String = "config.json"
    }

    override fun isConnected() : Boolean{
        if (component == null) return false
        else {
            val currentSsid = MainActivity.getCurrentSsid(mainActivity)?.toLowerCase()
            val contains = currentSsid?.contains("szyna")
            val test =     currentSsid?.contains("androidwifi")

            if ((contains != null  && !contains ) && (test != null && !test)){
                Log.i(javaClass.name, "Wifi was not found")
                Toast.makeText(mainActivity.applicationContext,"Cannot find your network. Please make sure location services are turned on!",Toast.LENGTH_LONG).show()
                return false
            }
        }
        return component!!.getClient().establishConnection()
    }

    @Synchronized
    override fun getDirectories(): Map<DirectoryDTO, String?> {
        if (map != null) return this.map!!;
        val dirs = getDirsFromServer()
        val readDirectories = getMappedDirs()
        val result: MutableMap<DirectoryDTO, String?> = HashMap()
        for (dir: DirectoryDTO in dirs) {
            result.put(dir, readDirectories.get(dir))
        }
        map = result
        return map!!
    }

    fun getDirsFromServer(): List<DirectoryDTO> {
        val directoryGetter = component!!.getDirectoryGetter()
        return directoryGetter.getDirs()
    }

    fun getMappedDirs(): MutableMap<DirectoryDTO, String> {
        val mapper = DirectoryMapper(File(mainActivity.filesDir.absolutePath + File.separator + CONFIG_NAME))
        return mapper.readDirectories()
    }

    fun getIdOfDir(dir: DirectoryDTO): Int {
        val keys = map?.keys
        if (keys != null) {
            return keys.indexOf(dir)
        }
        return -1;
    }

    @Synchronized
    override fun mapDirectory(dir: DirectoryDTO, path: String): Boolean {
        val file = File(path)
        if (file.exists() && file.isDirectory) {
            map!!.put(dir, path)
            val directoryMapper = DirectoryMapper(File(mainActivity.filesDir.absolutePath + File.separator + CONFIG_NAME))
            directoryMapper.addDirectory(dir,path)
            return true
        } else return false
    }

    @Synchronized
    override fun refreshDirectories() {
        map = null
        stopAllJobs()
        getDirectories()
        startAllJobs()
    }

    override fun startJobForDir(dir: DirectoryDTO) {
        val path = map?.get(dir)
        if (path != null) {
            val jobScheduler = mainActivity.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val componentName = ComponentName(mainActivity, DriveJob::class.java)
            val persistableBundle: PersistableBundle = PersistableBundle()

            persistableBundle.putString(DriveJob.DIR_SERVER_LABEL, dir.label)
            persistableBundle.putString(DriveJob.DIR_SERVER_PATH, dir.path)
            persistableBundle.putString(DriveJob.DIR_LOCAL_PATH, path)
            val build = JobInfo.Builder(getIdOfDir(dir), componentName).setPeriodic(900)
                 // .setMinimumLatency(324)
                    .setPersisted(true)
                    .setExtras(persistableBundle)
                    .build()
            val schedule = jobScheduler.schedule(build)
            if (schedule == JobScheduler.RESULT_SUCCESS) {
                Log.i("job", "Job scheduled!")
            } else {
                Log.i("job", "Job not scheduled")
            }
        }
    }

    override fun startJobOnlyOnce(dir: DirectoryDTO){
        val path = map?.get(dir)
        if (path != null) {

            val currentSsid = MainActivity.getCurrentSsid(mainActivity)?.toLowerCase()
            val contains = currentSsid?.contains("szyna")
            val test =     currentSsid?.contains("AndroidWifi")

            if ((contains != null && contains) || (test != null && test)) {
                val driveJob = DriveJob()
                val jobScheduler = mainActivity.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
                val componentName = ComponentName(mainActivity, DriveJob::class.java)
                val persistableBundle: Bundle = Bundle()

                persistableBundle.putString(DriveJob.DIR_SERVER_LABEL, dir.label)
                persistableBundle.putString(DriveJob.DIR_SERVER_PATH, dir.path)
                persistableBundle.putString(DriveJob.DIR_LOCAL_PATH, path)

                val intent = Intent(mainActivity, DriveIntentJob::class.java)
                intent.putExtras(persistableBundle)
                ContextCompat.startForegroundService(mainActivity, intent)
                DriveIntentJob.enqueue(this.mainActivity, intent, getIdOfDir(dir) or 4096)
                val t = true
            }
        }
    }

    override fun stopJobForDir(dir: DirectoryDTO) {

    }

    override fun stopAllJobs() {
        val jobScheduler = mainActivity.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.cancelAll()
    }

    override fun startAllJobs() {
        for(dir in map!!.keys){
            startJobForDir(dir)
        }
    }

    override fun jobsScheduled(): Int {
        val jobScheduler = mainActivity.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        if (jobScheduler != null){
            return jobScheduler.allPendingJobs.size
        }
        return 0
    }
}