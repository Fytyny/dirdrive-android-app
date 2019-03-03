package org.fytyny.dirdriveandroid.client

import org.fytyny.dirdrive.api.dto.DirectoryDTO
import org.fytyny.dirdrive.api.dto.FileDTO
import org.fytyny.dirdrive.api.dto.FileRequest
import org.fytyny.dirdriveandroid.component.DaggerDefaultComponent
import org.fytyny.dirdriveandroid.component.DefaultComponent
import org.junit.Test
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class DirDriveClientIT {

    var component : DefaultComponent = DaggerDefaultComponent.create()

    @Test
    fun init(){}

    @Test
    fun getListForDir(){
        val dir : DirectoryDTO = DirectoryDTO()
        dir.label = "MUSIC"
        dir.path = "E:\\Muzyka\\Yt-Music"
        val dirDriveClientImpl = component.getClient() as DirDriveClientImpl
        println(dirDriveClientImpl.getFilesFromServer(dir).body())
    }

    @Test
    fun getFile(){
        val client = component.getClient() as DirDriveClientImpl
        val file : FileDTO = FileDTO()
        file.name = "Monrroe - Distant Future (ft. LaMeduza)-o72461arebE.mp3"
        file.modifyDate = "15-11-2018 15:22:43"

        val dir : DirectoryDTO = DirectoryDTO()
        dir.label = "MUSIC"
        dir.path = "E:\\Muzyka\\Yt-Music"

        val fr : FileRequest = FileRequest()
        fr.directoryDTO = dir
        fr.fileDTO = file
        val fileFromServer = client.getFileFromServer(fr)

        val file1 = File(file.name)
        val fos : FileOutputStream = FileOutputStream(file1)
        fos.write(fileFromServer.bodyAsBytes())
        fos.flush()
        fos.close()
        val dateTimeFormatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

        val parse = dateTimeFormatter.parse(file.modifyDate)
        file1.setLastModified(parse.time)


    }


}