package ru.mamykin.foboreader.dictionary_impl

import ru.mamykin.foboreader.dictionary_api.DictionaryRepository
import ru.mamykin.foboreader.dictionary_api.GetStreakInfoUseCase
import ru.mamykin.foboreader.dictionary_api.StreakInfo
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class GetStreakInfoUseCaseImpl @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
) : GetStreakInfoUseCase {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override suspend fun execute(): StreakInfo {
        val todayDate = getTodayDate()
        val yesterdayDate = getYesterdayDate()
        val completionDatesReversed = dictionaryRepository.getCompletionDates().reversed()
        val currentStreak = completionDatesReversed.takeWhile { it == todayDate || it == yesterdayDate }.size
        val bestStreak = getBestStreak(completionDatesReversed)
        return StreakInfo(currentStreak, bestStreak)
    }

    private fun getBestStreak(completionDatesReversed: List<String>): Int {
        var bestStreak = 1
        var currentStreak = 1
        var lastDate = completionDatesReversed.firstOrNull() ?: return 0
        for (date in completionDatesReversed.drop(1)) {
            if (getDaysBetweenDates(date, lastDate) == 1) {
                currentStreak++
            } else {
                bestStreak = maxOf(bestStreak, currentStreak)
                currentStreak = 1
            }
            lastDate = date
        }
        return bestStreak
    }

    private fun getDaysBetweenDates(date1Str: String, date2Str: String): Int {
        val date1 = dateFormat.parse(date1Str)!!
        val date2 = dateFormat.parse(date2Str)!!
        return (date2.time - date1.time).toInt()
    }

    private fun getTodayDate(): String {
        return dateFormat.format(Date())
    }

    private fun getYesterdayDate(): String {
        val yesterday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
        }.time
        return dateFormat.format(yesterday)
    }
} 