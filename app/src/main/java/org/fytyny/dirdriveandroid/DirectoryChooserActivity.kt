package org.fytyny.dirdriveandroid

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.provider.DocumentFile
import android.util.Log
import org.fytyny.dirdriveandroid.job.DriveJob
import java.io.File

class DirectoryChooserActivity : AppCompatActivity() {
    companion object {
        val PATH_EXTRA : String = "pathExtra"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val i = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        i.addCategory(Intent.CATEGORY_DEFAULT)
        val createChooser = Intent.createChooser(i, "Choose directory")
        startActivityForResult(createChooser, 9999)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            9999 -> {
                val data1 = data?.getData()
                DocumentFile.fromTreeUri(this, data1).isDirectory
                val treePrimary = Environment.getExternalStorageDirectory().path;
                val newPath = data1!!.path!!.replace("/tree/primary:", treePrimary + File.separator)

                Log.i("Test", "Result URI " + newPath)
                val result: Intent = Intent()
                val bundle = Bundle()
                bundle.putString(PATH_EXTRA, newPath)
                bundle.putString(DriveJob.DIR_SERVER_PATH,intent.getStringExtra(DriveJob.DIR_SERVER_PATH))
                bundle.putString(DriveJob.DIR_SERVER_LABEL,intent.getStringExtra(DriveJob.DIR_SERVER_LABEL))

                result.putExtras(bundle)
                setResult(Activity.RESULT_OK, result)
                finish()
            }
        }
    }
}
