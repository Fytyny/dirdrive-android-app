package org.fytyny.dirdriveandroid

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private val welcomeMessage: String = "Welcome to the TutorialsPoint.com"

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("org.fytyny.dirdriveandroid", appContext.packageName)
    }
}
