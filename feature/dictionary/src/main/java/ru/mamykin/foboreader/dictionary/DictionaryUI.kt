package ru.mamykin.foboreader.dictionary

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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.GenericLoadingIndicatorComposable

@Composable
fun DictionaryUI(appNavController: NavController) {
    val viewModel: DictionaryViewModel = hiltViewModel()
    LaunchedEffect(viewModel) {
        viewModel.sendIntent(DictionaryViewModel.Intent.LoadData)
    }
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collect {
            takeEffect(it, snackbarHostState)
        }
    }
    DictionaryScreenComposable(
        state = viewModel.state,
        onIntent = viewModel::sendIntent,
        snackbarHostState = snackbarHostState,
        appNavController = appNavController,
    )
}

private suspend fun takeEffect(
    effect: DictionaryViewModel.Effect,
    snackbarHostState: SnackbarHostState,
) {
    when (effect) {
        is DictionaryViewModel.Effect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DictionaryScreenComposable(
    state: DictionaryViewModel.State,
    onIntent: (DictionaryViewModel.Intent) -> Unit,
    snackbarHostState: SnackbarHostState,
    appNavController: NavController,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
                    is DictionaryViewModel.State.Content -> ContentComposable(state, onIntent)
                }
            }
        })
}

@Composable
private fun ContentComposable(
    state: DictionaryViewModel.State.Content,
    onIntent: (DictionaryViewModel.Intent) -> Unit
) {
    Column {
        LearnNewWordsButtonComposable(state.learnedTodayCount)
        AllWordsButtonComposable(state.allWordsCount)
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
private fun LearnNewWordsButtonComposable(learnedTodayCount: Int) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        shape = ShapeDefaults.Small,
        onClick = { /*TODO*/ },
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
private fun AllWordsButtonComposable(allWordsCount: Int) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp),
        shape = ShapeDefaults.Small,
        onClick = { /*TODO*/ },
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
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() },
            appNavController = rememberNavController(),
        )
    }
}