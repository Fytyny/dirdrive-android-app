package org.fytyny.dirdriveandroid.service

import com.nhaarman.mockito_kotlin.any
import junit.framework.Assert
import org.apache.commons.lang3.RandomStringUtils
import org.fytyny.dirdrive.api.dto.DirectoryDTO
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class MainActivityServiceTest {

    @Mock
    val mas : MainActivityServiceImpl? = null

    var randomList = randomList()

    @Before
    fun init(){
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(mas?.getDirsFromServer()).thenReturn(randomList)
        Mockito.`when`(mas?.getMappedDirs()).thenReturn(mapFromList(randomList))
        Mockito.`when`(mas?.getDirectories()).thenAnswer(Mockito.CALLS_REAL_METHODS)
        Mockito.`when`(mas?.getIdOfDir(any())).thenAnswer(Mockito.CALLS_REAL_METHODS)
    }

    @Test
    fun makeMapTest(){
        Assert.assertFalse(mas?.getDirectories()!!.isEmpty())
    }

    @Test
    fun getIdOfDirTest(){
        mas!!.getDirectories()
        val map : MutableMap<DirectoryDTO, Int> = HashMap()
        for (dir in randomList){
            map.put(dir,mas!!.getIdOfDir(dir))
        }
        for (a in 1..10) {
            shuffleRandomList()
            Mockito.`when`(mas?.getMappedDirs()).thenReturn(mapFromList(randomList))
            mas!!.getDirectories()
            for (dir in randomList) {
                Assert.assertEquals(map.get(dir), mas!!.getIdOfDir(dir))
            }
        }

    }

    private fun shuffleRandomList() {
        val rand : Random = Random()
        val set : MutableSet<Int> = HashSet()
        for (i in 0..randomList.size-1){
            set.add(i)
        }
        val result : MutableList<DirectoryDTO> = LinkedList()
        for (i in 0..randomList.size-1){
            val r = rand.nextInt(set.size);
            result.add(randomList.get(set.elementAt(r)))
            set.remove(set.elementAt(r))
        }
        randomList = result
    }

    fun randomDir() : DirectoryDTO{
        val dir = DirectoryDTO()
        dir.label = RandomStringUtils.randomAlphabetic(10)
        dir.path = RandomStringUtils.randomAlphabetic(10)
        return dir
    }

    fun randomList() : List<DirectoryDTO>{
        val linkedList = LinkedList<DirectoryDTO>()
        val rand : Random = Random()
        val i = rand.nextInt(5) + 5
        for (j in 1..i){
            linkedList.add(randomDir())
        }
        return linkedList
    }

    fun mapFromList(randomList: List<DirectoryDTO>) : MutableMap<DirectoryDTO,String>{
        val map = HashMap<DirectoryDTO,String>()
        for (i in 0..randomList.size -1){
            map.put(randomList.get(i), RandomStringUtils.randomAlphabetic(15))
        }
        return map
    }

}