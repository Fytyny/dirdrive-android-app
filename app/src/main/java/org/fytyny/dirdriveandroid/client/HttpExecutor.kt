package org.fytyny.dirdriveandroid.client

import org.fytyny.dirdriveandroid.util.Session
import org.jsoup.Connection

interface HttpExecutor {
    fun connect(url: String, request: Connection.Request): Connection.Response
}