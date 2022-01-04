package ru.mamykin.foboreader.settings.presentation.list

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks
import ru.mamykin.foboreader.settings.databinding.ItemAppLanguageBinding
import ru.mamykin.foboreader.settings.domain.model.SettingsItem
import ru.mamykin.foboreader.settings.presentation.Settings

class AppLanguageHolder(
    private val binding: ItemAppLanguageBinding,
    private val onEvent: (Settings.Event) -> Unit,
    lifecycleScope: CoroutineScope
) : SettingsItemHolder<SettingsItem.AppLanguage>(binding.root) {

    init {
        binding.clRoot.clicks()
            .onEach { onEvent(Settings.Event.SelectAppLanguage) }
            .launchIn(lifecycleScope)
    }

    override fun bind(item: SettingsItem.AppLanguage) {
//        val (langName, _) = getSelectedLanguage(Unit).getOrThrow()
        binding.tvAppLanguageSubtitle.text = item.languageCode
    }
}