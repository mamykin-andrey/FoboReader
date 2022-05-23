package ru.mamykin.foboreader.app.platform

import android.util.Log
import android.view.View
import android.view.Window
import androidx.metrics.performance.JankStats
import androidx.metrics.performance.PerformanceMetricsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import java.lang.ref.WeakReference

object PerformanceTracker {

    private const val LOG_TAG = "PerformanceTracker"

    private lateinit var viewRef: WeakReference<View>
    private lateinit var jankStats: JankStats
    private var totalFrames = 0
    private var jankFrames = 0

    fun startTracking(root: View, window: Window) {
        viewRef = WeakReference(root)
        val metricsStateHolder = PerformanceMetricsState.getForHierarchy(root)
        jankStats = JankStats.createAndTrack(window, Dispatchers.Default.asExecutor()) {
            totalFrames++
            if (it.isJank) jankFrames++
        }
        metricsStateHolder.state?.addState("Activity", javaClass.simpleName)
    }

    fun stopTracking() {
        viewRef.clear()
        jankStats.isTrackingEnabled = false
        val jankFramesPercent = jankFrames.toFloat() / totalFrames.toFloat() * 100
        Log.d(LOG_TAG, "Total frames: $totalFrames, jank frames percent: $jankFramesPercent")
    }
}