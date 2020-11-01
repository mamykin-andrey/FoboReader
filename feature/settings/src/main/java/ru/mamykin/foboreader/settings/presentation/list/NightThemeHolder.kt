package ru.mamykin.foboreader.settings.presentation.list

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.mamykin.foboreader.core.extension.manualCheckedChanges
import ru.mamykin.foboreader.settings.databinding.ItemNightThemeBinding
import ru.mamykin.foboreader.settings.presentation.Event
import ru.mamykin.foboreader.settings.domain.model.SettingsItem

class NightThemeHolder(
    private val binding: ItemNightThemeBinding,
    private val onEvent: (Event) -> Unit,
    lifecycleScope: CoroutineScope
) : SettingsItemHolder<SettingsItem.NightTheme>(binding.root) {

    init {
        binding.swNightTheme.manualCheckedChanges()
            .onEach { onEvent(Event.NightThemeChanged(it)) }
            .launchIn(lifecycleScope)
    }

    override fun bind(item: SettingsItem.NightTheme) {
        binding.swNightTheme.isChecked = item.isEnabled
    }
}