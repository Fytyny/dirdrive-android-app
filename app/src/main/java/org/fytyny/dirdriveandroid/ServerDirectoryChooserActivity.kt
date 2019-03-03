package org.fytyny.dirdriveandroid

import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import java.util.*

class ServerDirectoryChooserActivity : Activity() {

    companion object {
        val SEREVER_LABELS : String = "server_labels"
        val LABEL_RESULT : String = "label_result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server_directory_chooser)

        val listView : ListView = this.findViewById(R.id.chooserListView)
        val list : ArrayList<String> = intent.getStringArrayListExtra(SEREVER_LABELS)
        listView.adapter = ArrayAdapter<String>(this, R.layout.simple_text_layout_1, list)
        listView.onItemClickListener = AdapterView.OnItemClickListener(
                fun (adapterView: AdapterView<*>, view : View, i : Int, l : Long){
                    val get = list.get(i)
                    val result = Intent()
                    val bundle = Bundle()
                    bundle.putString(LABEL_RESULT, get)
                    result.putExtras(bundle)
                    setResult(Activity.RESULT_OK,result)
                    finish()
                }
        )
    }
}
