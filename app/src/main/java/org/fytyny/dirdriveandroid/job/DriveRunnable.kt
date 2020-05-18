package org.fytyny.dirdriveandroid.job

import android.util.Log
import org.fytyny.dirdrive.api.dto.DirectoryDTO
import org.fytyny.dirdrive.api.dto.FileDTO
import org.fytyny.dirdrive.api.dto.FileRequest
import org.fytyny.dirdriveandroid.component.DaggerDefaultComponent
import org.fytyny.dirdriveandroid.component.DefaultComponent
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashSet

class DriveRunnable (val source : DirectoryDTO,  val destinationPath : String, val logging : DriveJobLogging) : Runnable {

    companion object {
        fun destinationFileDTOList(path : String) : List<FileDTO> {
            val linkedList = LinkedList<FileDTO>()

            val file = File(path)
            val list = file.listFiles()
            val dateTimeFormatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

            for (f : File in list){
                if (f.isFile){
                    val fileDTO = FileDTO()
                    fileDTO.name = f.name
                    fileDTO.modifyDate = dateTimeFormatter.format(Date(f.lastModified()))
                    linkedList.add(fileDTO)
                }
            }
            return linkedList
        }

        fun getFilesToRemove(source : List<FileDTO>, destination : List<FileDTO>) : Set<FileDTO>{
            val result : MutableSet<FileDTO> = HashSet()

            result.addAll(destination)
            result.removeAll(source)

            return result
        }

        fun getFilesToDownload(source : List<FileDTO>, destination : List<FileDTO>) : Set<FileDTO>{
            val result : MutableSet<FileDTO> = HashSet()
            result.addAll(source)
            result.removeAll(destination)
            return result
        }
    }

    var component : DefaultComponent? = null

    override fun run() {
        logging.log("Job started");
        component = DaggerDefaultComponent.create()
        val client = component!!.getClient()
        if (!client.establishConnection()){
            logging.log("Could not establish connection")
            return
        }
        val files = client.getFiles(source)
        var destination = destinationFileDTOList(destinationPath)
        val filesToRemove = getFilesToRemove(files, destination)
        logging.log("Removing " + filesToRemove.size);
        removeFiles(filesToRemove, destinationPath)

        destination = destinationFileDTOList(destinationPath)
        val filesToDownload = getFilesToDownload(files, destination)

        logging.log("Downloading " + filesToDownload.size + " in " + source.label);
        downloadFiles(filesToDownload, destinationPath)
        logging.log("Job finished");

    }

    private fun downloadFiles(filesToDownload: Set<FileDTO>, destinationPath: String) {

        val client = component!!.getClient()
        val dateTimeFormatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        var i : Int = 0
        for (fD : FileDTO in filesToDownload){
            logging.log("Downloading " + (++i) + "/" + filesToDownload.size + " in " + source.label);
            val fr = FileRequest()
            val file : File = File(destinationPath + File.separator + fD.name)
            fr.directoryDTO = source
            fr.fileDTO = fD
            val bodyBytes = client.getFile(fr)

            val fos = FileOutputStream(file)
            fos.write(bodyBytes)
            fos.flush()
            fos.close()
            val parse = dateTimeFormatter.parse(fD.modifyDate)
            file.setLastModified(parse.time)
        }
       }

    private fun removeFiles(filesToRemove: Set<FileDTO>, destinationPath: String) {
        for (f : FileDTO in filesToRemove){
            val file = File(destinationPath + File.separator + f.name)
            if (file.exists()){
                file.delete()
            }
        }
    }

}
