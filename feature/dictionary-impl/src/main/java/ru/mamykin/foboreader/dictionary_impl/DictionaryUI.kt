package ru.mamykin.foboreader.dictionary_impl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.mamykin.foboreader.core.navigation.AppScreen
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.GenericLoadingIndicatorComposable

@Composable
fun DictionaryUI(appNavController: NavController) {
    val viewModel: DictionaryViewModel = hiltViewModel()
    LaunchedEffect(viewModel) {
        viewModel.sendIntent(DictionaryViewModel.Intent.LoadData)
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    DictionaryScreenComposable(
        state = state,
        appNavController = appNavController,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DictionaryScreenComposable(
    state: DictionaryViewModel.State,
    appNavController: NavController,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.mw_tab_screen_title))
                },
                windowInsets = WindowInsets(0.dp),
            )
        }, content = { innerPadding ->
            Box(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
                when (state) {
                    is DictionaryViewModel.State.Loading -> GenericLoadingIndicatorComposable()
                    is DictionaryViewModel.State.Content -> ContentComposable(state, appNavController)
                }
            }
        })
}

@Composable
private fun ContentComposable(
    state: DictionaryViewModel.State.Content,
    appNavController: NavController
) {
    Column {
        LearnNewWordsButtonComposable(state.learnedTodayCount) {
            appNavController.navigate(AppScreen.LearnNewWords.route)
        }
        AllWordsButtonComposable(state.allWordsCount) {
            appNavController.navigate(AppScreen.AllWords.route)
        }
        Row {
            StreakCardComposable(
                stringResource(id = R.string.mw_current_streak_card_title),
                state.currentStreakDays,
                Modifier
                    .padding(start = 16.dp, end = 8.dp)
                    .weight(1f),
            )
            StreakCardComposable(
                stringResource(id = R.string.mw_best_streak_card_title),
                state.bestStreakDays,
                Modifier
                    .padding(end = 16.dp, start = 8.dp)
                    .weight(1f),
            )
        }
    }
}

@Composable
private fun LearnNewWordsButtonComposable(learnedTodayCount: Int, onClick: () -> Unit) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        shape = ShapeDefaults.Small,
        onClick = { onClick() },
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                stringResource(id = R.string.mw_learn_new_words_button_title),
                fontSize = 16.sp,
            )
            Text(
                stringResource(id = R.string.mw_learn_new_words_button_subtitle_fmt, learnedTodayCount),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
    }
}

@Composable
private fun AllWordsButtonComposable(allWordsCount: Int, onClick: () -> Unit) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp),
        shape = ShapeDefaults.Small,
        onClick = { onClick() },
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                stringResource(id = R.string.mw_all_words_button_title),
                fontSize = 16.sp,
            )
            Text(
                stringResource(id = R.string.mw_all_words_button_subtitle_fmt, allWordsCount),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
    }
}

@Composable
private fun StreakCardComposable(
    streakName: String,
    streakDays: Int,
    modifier: Modifier,
) {
    Card(
        modifier.then(
            Modifier
                .padding(top = 16.dp)
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = streakName,
                fontSize = 16.sp,
            )
            Text(
                text = stringResource(id = R.string.mw_streak_days_card_subtitle_fmt, streakDays),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
    }
}

@Preview
@Composable
fun Preview() {
    FoboReaderTheme {
        DictionaryScreenComposable(
            state = DictionaryViewModel.State.Content(
                learnedTodayCount = 0,
                allWordsCount = 50,
                currentStreakDays = 5,
                bestStreakDays = 120,
            ),
            appNavController = rememberNavController(),
        )
    }
}