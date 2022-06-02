package ru.tensor.testingapplication.data

import android.content.SharedPreferences
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.tensor.testingapplication.domain.Article
import ru.tensor.testingapplication.domain.ArticleRepository
import java.util.concurrent.TimeUnit

/**
 * Реализация репозитория
 * Допустим есть необходимость прокидывать напрямую класс из android - SharedPreferences
 * @see MainRepositoryTest
 */
class MainRepository(private val sharedPreferences: SharedPreferences) : ArticleRepository {

    override fun getArticle(): Single<Article> =
        Single.fromCallable {
            getTestArticle()
        }.subscribeOn(Schedulers.io())
            .delay(2, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())


    override fun saveArticleAuthor(article: Article) {
        sharedPreferences.edit().putString(PREF_AUTHOR_KEY, article.author).apply()
    }

    override fun getArticleAuthor(): String {
        return sharedPreferences.getString(PREF_AUTHOR_KEY, "").orEmpty()
    }

    /**
     * Для примера думаем, что ответ пришел с сервера
     */
    private fun getTestArticle(): Article =
        Article(
            title = "Types of tests in Android",
            subtitle = "Mobile applications are complex and must work well in many environments. As such, there are many types of tests",
            descriptions = "Subject\n" +
                    "For example, there are different types of tests depending on the subject:\n" +
                    "\n" +
                    "Functional testing: does my app do what it's supposed to?\n" +
                    "Performance testing: does it do it quickly and efficiently?\n" +
                    "Accessibility testing: does it work well with accessibility services?\n" +
                    "Compatibility testing: does it work well on every device and API level?\n" +
                    "Scope\n" +
                    "Tests also vary depending on size, or degree of isolation:\n" +
                    "\n" +
                    "Unit tests or small tests only verify a very small portion of the app, such as a method or class.\n" +
                    "End-to-end tests or big tests verify larger parts of the app at the same time, such as a whole screen or user flow.\n" +
                    "Medium tests are in between and check the integration between two or more units.",
            views = 51_445,
            author = "Jake Wharton",
            likes = 42
        )

    private companion object {
        const val PREF_AUTHOR_KEY = "PREF_AUTHOR_KEY"
    }
}