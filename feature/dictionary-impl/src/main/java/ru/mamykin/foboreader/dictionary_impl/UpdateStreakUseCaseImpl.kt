package ru.mamykin.foboreader.dictionary_impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.dictionary_api.DictionaryRepository
import ru.mamykin.foboreader.dictionary_api.UpdateStreakUseCase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class UpdateStreakUseCaseImpl
@Inject
constructor(
    private val dictionaryRepository: DictionaryRepository,
) : UpdateStreakUseCase {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun executeAsync(scope: CoroutineScope) {
        scope.launch {
            dictionaryRepository.addCompletionDate(getTodayDate())
        }
    }

    private fun getTodayDate(): String {
        return dateFormat.format(Date())
    }
}
