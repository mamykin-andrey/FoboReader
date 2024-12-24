package ru.mamykin.foboreader.main

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MainFeatureTest {

    private val actor = MainViewModel.MainActor()
    private val reducer = MainViewModel.MainReducer()
    private val testScope = TestScope(StandardTestDispatcher())
    private val feature = MainViewModel(actor, reducer, testScope)
    private val testRoute = "test"

    @Test
    fun `show screen tabs on start`() {
        assertTrue(feature.state.tabs.isNotEmpty())
    }

    @Test
    fun `navigate to tab`() = runTest {
        val state = feature.state

        feature.sendIntent(MainViewModel.Intent.OpenTab(testRoute))
        testScope.advanceUntilIdle()

        assertEquals(state, feature.state)
        assertEquals(MainViewModel.Effect.NavigateToTab(testRoute), feature.effectFlow.first())
    }
}