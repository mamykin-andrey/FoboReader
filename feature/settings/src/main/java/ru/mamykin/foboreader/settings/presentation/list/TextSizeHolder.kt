package ru.mamykin.foboreader.settings.presentation.list

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks
import ru.mamykin.foboreader.settings.databinding.ItemTextSizeBinding
import ru.mamykin.foboreader.settings.domain.model.SettingsItem
import ru.mamykin.foboreader.settings.presentation.Settings

class TextSizeHolder(
    private val binding: ItemTextSizeBinding,
    private val onEvent: (Settings.Event) -> Unit,
    lifecycleScope: CoroutineScope
) : SettingsItemHolder<SettingsItem.ReadTextSize>(binding.root) {

    init {
        binding.btnTextSizeMinus.clicks()
            .onEach { onEvent(Settings.Event.DecreaseTextSizeClicked) }
            .launchIn(lifecycleScope)

        binding.btnTextSizePlus.clicks()
            .onEach { onEvent(Settings.Event.IncreaseTextSizeClicked) }
            .launchIn(lifecycleScope)
    }

    override fun bind(item: SettingsItem.ReadTextSize) {
        binding.tvTextSize.text = item.textSize.toString()
    }
}