package ru.mamykin.foboreader.main

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MainFeatureTest {

    private val actor = MainFeature.MainActor()
    private val reducer = MainFeature.MainReducer()
    private val testRoute = "test"

    @Test
    fun actor_whenIntentIsOpenTab_changeTab() = runTest {
        val flow = actor.invoke(MainFeature.Intent.OpenTab(testRoute))

        val action = flow.first() as? MainFeature.Action.TabChanged
        assertNotNull(action)
        assertEquals(MainFeature.Action.TabChanged(testRoute), action)
    }

    @Test
    fun reducer_whenActionIsChangeTab_navigateToTab() = runTest {
        val state = MainFeature.State()

        val (newState, effects) = reducer.invoke(state, MainFeature.Action.TabChanged(testRoute))

        assertEquals(state, newState)
        assertTrue(effects.size == 1)
        assertEquals(MainFeature.Effect.NavigateToTab(testRoute), effects.first())
    }
}