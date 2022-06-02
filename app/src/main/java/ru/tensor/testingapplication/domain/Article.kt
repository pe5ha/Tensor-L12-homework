package ru.tensor.testingapplication.domain

data class Article(
    val title: String = "",
    val subtitle: String = "",
    val descriptions: String = "",
    val author: String = "",
    val views: Long = 0,
    val likes: Long = 0,
)