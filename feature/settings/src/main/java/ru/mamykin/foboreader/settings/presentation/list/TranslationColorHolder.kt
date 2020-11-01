package ru.mamykin.foboreader.settings.presentation.list

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks
import ru.mamykin.foboreader.settings.databinding.ItemTranslationColorBinding
import ru.mamykin.foboreader.settings.presentation.Event
import ru.mamykin.foboreader.settings.domain.model.SettingsItem

class TranslationColorHolder(
    private val binding: ItemTranslationColorBinding,
    private val onEvent: (Event) -> Unit,
    lifecycleScope: CoroutineScope
) : SettingsItemHolder<SettingsItem.TranslationColor>(binding.root) {

    init {
        binding.clTranslationColor.clicks()
            .onEach { onEvent(Event.SelectReadColorClicked) }
            .launchIn(lifecycleScope)
    }

    override fun bind(item: SettingsItem.TranslationColor) {
        // TODO:
    }
}