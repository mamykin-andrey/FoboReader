package ru.mamykin.foboreader.settings.custom_color

import ru.mamykin.foboreader.core.navigation.AppScreen
import java.io.Serializable

data class ChooseColorResult(
    val type: AppScreen.ChooseColor.CustomColorType,
    val colorCode: String?,
) : Serializable