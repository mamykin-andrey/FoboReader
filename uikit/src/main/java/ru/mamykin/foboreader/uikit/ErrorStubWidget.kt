package ru.mamykin.foboreader.uikit

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import ru.mamykin.foboreader.core.extension.dpToPx
import ru.mamykin.foboreader.core.extension.safeThrow

class ErrorStubWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private var retryClickListener: (() -> Unit)? = null
    private val view = LayoutInflater.from(context).inflate(R.layout.view_error_stub_content, this)
    private val btnRetry = view.findViewById<Button>(R.id.btn_retry)
    private val lavImage = view.findViewById<LottieAnimationView>(R.id.lav_image)
    private val tvErrorMessage = view.findViewById<TextView>(R.id.tv_error_message)
    private val contentPadding = context.dpToPx(12).toInt()

    init {
        orientation = VERTICAL
        setPadding(
            contentPadding,
            contentPadding,
            contentPadding,
            contentPadding
        )
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
        gravity = Gravity.CENTER

        btnRetry.setOnClickListener {
            retryClickListener?.invoke() ?: safeThrow(
                IllegalStateException("Retry click listener not set!")
            )
        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.VISIBLE) {
            lavImage.playAnimation()
        }
    }

    fun setMessage(message: String) {
        tvErrorMessage.text = message
    }

    fun setRetryClickListener(retryClickListener: () -> Unit) {
        this.retryClickListener = retryClickListener
    }
}