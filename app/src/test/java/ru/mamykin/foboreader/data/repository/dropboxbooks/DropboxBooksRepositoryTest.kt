package ru.mamykin.foboreader.data.repository.dropboxbooks

import com.dropbox.core.v2.files.ListFolderResult
import com.dropbox.core.v2.users.FullAccount
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import ru.mamykin.foboreader.data.repository.dropboxbooks.DropboxBooksRepository
import ru.mamykin.foboreader.data.repository.dropboxbooks.DropboxBooksStorage
import ru.mamykin.foboreader.data.repository.dropboxbooks.DropboxClientFactory
import ru.mamykin.foboreader.entity.DropboxFile
import ru.mamykin.foboreader.entity.mapper.FolderToFilesListMapper

class DropboxBooksRepositoryTest {

    @Mock
    lateinit var clientFactory: DropboxClientFactory
    @Mock
    lateinit var storage: DropboxBooksStorage
    @Mock
    lateinit var mapper: FolderToFilesListMapper
    @Mock
    lateinit var mockFolder: ListFolderResult
    @Mock
    lateinit var mockFile: DropboxFile

    lateinit var mockFiles: List<DropboxFile>

    val authToken = "S1828HD9J"

    lateinit var repository: DropboxBooksRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mockFiles = listOf(mockFile, mockFile)
        repository = DropboxBooksRepository(clientFactory, storage, mapper)
    }

    @Test
    fun initDropbox_returnsError_whenAuthTokenEmpty() {
        whenever(storage.authToken).thenReturn(null)

//        val testSubscriber = repository.initDropbox().test()
//
//        testSubscriber.assertNotCompleted().assertError(UserNotAuthorizedException::class.java)
    }

    @Test
    fun initDropbox_callsInitClient_whenAuthTokenNotEmpty() {
        whenever(storage.authToken).thenReturn(authToken)

//        val testSubscriber = repository.initDropbox().test()
//
//        verify(clientFactory).init(authToken)
//        testSubscriber.assertCompleted().assertNoErrors()
    }

    @Test
    @Ignore
    fun getFiles_returnsFolderFiles() {
        val directory = "/books"
        whenever(clientFactory.getClient().files().listFolder(directory)).thenReturn(mockFolder)
        whenever(mapper.transform(mockFolder)).thenReturn(mockFiles)

        val testSubscriber = repository.getFiles(directory).test()

        testSubscriber.assertCompleted().assertValue(mockFiles)
    }

    @Test
    @Ignore
    fun getAccountInfo_returnsAccountInfoFromClientFactory() {
        val mockAccount = mock<FullAccount>()
        val mockEmail = "test@test.ru"
        whenever(mockAccount.email).thenReturn(mockEmail)
        whenever(clientFactory.getClient().users().currentAccount).thenReturn(mockAccount)

        val testSubscriber = repository.getAccountEmail().test()

        testSubscriber.assertCompleted().assertValue(mockEmail)
    }
}