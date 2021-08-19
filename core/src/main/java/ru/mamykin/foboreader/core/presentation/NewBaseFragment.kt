package ru.mamykin.foboreader.core.presentation

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import ru.mamykin.foboreader.core.R

@Deprecated("Use common extensions instead")
abstract class NewBaseFragment(
    @LayoutRes layoutRes: Int
) : Fragment(layoutRes) {

    protected val toolbar: Toolbar?
        get() = requireView().findViewById(R.id.toolbar)

    protected val progressView: FrameLayout
        get() = requireView().findViewById(R.id.fl_content_loading)
            ?: throw IllegalStateException("Couldn't find progress view in layout!")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar?.setNavigationOnClickListener { requireActivity().onBackPressed() }
    }
}