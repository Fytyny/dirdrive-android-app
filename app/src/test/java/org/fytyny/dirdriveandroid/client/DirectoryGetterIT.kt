package org.fytyny.dirdriveandroid.client

import org.fytyny.dirdriveandroid.component.DaggerDefaultComponent
import org.fytyny.dirdriveandroid.component.DefaultComponent
import org.junit.Before
import org.junit.Test

class DirectoryGetterIT {

    var coponent : DefaultComponent = DaggerDefaultComponent.create()

    @Before
    fun init(){

    }

    @Test
    fun get(){
        val directoryGetterImpl = coponent.getDirectoryGetter() as DirectoryGetterImpl
        val dirsFromServer = directoryGetterImpl.getDirsFromServer()
        println(dirsFromServer.body())
    }
}