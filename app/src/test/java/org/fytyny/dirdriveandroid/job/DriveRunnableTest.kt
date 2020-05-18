package org.fytyny.dirdriveandroid.job

import org.fytyny.dirdrive.api.dto.DirectoryDTO
import org.fytyny.dirdrive.api.dto.FileDTO
import org.junit.Assert
import org.junit.Test
import java.io.File
import java.util.logging.Logger

class DriveRunnableTest {

    @Test
    fun testListFilesToDTO(){
        val file = File( File("src").absolutePath).parentFile
        Assert.assertTrue(file.exists())
        val sourceFileDTOList = DriveRunnable.destinationFileDTOList(file.absolutePath)
        println(sourceFileDTOList)
        val fileDtoToBeInList = FileDTO()
        fileDtoToBeInList.name = "Monrroe - Distant Future (ft. LaMeduza)-o72461arebE.mp3"
        fileDtoToBeInList.modifyDate = "15-11-2018 15:22:43"
        Assert.assertTrue(sourceFileDTOList.contains(fileDtoToBeInList))
    }

    @Test
    fun makeFolderDup(){
        val dir : DirectoryDTO = DirectoryDTO()
        dir.label = "MUSIC"
        dir.path = "E:\\Muzyka\\Yt-Music"
        val file = File("D:\\windows 10 new\\Pulpit\\afsef")

        val drive : DriveRunnable = DriveRunnable(dir, file.absolutePath, LogDriveJobLogging())
        drive.run()
    }
}