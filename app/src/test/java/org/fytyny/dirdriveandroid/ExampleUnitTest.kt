package org.fytyny.dirdriveandroid

import org.fytyny.dirdrive.api.dto.FileDTO
import org.fytyny.dirdrive.api.dto.FileListResponseDTO
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*
import java.io.File
import java.util.*
import kotlin.collections.HashSet

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun fileDtoListTest(){
        val f : FileDTO = FileDTO()
        f.modifyDate = "sfse"
        f.name = "fsfs"

        val g : FileDTO = FileDTO("sf", "sfe")

        val result : FileListResponseDTO = FileListResponseDTO()
        result.fileDTOSet = HashSet(Arrays.asList(f,g))

        val result2 : FileListResponseDTO = FileListResponseDTO()
        result2.fileDTOSet = HashSet(Arrays.asList(f,g))

        Assert.assertTrue(result == result2)

        val result3 : FileListResponseDTO = FileListResponseDTO()
        result3.fileDTOSet = HashSet(Arrays.asList(g,f))

        Assert.assertTrue(result3 == result2)
    }
}
