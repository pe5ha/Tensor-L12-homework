package ru.tensor.testingapplication.domain

import io.reactivex.Single

interface ArticleRepository {

    fun getArticle(): Single<Article>

    fun saveArticleAuthor(article: Article)

    fun getArticleAuthor(): String

}