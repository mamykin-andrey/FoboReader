package ru.mamykin.foboreader.domain.readbook

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import java.util.*
import javax.inject.Inject

class TextToSpeechService @Inject constructor(context: Context) : TextToSpeech.OnInitListener {

    private val textToSpeech = TextToSpeech(context, this)
    private var ttsInit = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            ttsInit = true
        }
    }

    @Suppress("deprecation")
    fun voiceWord(word: String) {
        if (word.isNotBlank() and ttsInit) {
            textToSpeech.language = Locale.ENGLISH

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak(word, TextToSpeech.QUEUE_ADD, null, null)
            } else {
                textToSpeech.speak(word, TextToSpeech.QUEUE_ADD, null)
            }
        }
    }
}