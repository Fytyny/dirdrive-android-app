package org.fytyny.dirdriveandroid.util

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.ObjectCodec
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.fytyny.dirdrive.api.dto.DirectoryDTO
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.Serializable
import java.util.*

class DirectoryMapper constructor(val file: File) {

    init {
        if (!file.exists()) {
            file.createNewFile()
        }
    }

    val mapper: ObjectMapper = ObjectMapper()

    fun getDirMap(): Map<DirectoryDTO, String> {
        val map: HashMap<DirectoryDTO, String> = HashMap()
        if (file.exists()) {
            map.putAll(readDirectories())
        }
        return map
    }

    fun addDirectory(dir: DirectoryDTO, path: String) {
        val readDirectories = readDirectories()
        readDirectories.put(dir, path)
        val list : MutableList<DirectoryConfig> = LinkedList()
        for ((k, v) in readDirectories) {
            list.add(DirectoryConfig.fromDirectoryDTO(v,k))
        }
        val fileWriter: FileWriter = FileWriter(file, false)
        fileWriter.write(mapper.writeValueAsString(list))

        fileWriter.flush()
        fileWriter.close()
    }

    fun readDirectories(): MutableMap<DirectoryDTO, String> {
        val fileReader: FileReader = FileReader(file)
        val result: MutableMap<DirectoryDTO, String> = HashMap()

        try {
            val readTree = mapper.readTree(fileReader)
            if (readTree.size() > 0){
                for (js : JsonNode in readTree){
                    val dto : DirectoryDTO = DirectoryDTO()
                    dto.path = js.get("serverPath").textValue()
                    dto.label = js.get("label").textValue()
                    result.put(dto, js.get("path").textValue())
                }
            }
        } catch (e: JsonMappingException) {
        }
        fileReader.close()
        return result
    }

}

data class DirectoriesList constructor(val list : List<DirectoryConfig>): Serializable{
}

data class DirectoryConfig constructor(val label : String, val serverPath : String, val path : String) : Serializable {
    companion object {
        fun fromDirectoryDTO(path : String, dto : DirectoryDTO) : DirectoryConfig{
            return DirectoryConfig(dto.label,dto.path,path)
        }
    }
}