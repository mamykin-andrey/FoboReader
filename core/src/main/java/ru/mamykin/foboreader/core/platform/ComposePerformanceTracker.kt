package ru.mamykin.foboreader.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember

object ComposePerformanceTracker {

    private const val LOG_TAG = "RecompositionTracker"

    @Composable
    fun TrackRecompositions(
        trackingName: String,
        content: @Composable () -> Unit
    ) {
        val recompositionTracker = remember { RecompositionTracker(trackingName) }

        recompositionTracker.onEnter()
        DisposableEffect(content) {
            onDispose {
                val metrics = recompositionTracker.onExit()
                Log.debug(tag = LOG_TAG, message = "Metrics: $metrics")
            }
        }

        content()
    }
}

private class RecompositionTracker(private val trackingName: String) {
    private var startTime: Long = 0
    private var recompositionCount = 0

    fun onEnter() {
        startTime = System.nanoTime()
        recompositionCount++
    }

    fun onExit(): RecompositionMetrics {
        val duration = (System.nanoTime() - startTime) / 1_000_000
        return RecompositionMetrics(
            trackingName,
            recompositionCount,
            duration,
        )
    }
}

private data class RecompositionMetrics(
    val trackingName: String,
    val recompositionCount: Int,
    val duration: Long,
)