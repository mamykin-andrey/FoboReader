package ru.mamykin.foboreader.presentation.devicebooks

import androidx.annotation.StringRes
import ru.mamykin.foboreader.R
import ru.mamykin.core.mvvm.BaseViewModel
import ru.mamykin.core.platform.PermissionsManager
import ru.mamykin.foboreader.data.model.FileStructure
import ru.mamykin.foboreader.domain.devicebooks.DeviceBooksInteractor
import java.io.File
import javax.inject.Inject

class DeviceBooksViewModel @Inject constructor(
        private val interactor: DeviceBooksInteractor,
        private val permissionsManager: PermissionsManager
) : BaseViewModel<DeviceBooksViewModel.ViewState, DeviceBooksViewModel.Action, DeviceBooksRouter>(
        ViewState(isLoading = true)
) {
    override fun reduceState(action: Action): ViewState = when (action) {
        is Action.FilesLoaded -> state.copy(
                isLoading = false,
                error = null,
                noPermission = false,
                structure = action.structure
        )
        is Action.NoPermission -> state.copy(
                isLoading = false,
                structure = null,
                noPermission = true,
                error = null
        )
        is Action.NoAccess -> state.copy(
                isLoading = false,
                structure = null,
                noPermission = false,
                error = R.string.error_access_denied
        )
        is Action.UnsupportedFormat -> state.copy(
                isLoading = false,
                structure = null,
                noPermission = false,
                error = R.string.error_unexpected_file_format
        )
    }

    private fun checkPermissions() = when {
        permissionsManager.hasReadExternalStoragePermission() -> openRootDirectory()
        else -> onAction(Action.NoPermission)
    }

    private fun onFileClicked(file: File) {
        runCatching { interactor.openFile(file) }
                .onSuccess { router?.openBook(it) }
                .onFailure { onAction(Action.UnsupportedFormat) }
    }

    private fun onDirectoryClicked(directoryPath: String) {
        runCatching { interactor.getDirectoryFiles(directoryPath) }
                .onSuccess { onAction(Action.FilesLoaded(it.first())) }
                .onFailure { onAction(Action.NoAccess) }
    }

    private fun onParentDirectoryClicked() {
        runCatching { interactor.getParentDirectoryFiles() }
                .onSuccess { onAction(Action.FilesLoaded(it.first())) }
                .onFailure { onAction(Action.NoAccess) }
    }

    private fun openRootDirectory() {
        runCatching { interactor.getRootDirectoryFiles() }
                .onSuccess { onAction(Action.FilesLoaded(it)) }
                .onFailure { onAction(Action.NoAccess) }
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnCheckPermissions -> checkPermissions()
            is Event.OnFileClicked -> onFileClicked(event.file)
            is Event.OnDirectoryClicked -> onDirectoryClicked(event.directoryPath)
            is Event.OnParentDirectoryClicked -> onParentDirectoryClicked()
        }
    }

    sealed class Event {
        object OnCheckPermissions : Event()
        data class OnFileClicked(val file: File) : Event()
        data class OnDirectoryClicked(val directoryPath: String) : Event()
        object OnParentDirectoryClicked : Event()
    }

    sealed class Action {
        data class FilesLoaded(val structure: FileStructure) : Action()
        object NoPermission : Action()
        object NoAccess : Action()
        object UnsupportedFormat : Action()
    }

    data class ViewState(
            val isLoading: Boolean = false,
            val structure: FileStructure? = null,
            val noPermission: Boolean = false,
            @StringRes val error: Int? = null
    )
}