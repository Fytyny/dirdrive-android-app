package org.fytyny.dirdriveandroid.client

import org.fytyny.dirdrive.api.dto.DirectoryDTO

interface DirectoryGetter {
    fun getDirs() : List<DirectoryDTO>
}