package ru.mamykin.foboreader.core.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import org.koin.android.ext.android.inject
import ru.mamykin.foboreader.core.R

/**
 * Created by terrakok 25.11.16
 */
abstract class TabContainerFragment(
    private val fragmentTag: String
) : Fragment(), RouterProvider, BackPressedListener {

    private val navigator: Navigator by lazy {
        AppNavigator(requireActivity(), R.id.ftc_container, childFragmentManager)
    }

    private val ciceroneHolder: LocalCiceroneHolder by inject()

    override val router: Router
        get() = cicerone.router

    protected val cicerone: Cicerone<Router>
        get() = ciceroneHolder.getCicerone(fragmentTag)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tab_container, container, false)
    }

    override fun onResume() {
        super.onResume()
        cicerone.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        cicerone.getNavigatorHolder().removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        router.exit()
    }
}