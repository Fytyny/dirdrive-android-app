package org.fytyny.dirdriveandroid.client

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.fytyny.dirdrive.api.dto.DirectoryDTO
import org.fytyny.dirdrive.api.dto.FileDTO
import org.fytyny.dirdrive.api.dto.FileListResponseDTO
import org.fytyny.dirdrive.api.dto.FileRequest
import org.fytyny.dirdriveandroid.annotation.Mockable
import org.fytyny.dirdriveandroid.model.JsoupRequest
import org.fytyny.dirdriveandroid.util.Session
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.util.*
import java.util.concurrent.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MediaType

@Mockable
class DirDriveClientImpl @Inject constructor() : DirDriveClient{

    @Inject
    lateinit var executor: HttpExecutor

    var mapper : ObjectMapper = ObjectMapper()

    override
    fun getFiles(directory: DirectoryDTO): List<FileDTO> {
        val body = getFilesFromServer(directory).body()
        val readTree = mapper.readTree(body)
        val list : LinkedList<FileDTO> = LinkedList()
        if (readTree.has("fileDTOSet")){
            val dirList = readTree.get("fileDTOSet").toList()
            for (node : JsonNode in dirList){
                val element = FileDTO()
                element.name = node.get("name").textValue()
                element.modifyDate = node.get("modifyDate").textValue()
                list.add(element)
            }
        }
        return list
    }

    override
    fun getFile(fileRequest: FileRequest): ByteArray {
        return getFileFromServer(fileRequest).bodyAsBytes()
    }

    override fun establishConnection(): Boolean {
        try{
            executor.connect("http://192.168.1.214:8080", JsoupRequest())
            return true
        }catch (e : ExecutionException){
            return false
        }
    }

    fun getFilesFromServer(directory: DirectoryDTO) : Connection.Response{
        val request : JsoupRequest = JsoupRequest()
        request.method = Connection.Method.POST
        request.requestBody = mapper.writeValueAsString(directory)
        request.headers.put(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON)
        return executor.connect("http://192.168.1.214:8080/DD/rest/dir/get",request)
    }

    fun getFileFromServer(file : FileRequest) : Connection.Response {
        val request : JsoupRequest = JsoupRequest()
        request.method = Connection.Method.POST
        request.requestBody = mapper.writeValueAsString(file)
        request.headers.put(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON)
        request.ignoreContentType = true
        return executor.connect("http://192.168.1.214:8080/DD/rest/dir/get/file",request)
    }

}
