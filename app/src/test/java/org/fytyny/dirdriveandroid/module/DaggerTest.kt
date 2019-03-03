package org.fytyny.dirdriveandroid.module

import junit.framework.Assert.assertTrue
import org.fytyny.dirdriveandroid.component.DaggerDefaultComponent
import org.fytyny.dirdriveandroid.component.DefaultComponent
import org.junit.Test

class DaggerTest {

    @Test
    fun daggerInjectionTest(){
        val component : DefaultComponent = DaggerDefaultComponent.create()
        val client = component.getClient()
        val directoryGetter = component.getDirectoryGetter()
        assertTrue(client === directoryGetter)
    }
}