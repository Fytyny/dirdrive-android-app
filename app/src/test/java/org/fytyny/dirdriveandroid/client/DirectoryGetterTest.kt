package org.fytyny.dirdriveandroid.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.whenever
import junit.framework.Assert
import org.fytyny.dirdrive.api.dto.DirectoryDTO
import org.jsoup.Connection
import org.junit.Before
import org.junit.Test
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

class DirectoryGetterTest {

    val responseBody : String = "{\"directoryList\":[{\"label\":\"MUSIC\",\"path\":\"E:\\\\Muzyka\\\\Yt-Music\"}]}"

    @Mock
    var getter : DirectoryGetterImpl? = null

    @Mock
    var response : Connection.Response? = null

    @Before
    fun init(){
        MockitoAnnotations.initMocks(this)
        `when`(response?.body()).thenReturn(responseBody)
        `when`(getter?.getDirsFromServer()).thenReturn(response)
        `when`(getter?.getDirs()).then(Mockito.CALLS_REAL_METHODS)
        doAnswer { return@doAnswer Mockito.CALLS_REAL_METHODS}.`when`(getter)?.mapper = any()
        whenever(getter?.mapper).thenReturn(ObjectMapper())
        getter?.mapper = ObjectMapper()
    }

    @Test
    fun shouldReturnDirectoryList(){
        val dirs = getter?.getDirs()
        val dirToBeInList : DirectoryDTO = DirectoryDTO()
        dirToBeInList.label = "MUSIC"
        dirToBeInList.path = "E:\\Muzyka\\Yt-Music"
        Assert.assertTrue(dirs!!.contains(dirToBeInList))
    }
}
