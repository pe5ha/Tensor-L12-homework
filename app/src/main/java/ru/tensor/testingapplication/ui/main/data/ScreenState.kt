package ru.tensor.testingapplication.ui.main.data

data class ScreenState(
    val title: String = "",
    val subtitle: String = "",
    val descriptions: String = "",
    val author: String = "",
    val views: String = "",
    val likes: String = "",
    val inProgress: Boolean = false
) {

    val isEmpty: Boolean
        get() = this == getEmpty()

    companion object {
        fun getEmpty() = ScreenState()
    }
}