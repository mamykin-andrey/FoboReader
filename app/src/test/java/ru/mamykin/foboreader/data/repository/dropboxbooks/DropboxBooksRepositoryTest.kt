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
import ru.mamykin.foboreader.data.exception.UserNotAuthorizedException
import ru.mamykin.foboreader.entity.DropboxFile
import ru.mamykin.foboreader.entity.mapper.FolderToFilesListMapper
import rx.Completable

class DropboxBooksRepositoryTest {

    @Mock
    lateinit var storage: DropboxBooksStorage
    @Mock
    lateinit var mapper: FolderToFilesListMapper
    @Mock
    lateinit var mockFolder: ListFolderResult
    @Mock
    lateinit var mockFile: DropboxFile

    lateinit var mockFiles: List<DropboxFile>

    lateinit var repository: DropboxBooksRepository

    private val authToken = "S1828HD9J"

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = DropboxBooksRepository(storage, mapper)
    }

    @Test
    @Ignore
    fun getRootDirectoryFiles_returnsRootDirectoryFiles_whenAuthTokenIsNotBlank() {
        whenever(storage.authToken).thenReturn(authToken)
//        whenever(clientFactory.init(authToken)).thenReturn(Completable.complete())
//        whenever(clientFactory.getClient().files().listFolder("")).thenReturn(mockFolder)
        whenever(mapper.transform(mockFolder)).thenReturn(mockFiles)

//        val testSubscriber = repository.getRootDirectoryFiles().test()

        testSubscriber.assertCompleted().assertValue(mockFiles)
    }

    @Test
    @Ignore
    fun getAccountEmail_returnsAccountEmail() {
        val mockAccount = mock<FullAccount>()
        val email = "test@test.ru"
        whenever(mockAccount.email).thenReturn(email)
//        whenever(clientFactory.getClient().users().currentAccount).thenReturn(mockAccount)

        val testSubscriber = repository.getAccountEmail().test()

        testSubscriber.assertCompleted().assertValue(email)
    }
}