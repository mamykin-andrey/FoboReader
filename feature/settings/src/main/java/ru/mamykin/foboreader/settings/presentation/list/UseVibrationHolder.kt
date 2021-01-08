package ru.mamykin.foboreader.settings.presentation.list

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.KoinComponent
import org.koin.core.inject
import reactivecircus.flowbinding.android.view.clicks
import ru.mamykin.foboreader.core.domain.usecase.GetVibrationEnabled
import ru.mamykin.foboreader.settings.databinding.ItemUseVibrationBinding
import ru.mamykin.foboreader.settings.domain.model.SettingsItem
import ru.mamykin.foboreader.settings.presentation.Event

class UseVibrationHolder(
    private val binding: ItemUseVibrationBinding,
    private val onEvent: (Event) -> Unit,
    lifecycleScope: CoroutineScope
) : SettingsItemHolder<SettingsItem.UseVibration>(binding.root), KoinComponent {

    private val getVibrationEnabled: GetVibrationEnabled by inject()

    init {
        binding.clRoot.clicks()
            .onEach { onEvent(Event.UseVibrationChanged(!binding.cbOptionValue.isChecked)) }
            .launchIn(lifecycleScope)
    }

    override fun bind(item: SettingsItem.UseVibration) {
        val vibrationEnabled = getVibrationEnabled(Unit).getOrThrow()
        binding.cbOptionValue.isChecked = vibrationEnabled
    }
}