// package ru.mamykin.foboreader.store.categories
//
// import ru.mamykin.foboreader.store.common.BooksStoreRepository
// import javax.inject.Inject
//
// internal class GetBookCategoriesUseCase @Inject constructor(
//     private val repository: BooksStoreRepository,
// ) {
//     suspend fun execute(): Result<List<BookCategoryEntity>> {
//         return runCatching {
//             repository.getCategories()
//         }
//     }
// }