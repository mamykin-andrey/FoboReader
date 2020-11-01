package ru.mamykin.foboreader.settings.presentation.list

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks
import ru.mamykin.foboreader.settings.databinding.ItemTextSizeBinding
import ru.mamykin.foboreader.settings.presentation.Event
import ru.mamykin.foboreader.settings.domain.model.SettingsItem

class TextSizeHolder(
    private val binding: ItemTextSizeBinding,
    private val onEvent: (Event) -> Unit,
    lifecycleScope: CoroutineScope
) : SettingsItemHolder<SettingsItem.ReadTextSize>(binding.root) {

    init {
        binding.btnTextSizeMinus.clicks()
            .onEach { onEvent(Event.DecreaseTextSizeClicked) }
            .launchIn(lifecycleScope)

        binding.btnTextSizePlus.clicks()
            .onEach { onEvent(Event.IncreaseTextSizeClicked) }
            .launchIn(lifecycleScope)
    }

    override fun bind(item: SettingsItem.ReadTextSize) {
        binding.tvTextSize.text = item.textSize.toString()
    }
}