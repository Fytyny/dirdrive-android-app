package org.fytyny.dirdriveandroid.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.whenever
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.fytyny.dirdrive.api.dto.DirectoryDTO
import org.fytyny.dirdrive.api.dto.FileDTO
import org.jsoup.Connection
import org.junit.Test

import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class DirDriveClientTest {
    val responseBody = "{\"fileDTOSet\":[{\"name\":\"Monrroe - Distant Future (ft. LaMeduza)-o72461arebE.mp3\",\"modifyDate\":\"15-11-2018 15:22:43\"},{\"name\":\"CHVRCHES - Leave A Trace-4Eo84jDIMKI.mp3\",\"modifyDate\":\"25-10-2018 17:25:28\"},{\"name\":\"[MV] REOL - 宵々古今 _ YoiYoi Kokon-8IK6eLTNV1k.mp3\",\"modifyDate\":\"23-09-2018 11:05:29\"},{\"name\":\"Alan Walker - Darkside (feat. Au_Ra and Tomine Harket)-M-P4QBt-FWw.mp3\",\"modifyDate\":\"04-12-2018 07:46:29\"},{\"name\":\"Hybrid Minds - Halcyon (feat. Grimm)-S7de4JJVV8A.mp3\",\"modifyDate\":\"24-10-2018 11:31:11\"},{\"name\":\"CHVRCHES - My Enemy ft. Matt Berninger-fKuxh0E9mSI.mp3\",\"modifyDate\":\"28-02-2018 19:49:47\"},{\"name\":\"Star Wars the Old Republic Soundtrack - 01 Clash of Destiny-09XRcFv0O4M.mp3\",\"modifyDate\":\"29-03-2015 07:04:02\"},{\"name\":\"Kardinal Offishall - Dangerous ft. Akon-Ro7yHf_pU14.mp3\",\"modifyDate\":\"27-09-2018 12:23:11\"},{\"name\":\"ALEX & VLADI - SUPERMAN [Official HD Video]-Y2HUpy05YgU.mp3\",\"modifyDate\":\"27-10-2018 13:43:43\"},{\"name\":\"Avicii - Waiting For Love-cHHLHGNpCSA.mp3\",\"modifyDate\":\"25-09-2018 04:55:48\"},{\"name\":\"The Gallows Trailer Song - Smells Like Teen Spirit Cover - Official Music Video-4-nkRX4FQEc.mp3\",\"modifyDate\":\"27-10-2018 02:44:37\"},{\"name\":\"Urbandawn - Words To Say (Hybrid Minds Remix)-T78dYOhdj_U.mp3\",\"modifyDate\":\"31-08-2018 20:39:58\"},{\"name\":\"DESI SLAVA - MNOGO SAM DOBRA _ Деси Слава - Много съм добра, 2018-lONCf9SJAxk.mp3\",\"modifyDate\":\"28-10-2018 06:11:12\"},{\"name\":\"Hybrid Minds - Kismet ft. Riya-diliY4ERkLU.mp3\",\"modifyDate\":\"29-10-2018 05:40:37\"},{\"name\":\"GALENA - FENOMENALEN _ Галена - Феноменален, 2018-FeCIwlbFKoc.mp3\",\"modifyDate\":\"14-11-2018 16:34:45\"},{\"name\":\"Numb (Official Video) - Linkin Park-kXYiU_JCYtU.mp3\",\"modifyDate\":\"04-12-2018 01:14:35\"},{\"name\":\"Position Music - Neptune (2WEI - Epic Intense Emotional Trailer)-goeFV0M5Jig.mp3\",\"modifyDate\":\"04-12-2018 19:24:50\"},{\"name\":\"Daughter - Youth (Hybrid Minds Bootleg)-8RUWkv4Ray8.mp3\",\"modifyDate\":\"26-10-2018 03:21:39\"},{\"name\":\"Alan Walker - Faded (Restrung)-bDmzGLrdjxQ.mp3\",\"modifyDate\":\"24-10-2018 01:16:08\"},{\"name\":\"K_DA - POP_STARS (ft Madison Beer, (G)I-DLE, Jaira Burns) _ Official Music Video - League of Legends-UOxkGD8qRB4.mp3\",\"modifyDate\":\"06-11-2018 22:19:15\"},{\"name\":\"OneRepublic - Counting Stars-hT_nvWreIhg.mp3\",\"modifyDate\":\"05-12-2018 02:17:03\"},{\"name\":\"CHVRCHES - Warning Call (Lyric Video)-fB4gjiMVKFI.mp3\",\"modifyDate\":\"06-12-2018 22:40:53\"},{\"name\":\"Day of Fate ~Spirit VS Spirit~ Lyric Video (Unmei No Hi English Cover) _ Team Four Star-9OZ-yNdKw3o.mp3\",\"modifyDate\":\"04-09-2018 07:50:58\"},{\"name\":\"Paktofonika - '30.09.2000'-YqOqlLftizk.mp3\",\"modifyDate\":\"29-03-2015 17:15:03\"},{\"name\":\"Jay-Z feat. Alicia Keys - Empire State Of Mind Official Video-vz2fsJ0CyaU.mp3\",\"modifyDate\":\"12-12-2018 15:24:35\"},{\"name\":\"Imagine Dragons - Whatever It Takes (Official Music Video)-gOsM-DYAEhY.mp3\",\"modifyDate\":\"27-09-2018 19:21:42\"},{\"name\":\"Anna Yvette - Shooting Star-ZXgHcdLM7lM.mp3\",\"modifyDate\":\"31-10-2018 16:18:33\"},{\"name\":\"Timbaland - Apologize ft. OneRepublic (Official Music Video)-ZSM3w1v-A_Y.mp3\",\"modifyDate\":\"28-10-2018 00:12:19\"},{\"name\":\"ALISIA - Zapali _ АЛИСИЯ - Запали-xEcbqJ0cJyQ.mp3\",\"modifyDate\":\"15-09-2018 20:27:35\"},{\"name\":\"Eminem - Venom-8CdcCD5V-d8.mp3\",\"modifyDate\":\"04-12-2018 03:35:02\"},{\"name\":\"Whiney x Urbandawn - Loki-uwr9HrF4xuE.mp3\",\"modifyDate\":\"14-11-2018 16:42:49\"},{\"name\":\"CHVRCHES - Never Say Die (Audio)-ifr3O33UpWs.mp3\",\"modifyDate\":\"23-10-2018 19:57:20\"},{\"name\":\"2WEI - Survivor (Epic Cover - 'Tomb Raider - Trailer 2 Music')-JGuWb_81als.mp3\",\"modifyDate\":\"25-09-2018 22:36:48\"},{\"name\":\"PRESLAVA - NYAMA DA TI PISHA _ Преслава - Няма да ти пиша, 2016-bE1q_zRpOII.mp3\",\"modifyDate\":\"30-10-2018 14:33:08\"},{\"name\":\"ROKSANA ft. SASHO ZHOKERA - KESH, KESH _ Роксана ft. Сашо Жокера - Кеш, кеш, 2017-jeNpHt3h1xk.mp3\",\"modifyDate\":\"15-02-2018 10:57:00\"},{\"name\":\"Kylie Minogue - Love At First Sight-wf421JsG004.mp3\",\"modifyDate\":\"21-10-2018 02:06:12\"},{\"name\":\"Timbaland - The Way I Are ft. Keri Hilson, D.O.E., Sebastian (Official Music Video)-U5rLz5AZBIA.mp3\",\"modifyDate\":\"20-10-2018 09:07:38\"},{\"name\":\"PRESLAVA - MOETO SLABO MYASTO _ Преслава - Моето слабо място, slideshow 2014-WdM22pzqaac.mp3\",\"modifyDate\":\"21-10-2018 06:13:58\"},{\"name\":\"Oxon - HipoPato cypher + Bazi, Delekta, Penx, Kojot, Zygson, Eripe (prod. TMK Beatz) (OneTake #9)-ecc_G1W8VvU.mp3\",\"modifyDate\":\"07-11-2018 09:54:55\"},{\"name\":\"desktop.ini\",\"modifyDate\":\"16-12-2018 16:46:27\"},{\"name\":\"Bert H & Edlan - Intercept-PGuxpIo47Pg.mp3\",\"modifyDate\":\"07-08-2018 21:14:39\"},{\"name\":\"KONSTANTIN  - STUDENTKA _ Константин - Студентка, 2014-40sfmLrnVYc.mp3\",\"modifyDate\":\"28-10-2018 23:41:57\"}]}"

    @Mock
    var dirDriveClient: DirDriveClientImpl? = null

    @Mock
    var getter : DirDriveClientImpl? = null

    @Mock
    var response : Connection.Response? = null

    @Before
    fun init(){
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(response?.body()).thenReturn(responseBody)
        Mockito.`when`(getter?.getFilesFromServer(any())).thenReturn(response)
        Mockito.`when`(getter?.getFiles(any())).then(Mockito.CALLS_REAL_METHODS)
        doAnswer { return@doAnswer Mockito.CALLS_REAL_METHODS}.`when`(getter)?.mapper = com.nhaarman.mockito_kotlin.any()
        whenever(getter?.mapper).thenReturn(ObjectMapper())
        getter?.mapper = ObjectMapper()
    }

    @Test
    fun shouldReturnFile(){
        val dir : DirectoryDTO = DirectoryDTO()
        dir.label = "MUSIC"
        dir.path = "E:\\Muzyka\\Yt-Music"
        val files = getter?.getFiles(dir)

        val fileDtoToBeInList = FileDTO()
        fileDtoToBeInList.name = "Monrroe - Distant Future (ft. LaMeduza)-o72461arebE.mp3"
        fileDtoToBeInList.modifyDate = "15-11-2018 15:22:43"
        assertTrue(files!!.contains(fileDtoToBeInList))
    }
    @Test
    fun shouldReturnMoreThanOneFile(){
        val dir : DirectoryDTO = DirectoryDTO()
        dir.label = "MUSIC"
        dir.path = "E:\\Muzyka\\Yt-Music"
        val files = getter?.getFiles(dir)
        assertTrue(files!!.size > 1)
    }
    @Test
    fun shouldNotContainNotExistent(){
        val dir : DirectoryDTO = DirectoryDTO()
        dir.label = "MUSIC"
        dir.path = "E:\\Muzyka\\Yt-Music"
        val files = getter?.getFiles(dir)
        val fileDtoToBeInList = FileDTO()
        fileDtoToBeInList.name = "NOT!!Monrroe - Distant Future (ft. LaMeduza)-o72461arebE.mp3"
        fileDtoToBeInList.modifyDate = "15-11-2018 15:22:43"
        assertFalse(files!!.contains(fileDtoToBeInList))    }
}