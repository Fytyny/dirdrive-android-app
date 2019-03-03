package org.fytyny.dirdriveandroid.model

import org.jsoup.Connection
import org.jsoup.parser.Parser
import java.net.Proxy
import java.net.URL
import javax.net.ssl.SSLSocketFactory

class JsoupRequest : Connection.Request {

    var ignoreContentType : Boolean = true
    var followRedirects : Boolean = false
    var timeout : Int = Int.MAX_VALUE
    var method : Connection.Method = Connection.Method.GET;
    var headers : MutableMap<String, String> = HashMap()
    var cookies : MutableMap<String, String> = HashMap()
    var maxBodySize : Int = Int.MAX_VALUE;
    var requestBody : String? = ""

    override fun ignoreContentType(): Boolean {
        return ignoreContentType
    }

    override fun ignoreContentType(ignoreContentType: Boolean): Connection.Request {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun postDataCharset(charset: String?): Connection.Request {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun postDataCharset(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun timeout(): Int {
        return timeout
    }

    override fun timeout(millis: Int): Connection.Request {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun followRedirects(): Boolean {
        return followRedirects
    }

    override fun followRedirects(followRedirects: Boolean): Connection.Request {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeHeader(name: String?): Connection.Request {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun url(): URL {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun url(url: URL?): Connection.Request {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hasCookie(name: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun method(): Connection.Method {
        return this.method
    }

    override fun method(method: Connection.Method?): Connection.Request {
       if (method != null)  this.method = method
        return this
    }

    override fun proxy(): Proxy {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun proxy(proxy: Proxy?): Connection.Request {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun proxy(host: String?, port: Int): Connection.Request {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sslSocketFactory(): SSLSocketFactory {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sslSocketFactory(sslSocketFactory: SSLSocketFactory?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hasHeaderWithValue(name: String?, value: String?): Boolean {
        if (headers.containsKey(name)){
            return headers.get(name).equals(value)
        }
        return false
    }

    override fun hasHeader(name: String?): Boolean {
        return this.headers.containsKey(name)
    }

    override fun validateTLSCertificates(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun validateTLSCertificates(value: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addHeader(name: String?, value: String?): Connection.Request {
        this.headers.put(name!!,value!!)
        return this
    }

    override fun requestBody(body: String?): Connection.Request {
        this.requestBody = body
        return this
    }

    override fun requestBody(): String {
        return this.requestBody!!;
    }

    override fun cookie(name: String?): String {
        return cookies.get(name)!!
    }

    override fun cookie(name: String?, value: String?): Connection.Request {
        this.cookies.put(name!!,value!!)
        return this
    }

    override fun maxBodySize(): Int {
        return maxBodySize
    }

    override fun maxBodySize(bytes: Int): Connection.Request {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun header(name: String?): String {
        return headers.get(name)!!
    }

    override fun header(name: String?, value: String?): Connection.Request {
        return addHeader(name,value)
    }

    override fun ignoreHttpErrors(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun ignoreHttpErrors(ignoreHttpErrors: Boolean): Connection.Request {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cookies(): MutableMap<String, String> {
        return cookies
    }

    override fun data(keyval: Connection.KeyVal?): Connection.Request {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun data(): MutableCollection<Connection.KeyVal> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeCookie(name: String?): Connection.Request {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun headers(name: String?): MutableList<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun headers(): MutableMap<String, String> {
        return headers
    }

    override fun multiHeaders(): MutableMap<String, MutableList<String>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun parser(parser: Parser?): Connection.Request {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun parser(): Parser {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JsoupRequest

        if (ignoreContentType != other.ignoreContentType) return false
        if (followRedirects != other.followRedirects) return false
        if (timeout != other.timeout) return false
        if (method != other.method) return false
        if (headers != other.headers) return false
        if (cookies != other.cookies) return false
        if (maxBodySize != other.maxBodySize) return false
        if (requestBody != other.requestBody) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ignoreContentType.hashCode()
        result = 31 * result + followRedirects.hashCode()
        result = 31 * result + timeout
        result = 31 * result + method.hashCode()
        result = 31 * result + headers.hashCode()
        result = 31 * result + cookies.hashCode()
        result = 31 * result + maxBodySize
        result = 31 * result + (requestBody?.hashCode() ?: 0)
        return result
    }


}