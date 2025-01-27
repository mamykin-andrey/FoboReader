package ru.mamykin.foboreader.uikit.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ColoredCircleCompose(colorHexCode: String, size: Dp) {
    Canvas(
        modifier = Modifier.size(size),
        onDraw = {
            drawCircle(color = Color(android.graphics.Color.parseColor(colorHexCode)))
        }
    )
}

@Composable
fun GenericLoadingIndicatorComposable() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        CircularProgressIndicator(modifier = Modifier.size(48.dp))
    }
}

@Composable
fun GenericErrorStubComposable(onRetryClicked: () -> Unit) {
    // TODO: Add Lottie animation
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp),
            contentDescription = null,
        )
        Text(
            text = stringResource(id = ru.mamykin.foboreader.core.R.string.core_error_retry_description),
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 12.dp),
            color = MaterialTheme.colorScheme.onBackground,
        )
        Button(
            modifier = Modifier.padding(top = 24.dp),
            onClick = { onRetryClicked() },
        ) {
            Text(
                text = stringResource(id = ru.mamykin.foboreader.core.R.string.core_error_retry_title)
            )
        }
    }
}

@Preview
@Composable
fun WidgetPreview() {
    FoboReaderTheme {
        GenericErrorStubComposable {}
    }
}