package ru.mamykin.foboreader.settings.custom_color

import ru.mamykin.foboreader.settings.common.CustomColorType
import java.io.Serializable

data class ChooseColorResult(
    val type: CustomColorType,
    val colorCode: String?,
) : Serializable