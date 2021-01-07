package ru.mamykin.foboreader.settings.presentation.list

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.KoinComponent
import org.koin.core.inject
import reactivecircus.flowbinding.android.view.clicks
import ru.mamykin.foboreader.settings.databinding.ItemAppLanguageBinding
import ru.mamykin.foboreader.settings.domain.model.SettingsItem
import ru.mamykin.foboreader.settings.domain.usecase.GetSelectedLanguage
import ru.mamykin.foboreader.settings.presentation.Event

class AppLanguageHolder(
    private val binding: ItemAppLanguageBinding,
    private val onEvent: (Event) -> Unit,
    lifecycleScope: CoroutineScope
) : SettingsItemHolder<SettingsItem.AppLanguage>(binding.root), KoinComponent {

    private val getSelectedLanguage: GetSelectedLanguage by inject()

    init {
        binding.clRoot.clicks()
            .onEach { onEvent(Event.SelectAppLanguage) }
            .launchIn(lifecycleScope)
    }

    override fun bind(item: SettingsItem.AppLanguage) {
        val (langName, _) = getSelectedLanguage(Unit).getOrThrow()
        binding.tvAppLanguageSubtitle.text = langName
    }
}