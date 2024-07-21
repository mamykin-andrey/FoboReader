package ru.mamykin.foboreader.read_book.presentation

object TextUtils {

    fun getWordsPositions(text: String): List<Pair<Int, Int>> {
        val wordPositions = mutableListOf<Pair<Int, Int>>()
        var wordStartPos = 0
        for (i in text.indices) {
            val character = text[i]
            if (character.isLetter() || character == '\'') {
                if (wordStartPos == wordPositions.lastOrNull()?.first) {
                    wordStartPos = i
                }
            } else if (wordStartPos != wordPositions.lastOrNull()?.first) {
                wordPositions.add(wordStartPos to i)
            }
        }
        return wordPositions
    }

    fun getWord(wordsPositions: List<Pair<Int, Int>>, charPos: Int, text: String): String {
        return requireNotNull(
            wordsPositions.find { charPos in it.first..it.second }?.let { (start, end) ->
                text.substring(start, end)
            }
        )
    }

    fun getParagraph(wordsPositions: List<Pair<Int, Int>>, charPos: Int, text: String): String {
        val (start, end) = requireNotNull(wordsPositions.find { charPos in it.first..it.second })
        return getSelectedParagraph(text, start, end)
    }

    private fun getSelectedParagraph(text: String, selectionStart: Int, selectionEnd: Int): String {
        val paragraphStart = findLeftLineBreak(text, selectionStart)
        val paragraphEnd = findRightLineBreak(text, selectionEnd)
        return text.substring(paragraphStart, paragraphEnd)
    }

    private fun findLeftLineBreak(text: CharSequence, selStart: Int): Int {
        for (i in selStart downTo 0) {
            if (text[i] == '\n') return i + 1
        }
        return 0
    }

    private fun findRightLineBreak(text: CharSequence, selEnd: Int): Int {
        for (i in selEnd until text.length) {
            if (text[i] == '\n') return i + 1
        }
        return text.length - 1
    }
}