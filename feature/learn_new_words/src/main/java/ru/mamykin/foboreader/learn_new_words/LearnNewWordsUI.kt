package ru.mamykin.foboreader.learn_new_words

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.GenericLoadingIndicatorComposable

@Composable
fun LearnNewWordsUI(appNavController: NavController) {
    val viewModel: LearnNewWordsViewModel = hiltViewModel()
    LaunchedEffect(viewModel) {
        viewModel.sendIntent(LearnNewWordsViewModel.Intent.LoadData)
    }
    LearnNewWordsScreenComposable(
        state = viewModel.state,
        appNavController = appNavController,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LearnNewWordsScreenComposable(
    state: LearnNewWordsViewModel.State,
    appNavController: NavController,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.mw_tab_screen_title))
                },
            )
        }, content = { innerPadding ->
            Box(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
            ) {
                when (state) {
                    is LearnNewWordsViewModel.State.Loading -> GenericLoadingIndicatorComposable()
                    is LearnNewWordsViewModel.State.Content -> ContentComposable(state, appNavController)
                }
            }
        })
}

@Composable
private fun ContentComposable(
    state: LearnNewWordsViewModel.State.Content,
    appNavController: NavController
) {
}

@Preview
@Composable
fun Preview() {
    FoboReaderTheme {
        LearnNewWordsScreenComposable(
            state = LearnNewWordsViewModel.State.Content,
            appNavController = rememberNavController(),
        )
    }
}