package org.fytyny.dirdriveandroid.service

import org.fytyny.dirdrive.api.dto.DirectoryDTO

interface MainActivityService {

    fun getDirectories() : Map<DirectoryDTO, String?>

    fun mapDirectory(dir : DirectoryDTO, path : String) : Boolean

    fun refreshDirectories()

    fun startJobForDir(dir : DirectoryDTO)

    fun stopJobForDir(dir : DirectoryDTO)

    fun stopAllJobs()

    fun startAllJobs()
    fun isConnected(): Boolean

    fun jobsScheduled(): Int
    fun startJobOnlyOnce(dir: DirectoryDTO)
}