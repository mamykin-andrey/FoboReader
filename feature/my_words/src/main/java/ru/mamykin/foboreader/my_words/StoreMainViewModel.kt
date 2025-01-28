// package ru.mamykin.foboreader.store.categories
//
// import androidx.lifecycle.ViewModel
// import androidx.lifecycle.viewModelScope
// import dagger.hilt.android.lifecycle.HiltViewModel
// import kotlinx.coroutines.launch
// import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
// import ru.mamykin.foboreader.core.presentation.LoggingEffectChannel
// import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
// import ru.mamykin.foboreader.core.presentation.StringOrResource
// import javax.inject.Inject
//
// @HiltViewModel
// internal class StoreMainViewModel @Inject constructor(
//     private val getBookCategories: GetBookCategoriesUseCase,
//     private val errorMessageMapper: ErrorMessageMapper,
// ) : ViewModel() {
//
//     var state: State by LoggingStateDelegate(State.Loading)
//         private set
//
//     private val effectChannel = LoggingEffectChannel<Effect>()
//     val effectFlow = effectChannel.receiveAsFlow()
//
//     fun sendIntent(intent: Intent) = viewModelScope.launch {
//         when (intent) {
//             is Intent.LoadCategories -> {
//                 if (state is State.Content) return@launch
//                 state = State.Loading
//                 getBookCategories.execute().fold(
//                     { state = State.Content(it.map(BookCategoryUIModel::fromDomainModel)) },
//                     { state = State.Error(errorMessageMapper.getMessage(it)) }
//                 )
//             }
//
//             is Intent.OpenCategory -> {
//                 effectChannel.send(Effect.OpenBooksListScreen(intent.categoryId))
//             }
//         }
//     }
//
//     sealed class Intent {
//         data object LoadCategories : Intent()
//         class OpenCategory(val categoryId: String) : Intent()
//     }
//
//     sealed class Effect {
//         class ShowSnackbar(val message: String) : Effect()
//         class OpenBooksListScreen(val categoryId: String) : Effect()
//     }
//
//     sealed class State {
//         data object Loading : State()
//
//         data class Content(
//             val categories: List<BookCategoryUIModel>,
//         ) : State()
//
//         data class Error(
//             val message: StringOrResource
//         ) : State()
//     }
// }