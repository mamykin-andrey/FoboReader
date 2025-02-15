package ru.mamykin.foboreader.read_book.translation

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import ru.mamykin.foboreader.read_book.R

@Composable
internal fun WordTranslationPopupComposable(
    translation: WordTranslationUIModel,
    onLearningChecked: (String) -> Unit,
    onLearningUnchecked: (Long) -> Unit,
    onDismiss: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { onDismiss() }
            }
            .background(Color.Transparent)
    )
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Popup(alignment = Alignment.Center,
            properties = PopupProperties(excludeFromSystemGesture = true),
            onDismissRequest = { onDismiss() }) {
            Box(
                Modifier
                    .background(MaterialTheme.colorScheme.inverseSurface)
                    .padding(20.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    TextWithTitleComposable(
                        R.string.rb_translation_original,
                        translation.word,
                    )
                    TextWithTitleComposable(
                        R.string.rb_translation_translated,
                        translation.translation,
                    )
                    DictionaryCheckboxComposable(
                        translation = translation,
                        onLearningChecked = onLearningChecked,
                        onLearningUnchecked = onLearningUnchecked,
                    )
                }
            }
        }
    }
}

@Composable
private fun TextWithTitleComposable(@StringRes titleRes: Int, text: String) {
    Text(
        text = "${stringResource(titleRes)}: $text",
        color = MaterialTheme.colorScheme.inverseOnSurface,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun DictionaryCheckboxComposable(
    translation: WordTranslationUIModel,
    onLearningChecked: (String) -> Unit,
    onLearningUnchecked: (Long) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        Checkbox(
            checked = translation.dictionaryId != null,
            onCheckedChange = { isChecked ->
                if (isChecked) {
                    onLearningChecked(translation.word)
                } else {
                    onLearningUnchecked(translation.dictionaryId!!)
                }
            }
        )
        Text(
            text = stringResource(R.string.rb_word_translation_learn_check_title),
            color = MaterialTheme.colorScheme.inverseOnSurface,
        )
    }
}