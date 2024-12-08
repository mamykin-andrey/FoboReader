package ru.mamykin.foboreader.main

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MainFeatureTest {

    private val actor = MainFeature.MainActor()
    private val reducer = MainFeature.MainReducer()
    private val feature = MainFeature(actor, reducer)
    private val testRoute = "test"

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun openTab_navigateToTabEffect() = runTest {
        val state = feature.state

        feature.sendIntent(MainFeature.Intent.OpenTab(testRoute))

        assertEquals(state, feature.state)
        assertEquals(MainFeature.Effect.NavigateToTab(testRoute), feature.effectFlow.first())
    }
}