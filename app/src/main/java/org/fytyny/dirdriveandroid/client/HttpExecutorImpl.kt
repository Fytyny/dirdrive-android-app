package org.fytyny.dirdriveandroid.client

import android.util.Log
import org.fytyny.dirdriveandroid.util.Session
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HttpExecutorImpl @Inject constructor() : HttpExecutor{

    companion object {
        const val X_SESSION_TOKEN = "x-session-token"
    }

    @Inject
    lateinit var sessionManager : SessionManager

    override @Synchronized
    fun connect(url: String, request: Connection.Request): Connection.Response {
        request.headers().put(X_SESSION_TOKEN, sessionManager.getSession()?.token)
        val executor = Executors.newSingleThreadExecutor()
        val c: Callable<Connection.Response> = object : Callable<Connection.Response> {
            override fun call(): Connection.Response {
                var execute = Jsoup.connect(url).ignoreContentType(request.ignoreContentType())
                        .maxBodySize(request.maxBodySize())
                        .timeout(request.timeout())
                        .headers(request.headers())
                        .cookies(request.cookies())
                if (request.method().equals(Connection.Method.POST)) {
                    execute = execute.method(request.method()).requestBody(request.requestBody())
                }
                return execute
                        .execute()
            }
        }
        Log.i(this.javaClass.name, "Started " + url)
        val submit = executor.submit(c)
   //     executor.shutdown();
 //       executor.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);

         val get = submit.get()
        Log.i(this.javaClass.name, "Finished " + url + " " + (get != null))
        return get;
    }
}