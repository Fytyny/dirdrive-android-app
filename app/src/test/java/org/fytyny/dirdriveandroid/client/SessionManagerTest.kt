package org.fytyny.dirdriveandroid.client

import com.fasterxml.jackson.databind.ObjectMapper
import junit.framework.Assert
import junit.framework.Assert.assertTrue
import org.apache.commons.lang3.RandomStringUtils
import org.fytyny.dirdriveandroid.model.JsoupRequest
import org.jsoup.Connection
import org.junit.Before
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.*
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer

class SessionManagerTest {

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    var sessionManager : SessionManagerImpl? = null

    @Mock
    var respone : Connection.Response? = null

    @Before
    fun init(){
        MockitoAnnotations.initMocks(this)
        `when`(respone?.body()).thenReturn(getRandomToken())
        val request: JsoupRequest = JsoupRequest();
        request.method = Connection.Method.GET;
        request.headers = HashMap()
        request.headers.put(SessionManagerImpl.X_API_KEY,"superapikey")
        Mockito.doAnswer(object : Answer<Connection.Response?> {
            override fun answer(invocation: InvocationOnMock?): Connection.Response? {
                return respone
            }}
        ).`when`(sessionManager)?.getSession(Matchers.anyString(), safeEq(request))
        sessionManager?.mapper = ObjectMapper()
    }

    @Test
    fun testSave(){
        var session = sessionManager?.getSession()
        var token : String = session!!.token;
        session = sessionManager?.getSession()
        Assert.assertNotNull(session)
        Assert.assertEquals(token,session?.token)
    }

    fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    fun <T> uninitialized(): T = null as T
    fun <T : Any> safeEq(value: T): T = eq(value) ?: value

    fun getRandomToken() : String{
        return "{\n" +
                "\"token\": \""+ RandomStringUtils.randomAlphabetic(20)+"\"\n" +
                "}"
    }
}
