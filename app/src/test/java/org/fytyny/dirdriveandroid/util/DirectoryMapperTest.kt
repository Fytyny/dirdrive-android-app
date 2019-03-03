package org.fytyny.dirdriveandroid.util

import junit.framework.Assert
import org.fytyny.dirdrive.api.dto.DirectoryDTO
import org.junit.Test
import java.io.File

class DirectoryMapperIT {

    val file : File = File("conf.json")

    @Test
    fun addDirectoryToFile(){
        val mapper : DirectoryMapper = DirectoryMapper(file)
        val dir = DirectoryDTO()
        dir.label = "lalala"
        dir.path = "c:/lunyx"
        val dir2 = DirectoryDTO()
        dir2.label = "lalasfsala"
        dir2.path = "c:/ludasfsnyx"
        mapper.addDirectory(dir,"fssaf")
        mapper.addDirectory(dir2,"35435")

        val readDirectories = mapper.readDirectories()
        Assert.assertTrue(readDirectories.containsKey(dir))
        Assert.assertTrue(readDirectories.containsKey(dir2))

    }

}