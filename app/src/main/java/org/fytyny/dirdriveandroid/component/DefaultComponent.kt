package org.fytyny.dirdriveandroid.component

import dagger.Component
import org.fytyny.dirdriveandroid.client.*
import org.fytyny.dirdriveandroid.module.DriveModule
import javax.inject.Singleton

@Component(modules = [DriveModule::class])
@Singleton
interface DefaultComponent {
    fun inject(sessionManager : SessionManagerImpl)
    fun getClient() : DirDriveClient
    fun getDirectoryGetter() : DirectoryGetter
}