package ru.mamykin.widget.paginatedtextview.view

/**
 * Interface definition for a callback to be invoked when user make a swipe
 */
interface OnSwipeListener {

    /**
     * Swipe from right to left
     */
    fun onSwipeLeft()

    /**
     * Swipe from left to right
     */
    fun onSwipeRight()
}