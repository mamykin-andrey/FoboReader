package ru.mamykin.foboreader.uikit

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import ru.mamykin.foboreader.core.extension.dpToPx
import ru.mamykin.foboreader.core.extension.getLayoutInflater
import ru.mamykin.foboreader.core.extension.safeThrow
import ru.mamykin.foboreader.uikit.databinding.ViewErrorStubContentBinding

class ErrorStubWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private var retryClickListener: (() -> Unit)? = null
    private val binding = ViewErrorStubContentBinding.inflate(getLayoutInflater(), this)
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

        binding.btnRetry.setOnClickListener {
            retryClickListener?.invoke() ?: safeThrow(
                IllegalStateException("Retry click listener not set!")
            )
        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.VISIBLE) {
            binding.lavImage.playAnimation()
        }
    }

    fun setMessage(message: String) {
        binding.tvErrorMessage.text = message
    }

    fun setRetryClickListener(retryClickListener: () -> Unit) {
        this.retryClickListener = retryClickListener
    }
}