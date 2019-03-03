package org.fytyny.dirdriveandroid.client

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.fytyny.dirdrive.api.dto.DirectoryDTO
import org.fytyny.dirdriveandroid.annotation.Mockable
import org.fytyny.dirdriveandroid.model.JsoupRequest
import org.jsoup.Connection
import java.util.*
import javax.inject.Inject

@Mockable
class DirectoryGetterImpl @Inject constructor(): DirectoryGetter {
    @Inject
    lateinit var executor: HttpExecutor

    var mapper : ObjectMapper = ObjectMapper()

    override fun getDirs(): List<DirectoryDTO> {
        val body = getDirsFromServer().body()
        val readTree = mapper.readTree(body)
        val list : LinkedList<DirectoryDTO> = LinkedList()
        if (readTree.has("directoryList")){
            val dirList = readTree.get("directoryList").toList()
            for (node : JsonNode in dirList){
                val element = DirectoryDTO()
                element.label = node.get("label").textValue()
                element.path = node.get("path").textValue()
                list.add(element)
            }
        }
        return list
    }

    fun getDirsFromServer() : Connection.Response{
        val request : JsoupRequest = JsoupRequest()
        request.method = Connection.Method.GET
        return executor.connect("http://192.168.1.214:8080/DD/rest/dir/listAll",request)
    }

}