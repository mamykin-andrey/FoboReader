package ru.mamykin.foboreader.settings.custom_color

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import ru.mamykin.foboreader.settings.R

private const val SCREEN_KEY = "choose_custom_color"
private const val COLOR_CIRCLE_SIZE = 45
private const val COLOR_CIRCLE_PADDING = 4

@Composable
fun ChooseCustomColorDialogUI(
    currentColorCode: String,
    title: String,
    onDismiss: (selectedColorCode: String?) -> Unit,
) {
    val viewModel: ChooseCustomColorViewModel = hiltViewModel() // TODO: Title
    LaunchedEffect(viewModel) {
        viewModel.sendIntent(ChooseCustomColorViewModel.Intent.LoadColors(currentColorCode))
    }
    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collect {
            if (it is ChooseCustomColorViewModel.Effect.Dismiss) {
                onDismiss(it.selectedColorCode)
            }
        }
    }
    ChooseCustomColorDialogScreen(
        viewModel.state,
        viewModel::sendIntent,
    )
}

@Composable
private fun ChooseCustomColorDialogScreen(
    state: ChooseCustomColorViewModel.State,
    onIntent: (ChooseCustomColorViewModel.Intent) -> Unit,
) {
    Dialog(
        onDismissRequest = {
            onIntent(ChooseCustomColorViewModel.Intent.SelectColor(null))
        }, properties = DialogProperties(
            dismissOnBackPress = true, dismissOnClickOutside = true
        )
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column {
                Text(
                    text = AnnotatedString(state.screenTitle),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium)
                )
                CustomColorsScreen(state.colors, onIntent)
                ClickableText(text = AnnotatedString(stringResource(android.R.string.cancel)),
                    style = TextStyle(color = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    onClick = {
                        onIntent(ChooseCustomColorViewModel.Intent.SelectColor(null))
                    })
            }
        }
    }
}

@Composable
private fun CustomColorsScreen(colors: List<ColorItem>, onIntent: (ChooseCustomColorViewModel.Intent) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = (COLOR_CIRCLE_SIZE + COLOR_CIRCLE_PADDING * 2).dp),
        modifier = Modifier.padding(12.dp)
    ) {
        items(colors.size) { pos ->
            val color = colors[pos]
            ColorCircleComposable(color, onIntent)
        }
    }
}

@Composable
private fun ColorCircleComposable(color: ColorItem, onIntent: (ChooseCustomColorViewModel.Intent) -> Unit) {
    Surface(
        modifier = Modifier
            .padding(all = COLOR_CIRCLE_PADDING.dp)
            .aspectRatio(1f)
            .clickable {
                onIntent(ChooseCustomColorViewModel.Intent.SelectColor(color.colorCode))
            }, shape = CircleShape, color = color.colorCode.fromHex()
    ) {
        Box(modifier = Modifier.size(size = COLOR_CIRCLE_SIZE.dp))
        if (color.selected) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                tint = color.checkColorCode.fromHex(),
                contentDescription = null,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

private fun String.fromHex() = Color(android.graphics.Color.parseColor(this))

@Preview
@Composable
private fun Preview() {
    ChooseCustomColorDialogScreen(
        state = ChooseCustomColorViewModel.State(
            "Select translation color", listOf(
                ColorItem(
                    "#000000",
                    "#ffffff", // TODO: Automatic detection?
                    true,
                ),
                ColorItem(
                    "#000000",
                    "#ffffff",
                    true,
                ),
                ColorItem(
                    "#ff0000",
                    "#00ffff",
                    true,
                ),
                ColorItem(
                    "#0000ff",
                    "#ffff00",
                    true,
                ),
            )
        ),
        onIntent = {},
    )
}