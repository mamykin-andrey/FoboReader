package ru.mamykin.foboreader.book_details.presentation

import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.mamykin.foboreader.core.platform.Log
import ru.mamykin.foboreader.core.platform.NotificationManager
import ru.mamykin.foboreader.core.platform.ResourceManager
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.store.data.network.DownloadFileException
import ru.mamykin.foboreader.store.data.network.FileRepository
import ru.mamykin.foboreader.store.domain.usecase.DownloadBook
import java.io.File

class DownloadBookTest {

    private val fileRepository: FileRepository = mockk()
    private val resourceManager: ResourceManager = mockk()
    private val notificationManager: NotificationManager = mockk()
    private val bookLink = "https://somehost.ru/file.fb"
    private val downloadFailed = "failed"
    private val downloadStarted = "started"
    private val downloadFinished = "started"
    private val downloadBook = DownloadBook(
        fileRepository,
        resourceManager,
        notificationManager,
    )
    private val file: File = mockk()

    init {
        every { resourceManager.getString(R.string.books_store_download_failed) } returns downloadFailed
        every { resourceManager.getString(R.string.books_store_download_progress) } returns downloadStarted
        every { resourceManager.getString(R.string.books_store_download_completed) } returns downloadFinished
        every { notificationManager.notify(any(), any(), any(), any(), any()) } just Runs
        Log.init(false)
    }

    @Test
    fun `test download finished`() {
        runBlocking {
            every { fileRepository.createFile(any()) } returns file
            coEvery { fileRepository.downloadFile(any(), any()) } just Runs

            val result = downloadBook.execute(bookLink, "Download/file.fb")

            verifyOrder {
                notificationManager.notify(any(), downloadStarted, any(), any(), any())
                notificationManager.notify(any(), downloadFinished, any(), any(), any())
            }
            assert(result.isSuccess)
        }
    }

    @Test
    fun `test create file failed`() {
        runBlocking {
            every { fileRepository.createFile(any()) } throws DownloadFileException()

            val result = downloadBook.execute(bookLink, "Download/file.fb")

            verifyOrder {
                notificationManager.notify(any(), downloadStarted, any(), any(), any())
                notificationManager.notify(any(), downloadFailed, any(), any(), any())
            }
            assert(!result.isSuccess)
        }
    }

    @Test
    fun `test download file failed`() {
        runBlocking {
            every { fileRepository.createFile(any()) } returns file
            coEvery { fileRepository.downloadFile(any(), any()) } throws DownloadFileException()

            val result = downloadBook.execute(bookLink, "Download/file.fb")

            verifyOrder {
                notificationManager.notify(any(), downloadStarted, any(), any(), any())
                notificationManager.notify(any(), downloadFailed, any(), any(), any())
            }
            assert(!result.isSuccess)
        }
    }
}