package org.fytyny.dirdriveandroid.drive

import org.fytyny.dirdrive.api.dto.FileDTO
import java.io.File

interface DriveManager {
    fun saveFileInDrive(file : ByteArray) : Boolean
    fun getAllFiles() : List<FileDTO>
    fun removeFiles(fileList : List<FileDTO>) : Boolean
}