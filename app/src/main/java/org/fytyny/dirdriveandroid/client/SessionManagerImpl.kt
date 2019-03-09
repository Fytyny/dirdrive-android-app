package org.fytyny.dirdriveandroid.client

import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import org.fytyny.dirdriveandroid.annotation.Mockable
import org.fytyny.dirdriveandroid.model.JsoupRequest
import org.fytyny.dirdriveandroid.util.Session
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Mockable
class SessionManagerImpl @Inject constructor() : SessionManager {

    companion object {
        val X_API_KEY : String = "X-api-key"
    }

    private var session : Session? = null
    var mapper : ObjectMapper = ObjectMapper()

    override fun getSession(): Session? {
        if (session != null) return session
        val url : String = "http://192.168.1.214:8080/DD/rest/session/create";
        val request: JsoupRequest = JsoupRequest();
        request.method = Connection.Method.GET;
        request.headers = HashMap()
        request.headers.put(X_API_KEY,"superapikey")
        request.timeout = 2000
        val response = getSession(url, request)
        val readTree = mapper.readTree(response.body())
        if (readTree.has("token")){
            session = Session(readTree.get("token").asText(), true)
            return session
        } else return null
    }

    fun getSession(url: String, request: Connection.Request): Connection.Response {
        val executor = Executors.newSingleThreadExecutor()
        val c: Callable<Connection.Response> = object : Callable<Connection.Response> {
            override fun call(): Connection.Response {
                var execute = Jsoup.connect(url).ignoreContentType(request.ignoreContentType())
                        .maxBodySize(request.maxBodySize())
                        .timeout(request.timeout())
                        .headers(request.headers())
                        .cookies(request.cookies())
                if (request.method().equals(Connection.Method.POST)) {
                    execute = execute.requestBody(request.requestBody())
                }
                return execute
                        .execute()
            }
        }
        val submit = executor.submit(c)
        Log.i(this.javaClass.name, "Started " + url)

     //   executor.shutdown();
    //    executor.awaitTermination(Long.MAX_VALUE, TimeUnit.HOUR S);
        val get = submit.get()
        Log.i(this.javaClass.name, "Finished " + url + " " + (get != null))

        return get;
    }
}