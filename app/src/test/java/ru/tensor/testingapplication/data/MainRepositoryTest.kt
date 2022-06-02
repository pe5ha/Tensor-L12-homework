package ru.tensor.testingapplication.data

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.tensor.testingapplication.domain.Article

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P], manifest = Config.NONE)
class MainRepositoryTest {
    //Контекст из Robolectric
    private val context: Context = ApplicationProvider.getApplicationContext()
    //SharedPreferences из Robolectric
    private val prefs = context.getSharedPreferences("testName", Context.MODE_PRIVATE)

    // Здесь не получится использовать mock() заместо prefs, т.к unit тесты не имеют реализации android.jar - будет исключение.
    // Но можно это частично исправить с помощью Robolectric
    private val repo = MainRepository(prefs)

    @Test
    fun `Save author of article`(){
        // act
        repo.saveArticleAuthor(Article(author = "Tensor"))
        // verify
        val savedAuthor = repo.getArticleAuthor()
        assertEquals("Tensor", savedAuthor)

    }
}