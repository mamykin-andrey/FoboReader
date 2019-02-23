package ru.mamykin.foboreader.domain.devicebooks

import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import ru.mamykin.foboreader.data.repository.devicebooks.DeviceBooksRepository
import ru.mamykin.foboreader.core.extension.isFictionBook
import java.io.File

class DeviceBooksInteractorTest {

    @Mock
    lateinit var repository: DeviceBooksRepository
    @Mock
    lateinit var mockFile: File

    lateinit var interactor: DeviceBooksInteractor

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        interactor = DeviceBooksInteractor(repository)
        whenever(mockFile.name).thenReturn("name")
    }

    @Test
    fun openFile_throwsAccessDeniedException_whenCannotRead() {
        whenever(mockFile.canRead()).thenReturn(false)

        val testSubscriber = interactor.openFile(mockFile).test()

        testSubscriber.assertNotCompleted().assertError(AccessDeniedException::class.java)
    }

    @Test
    @Ignore
    fun openFile_throwsUnknownFormatException_whenUnknownFormat() {
        whenever(mockFile.canRead()).thenReturn(true)
        whenever(mockFile.isFictionBook).thenReturn(false)

        val testSubscriber = interactor.openFile(mockFile).test()

        testSubscriber.assertNotCompleted().assertError(UnknownBookFormatException::class.java)
    }

    @Test
    @Ignore
    fun openFile_returnsFilePath_whenCanReadAndIsBook() {
        whenever(mockFile.canRead()).thenReturn(true)
        whenever(mockFile.isFictionBook).thenReturn(true)

        val testSubscriber = interactor.openFile(mockFile).test()

        testSubscriber.assertCompleted().assertNoErrors()
    }
}