package ru.tensor.testingapplication.ui.main.data

import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.tensor.testingapplication.domain.Article

/**
 * Тест маппера
 */
class ArticleMapperTest {

    // Тут нужен просто контекст, его можно спокойно мокать
    private val mapper = ArticleMapper(mock())

    @Test
    fun `Correct title transformation`() {
        // act
        val result = mapper.apply(Article("TestTitle"))
        // verify
        assertEquals("TestTitle", result.title)
    }

    @Test
    fun `Correct subtitle transformation`() {
        // act
        val result = mapper.apply(Article(subtitle = "TestSubTitle"))
        // verify
        assertEquals("TestSubTitle", result.subtitle)
    }

    @Test
    fun `Correct likes and views transformation`() {
        // act
        val result = mapper.apply(Article(likes = 505, views = 1001))
        // verify
        assertEquals("505", result.likes)
        assertEquals("1001", result.views)
    }

    //etc.
}