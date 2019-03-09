package org.fytyny.dirdriveandroid

import android.Manifest
import android.R.attr.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import dagger.Component
import kotlinx.android.synthetic.main.activity_main.*
import org.fytyny.dirdriveandroid.util.DirectoryMapper
import java.io.File
import javax.inject.Inject
import android.content.Intent
import android.util.Log
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.os.Parcel
import android.support.v4.provider.DocumentFile
import android.view.View
import org.fytyny.dirdrive.api.dto.DirectoryDTO
import org.fytyny.dirdriveandroid.job.DriveJob
import org.fytyny.dirdriveandroid.service.MainActivityService
import org.fytyny.dirdriveandroid.service.MainActivityServiceImpl
import java.net.URI
import android.net.wifi.WifiInfo
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import org.jsoup.helper.StringUtil


class MainActivity : AppCompatActivity() {

    companion object {
        val REQUEST_WRITE_EXTERNAL_STORAGE = 2023
        val REQUEST_READ_EXTERNAL_STORAGE = 2022
        val REQUEST_IGNORE_BATTERY_OPT = 2020
        val REQUEST_INTERNET = 2021
        val WAKE_LOCK = 2019

        fun getCurrentSsid(context: Context): String? {
            var ssid: String? = null
            val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connManager.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnected) {
                val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val connectionInfo = wifiManager.connectionInfo
                if (connectionInfo != null && !StringUtil.isBlank(connectionInfo.ssid)) {
                    ssid = connectionInfo.ssid
                }
            }
            return ssid
        }
    }
    var activityService : MainActivityService? = null

    fun startService(){
        if (activityService == null){
            activityService = MainActivityServiceImpl(this)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val b1 = checkPermission(Manifest.permission.INTERNET, REQUEST_INTERNET)
        val b2 = checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_WRITE_EXTERNAL_STORAGE)
        val b3 = checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_READ_EXTERNAL_STORAGE)
        val b4 = checkPermission(Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, REQUEST_IGNORE_BATTERY_OPT)
        val b5 = checkPermission(Manifest.permission.WAKE_LOCK, WAKE_LOCK)


        if (b1 && b2 && b3){
            startService()
            if (activityService!!.isConnected()) {
                activityService!!.getDirectories()
                setNewTextSummary()
            } else {
                setNewTextError()
            }
        }

        fab.setOnClickListener { view ->
            if (activityService == null) startService()
            if (activityService!!.isConnected()) {
                val proto = Intent(this, ServerDirectoryChooserActivity::class.java)
                proto.putStringArrayListExtra(ServerDirectoryChooserActivity.SEREVER_LABELS, ArrayList(activityService!!.getDirectories().keys.map { it -> it.label }.toList()))
                startActivityForResult(proto, 9998)
            }
        }
        sync.setOnClickListener { view ->
            if (activityService == null) startService()
            if (activityService!!.isConnected()) {
                val proto = Intent(this, ServerDirectoryChooserActivity::class.java)
                proto.putStringArrayListExtra(ServerDirectoryChooserActivity.SEREVER_LABELS, ArrayList(activityService!!.getDirectories().filter { it -> it.value != null }.keys.map { it -> it.label }.toList()))
                startActivityForResult(proto, 9997)
            }
        }

    }

    private fun setNewTextError() {
        summary.text = "Failed to establish connection"
        val jobsScheduled = activityService!!.jobsScheduled()
        scheduled.text = "Jobs Scheduled: " + jobsScheduled.toString()
    }

    fun checkPermission(permission : String, requestCode: Int) : Boolean{
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,permission)) {
            } else {
                ActivityCompat.requestPermissions(this,arrayOf(permission),requestCode)
            }
            return false
        }
        return true
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    class Info @Inject constructor() {
        val text = "Hello Dagger 2"
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(data == null) return

        when (requestCode) {
            9999 -> {
                val newPath = data!!.getStringExtra(DirectoryChooserActivity.PATH_EXTRA)
                val dir = DirectoryDTO()
                dir.path = data!!.getStringExtra(DriveJob.DIR_SERVER_PATH)
                dir.label = data!!.getStringExtra(DriveJob.DIR_SERVER_LABEL)
                if (activityService == null){
                    startService()
                }
                activityService!!.mapDirectory(dir,newPath)
                activityService!!.startJobForDir(dir)
                setNewTextSummary()
            }

            9998 -> {
                val stringExtra = data!!.getStringExtra(ServerDirectoryChooserActivity.LABEL_RESULT)
                if (activityService == null){
                    startService()
                }
                val elementAt = activityService!!.getDirectories().keys.filter { it -> it.label.equals(stringExtra) }.get(0)
                val intent = Intent(this, DirectoryChooserActivity::class.java)
                intent.putExtra(DriveJob.DIR_SERVER_LABEL,elementAt.label)
                intent.putExtra(DriveJob.DIR_SERVER_PATH,elementAt.path)
                startActivityForResult(intent,9999)
            }

            9997 -> {
                val stringExtra = data!!.getStringExtra(ServerDirectoryChooserActivity.LABEL_RESULT)
                if (activityService == null){
                    startService()
                }
                val elementAt = activityService!!.getDirectories().keys.filter { it -> it.label.equals(stringExtra) }.get(0)
                if (elementAt != null){
                    activityService!!.startJobOnlyOnce(elementAt)
                }
            }
        }
    }

    fun refreshOnClick(v : View){
        if(activityService == null) startService()
        if (activityService!!.isConnected()) {
            activityService!!.refreshDirectories()
            setNewTextSummary()
        }else{
            setNewTextError()
        }
    }

    fun setNewTextSummary(){
        val directories = activityService!!.getDirectories()
        val mapped = activityService!!.getDirectories().filter { it -> it.value != null }.size
        summary.text = "Found: " + directories.keys.size + ", Mapped: " + mapped
        val jobsScheduled = activityService!!.jobsScheduled()
        scheduled.text = "Jobs Scheduled: " + jobsScheduled.toString()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_WRITE_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    println("what?")
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }


}