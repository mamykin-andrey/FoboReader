package ru.mamykin.foboreader.uikit

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import ru.mamykin.foboreader.core.extension.safeThrow

// TODO: Decrease nesting of ViewGroups
class ErrorStubWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private var retryClickListener: (() -> Unit)? = null
    private val tvErrorMessage: TextView
    private val lavImage: LottieAnimationView
    private val btnRetry: Button

    init {
        inflate(context, R.layout.view_error_stub_content, this)
        tvErrorMessage = findViewById(R.id.tv_error_message)
        lavImage = findViewById(R.id.lav_image)
        btnRetry = findViewById(R.id.btn_retry)
        initViews()
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

    private fun initViews() {
        btnRetry.setOnClickListener {
            retryClickListener?.invoke() ?: safeThrow(
                IllegalStateException("Retry click listener not set!")
            )
        }
    }
}