package org.fytyny.dirdriveandroid.client

import org.fytyny.dirdrive.api.dto.DirectoryDTO
import org.fytyny.dirdrive.api.dto.FileDTO
import org.fytyny.dirdrive.api.dto.FileListResponseDTO
import org.fytyny.dirdrive.api.dto.FileRequest
import org.fytyny.dirdriveandroid.util.Session

interface DirDriveClient {
    fun getFiles(directory: DirectoryDTO): List<FileDTO>
    fun getFile(file: FileRequest): ByteArray
    fun establishConnection() : Boolean
}