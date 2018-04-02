package ru.mamykin.foboreader.domain.interactor

import com.nhaarman.mockito_kotlin.whenever
import org.amshove.kluent.mock
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import ru.mamykin.foboreader.data.repository.devicebooks.DeviceBooksRepository
import ru.mamykin.foboreader.domain.devicebooks.DeviceBooksInteractor
import ru.mamykin.foboreader.domain.devicebooks.FileStructureEntity
import ru.mamykin.foboreader.entity.AndroidFile
import ru.mamykin.foboreader.extension.getWeight
import rx.Single

class DeviceBooksInteractorTest {

    @Mock
    lateinit var repository: DeviceBooksRepository

    lateinit var files: List<AndroidFile>

    lateinit var sortedFiles: List<AndroidFile>

    lateinit var interactor: DeviceBooksInteractor

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        files = createFiles()
        //sortedFiles = createSortedFiles()
                //interactor = DeviceBooksInteractor(repository)
    }

    @Test
    fun openDirectory_returnsSortedFiles() {
//        val directory = "/books"
//        val fileStructure = FileStructureEntity(files, true, directory)
//        whenever(repository.canReadDirectory("")).thenReturn(Single.just(true))
//
//        val testSubscriber = interactor.openDirectory("").test()
//
//        testSubscriber.assertCompleted().assertValue(fileStructure)

        val files = files.sortedBy { it.getWeight() }

        files.shouldBe(sortedFiles)
    }

    private fun createFiles(): List<AndroidFile> {
        val mockFile1 = mock<AndroidFile>()
        val mockFile2 = mock<AndroidFile>()
        val mockFile3 = mock<AndroidFile>()
        val mockFile4 = mock<AndroidFile>()

        whenever(mockFile1.getWeight()).thenReturn(6)
        whenever(mockFile2.getWeight()).thenReturn(4)
        whenever(mockFile3.getWeight()).thenReturn(3)
        whenever(mockFile4.getWeight()).thenReturn(1)

        return listOf(mockFile1, mockFile2, mockFile3, mockFile4)
    }

    private fun createSortedFiles(): List<AndroidFile> {
        return listOf(files[3], files[2], files[1], files[0])
    }
}