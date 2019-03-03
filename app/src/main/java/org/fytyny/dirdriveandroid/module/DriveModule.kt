package org.fytyny.dirdriveandroid.module

import dagger.Module
import dagger.Provides
import org.fytyny.dirdriveandroid.client.*
import javax.inject.Inject
import javax.inject.Singleton

@Module
class DriveModule{

    @Provides
    @Singleton
    fun getClient(impl: DirDriveClientImpl) : DirDriveClient{
        return impl
    }
    @Provides
    @Singleton
    fun getDirectoryGetter(impl: DirectoryGetterImpl) : DirectoryGetter{
        return impl
    }
    @Provides
    @Singleton
    fun getSessionManager(impl: SessionManagerImpl) : SessionManager{
        return impl
    }

    @Provides
    @Singleton
    fun getExecutor(impl: HttpExecutorImpl) : HttpExecutor{
        return impl
    }

}