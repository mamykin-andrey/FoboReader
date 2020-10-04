package ru.mamykin.foboreader.read_book.domain.helper

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import java.util.*

class TextToSpeechService constructor(
    context: Context
) : TextToSpeech.OnInitListener {

    private val textToSpeech = TextToSpeech(context, this)
    private var ttsInit = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.language = Locale.ENGLISH
            ttsInit = true
        }
    }

    @Suppress("deprecation")
    fun voiceWord(word: String) {
        if (word.isBlank() || !ttsInit)
            return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(word, TextToSpeech.QUEUE_ADD, null, null)
        } else {
            textToSpeech.speak(word, TextToSpeech.QUEUE_ADD, null)
        }
    }
}