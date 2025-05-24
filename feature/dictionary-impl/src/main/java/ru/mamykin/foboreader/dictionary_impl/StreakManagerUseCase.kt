package ru.mamykin.foboreader.dictionary_impl

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.dictionary_api.StreakManagerUseCase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class StreakManagerUseCaseImpl @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository
) : StreakManagerUseCase {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun updateStreakForCompletion() {
        val today = dateFormat.format(Date())
        val lastCompletionDate = appSettingsRepository.getLastCompletionDate()

        val currentStreak = when {
            lastCompletionDate == null -> {
                // First time completing
                1
            }

            lastCompletionDate == today -> {
                // Already completed today, don't increment
                appSettingsRepository.getCurrentStreak()
            }

            isYesterday(lastCompletionDate) -> {
                // Completed yesterday, increment streak
                appSettingsRepository.getCurrentStreak() + 1
            }

            else -> {
                // Missed days, reset streak
                1
            }
        }

        appSettingsRepository.setCurrentStreak(currentStreak)
        appSettingsRepository.setLastCompletionDate(today)

        // Update best streak if current is better
        val bestStreak = appSettingsRepository.getBestStreak()
        if (currentStreak > bestStreak) {
            appSettingsRepository.setBestStreak(currentStreak)
        }
    }

    override fun getCurrentStreak(): Int {
        val currentStreak = appSettingsRepository.getCurrentStreak()
        val lastCompletionDate = appSettingsRepository.getLastCompletionDate()

        // If last completion was not yesterday or today, reset streak
        return if (lastCompletionDate != null &&
            !isToday(lastCompletionDate) &&
            !isYesterday(lastCompletionDate)
        ) {
            appSettingsRepository.setCurrentStreak(0)
            0
        } else {
            currentStreak
        }
    }

    override fun getBestStreak(): Int {
        return appSettingsRepository.getBestStreak()
    }

    private fun isYesterday(dateString: String): Boolean {
        return try {
            val date = dateFormat.parse(dateString) ?: return false
            val yesterday = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -1)
            }.time
            val yesterdayString = dateFormat.format(yesterday)
            dateString == yesterdayString
        } catch (e: Exception) {
            false
        }
    }

    private fun isToday(dateString: String): Boolean {
        return try {
            val today = dateFormat.format(Date())
            dateString == today
        } catch (e: Exception) {
            false
        }
    }
} 