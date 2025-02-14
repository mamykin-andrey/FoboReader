package ru.mamykin.foboreader.dictionary_impl

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.mamykin.foboreader.dictionary_api.DictionaryWord
import java.util.Date

@Entity
class DictionaryWordDBModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val dateAdded: Date,
    val word: String,
    val translation: String,
) {
    fun toDomainModel() = DictionaryWord(
        id = id,
        dateAdded = dateAdded,
        word = word,
        translation = translation,
    )
}