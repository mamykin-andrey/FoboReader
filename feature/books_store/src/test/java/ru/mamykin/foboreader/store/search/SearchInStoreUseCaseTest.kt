package ru.mamykin.foboreader.store.search

import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import ru.mamykin.foboreader.store.common.BooksStoreRepository

class SearchInStoreUseCaseTest {

    private val repository: BooksStoreRepository = mock()
    private val useCase = SearchInStoreUseCase(repository)

    @Test
    fun `return result when repository succeeds`() = runTest {
        val testQuery = "abc"
        val mockedResult: SearchResultsEntity = mock()
        whenever(repository.search(testQuery)).thenReturn(mockedResult)

        val result = useCase.execute(testQuery)

        assert(result.isSuccess)
        assert(result.getOrNull() == mockedResult)
    }

    @Test
    fun `return result when repository fails`() = runTest {
        val testQuery = "abc"
        val mockedError = IllegalStateException("test")
        whenever(repository.search(testQuery)).thenThrow(mockedError)

        val result = useCase.execute(testQuery)

        assert(result.isFailure)
        assert(result.exceptionOrNull() == mockedError)
    }
}