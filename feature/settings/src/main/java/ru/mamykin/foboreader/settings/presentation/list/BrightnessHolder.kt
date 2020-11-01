package ru.mamykin.foboreader.settings.presentation.list

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.mamykin.foboreader.core.extension.changeProgressEvents
import ru.mamykin.foboreader.settings.databinding.ItemBackgroundBrightnessBinding
import ru.mamykin.foboreader.settings.presentation.Event
import ru.mamykin.foboreader.settings.domain.model.SettingsItem

class BrightnessHolder(
    private val binding: ItemBackgroundBrightnessBinding,
    private val onEvent: (Event) -> Unit,
    lifecycleScope: CoroutineScope
) : SettingsItemHolder<SettingsItem.Brightness>(binding.root) {

    init {
        binding.seekBright.changeProgressEvents()
            .onEach { onEvent(Event.BrightnessChanged(it)) }
            .launchIn(lifecycleScope)
    }

    override fun bind(item: SettingsItem.Brightness) {
        binding.seekBright.progress = item.brightness
    }
}