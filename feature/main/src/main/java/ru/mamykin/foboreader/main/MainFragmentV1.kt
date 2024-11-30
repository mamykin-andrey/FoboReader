// package ru.mamykin.foboreader.main
//
// import android.os.Bundle
// import android.view.View
// import androidx.fragment.app.Fragment
// import ru.mamykin.foboreader.core.extension.apiHolder
// import ru.mamykin.foboreader.core.presentation.autoCleanedValue
// import ru.mamykin.foboreader.main.databinding.FragmentMainBinding
//
// class MainFragmentV1 : Fragment(R.layout.fragment_main) {
//
//     private val binding by autoCleanedValue { FragmentMainBinding.bind(requireView()) }
//     private val tabFragmentProvider by lazy { apiHolder().mainApi().tabFragmentProvider() }
//     private var isFirstLaunch = true
//
//     private fun getCurrentTabFragment(): Fragment? {
//         return childFragmentManager.fragments.firstOrNull { it.isVisible }
//     }
//
//     override fun onCreate(savedInstanceState: Bundle?) {
//         super.onCreate(savedInstanceState)
//         isFirstLaunch = savedInstanceState == null
//     }
//
//     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//         super.onViewCreated(view, savedInstanceState)
//         setupNavigationBar()
//
//         if (isFirstLaunch) {
//             isFirstLaunch = false
//             selectTab(Tab.MyBooks, addToBackStack = false)
//         }
//     }
//
//     private fun setupNavigationBar() {
//         binding.bnvTabs.setOnNavigationItemSelectedListener { item ->
//             when (item.itemId) {
//                 R.id.my_books -> selectTab(Tab.MyBooks)
//                 R.id.books_store -> selectTab(Tab.BooksStore)
//                 R.id.settings -> selectTab(Tab.Settings)
//             }
//             true
//         }
//     }
//
//     private fun selectTab(tab: Tab, addToBackStack: Boolean = true) {
//         val currentFragment = getCurrentTabFragment()
//         val fragmentToShow = childFragmentManager.findFragmentByTag(tab.tag)
//         if (currentFragment != null && currentFragment == fragmentToShow) return
//
//         val transaction = childFragmentManager.beginTransaction()
//         if (fragmentToShow == null) {
//             transaction.add(
//                 R.id.fr_tabs_host,
//                 tab.newFragment(tabFragmentProvider),
//                 tab.tag,
//             )
//         }
//         currentFragment?.let(transaction::hide)
//         fragmentToShow?.let(transaction::show)
//         if (addToBackStack) {
//             transaction.addToBackStack(null)
//         }
//         transaction.commit()
//     }
// }