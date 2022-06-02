package ru.tensor.testingapplication.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.tensor.testingapplication.domain.Article
import ru.tensor.testingapplication.domain.ArticleRepository
import ru.tensor.testingapplication.ui.main.data.ArticleMapper
import ru.tensor.testingapplication.ui.main.data.ScreenState
import utils.TrampolineSchedulerRule

/**
 * Класс теста VM
 * Его экземпляр будет создаваться для каждого @Test метода (т.е нет смысла пытаться хранить промежуточное состояние)
 */
class MainViewModelTest {

    /*
     * Правило. Т.к в тестируемой VM используется rxJava2, необходимо добавить правило для работы с потоками
     */
    @get:Rule
    val rxRule = TrampolineSchedulerRule()

    /*
     * Правило. Т.к в тестируемой VM используется LiveData, необходимо добавить правило для работы с потоками.
     * Обычно хватает первого.
     */
    @get:Rule
    val liveDataRule = InstantTaskExecutorRule()

    // Заставим репозиторий возвращать только данный ответ
    private val testArticle = Article(
        title = "title",
        subtitle = "subtitle",
        descriptions = "description",
        author = "author",
        views = 50,
        likes = 200
    )

    /*
     * Моки. На моках можно вызывать [verify]
     * В отсутствие подключенного Mockito есть возможность использовать
     * Fake - тестовую реализацию интерфейса
     */
    private val mockRepository: ArticleRepository = mock {
        on { getArticle() } doReturn Single.just(testArticle)
    }
    // Маппер принимает context, но в данном случае его можно замокать
    private val mockMapper: ArticleMapper = spy(ArticleMapper(mock()))
    // Подписка на стороне фрагмента, но в тесте мы подписываемся сразу в VM при необходимости
    private val mockStateObserver: Observer<ScreenState> = mock()
    private lateinit var vm: MainViewModel

    /**
     * Вызывается перед выполнением метода с аннотацией @Test
     */
    @Before
    fun setUp() {
        /*
         * Spy. Использует реальный объект vm, тоже можно вызывать [verify]
         */
        vm = spy(MainViewModel(mockRepository, mockMapper)).apply {
            state.observeForever(mockStateObserver)
        }
    }

    /**
     * Вызывается после метода с аннотацией @Test
     * В примере ничего не делает, для сведения
     */
    @After
    fun tearDown() = Unit

    @Test
    fun `On vm init request data from repository`() {
        // verify
        /* Здесь уже отработал метод [setUp] и vm инициализирована.
        * В текущей реализации есть запрос в репозиторий в момент инициализации
        * Проверим что ответ лежит в нужном месте  */
        assertFalse(vm.state.value!!.isEmpty) //JUnit assert
        //Проверим остатки модели
        assertEquals("title", vm.state.value!!.title)
        assertEquals("subtitle", vm.state.value!!.subtitle)
        assertEquals("description", vm.state.value!!.descriptions)
        assertEquals("author", vm.state.value!!.author)
        /* Т.к используется настоящий объект маппера(который передается в VM),
         * то согласно реализации меняются типы у 2 полей ниже */
        assertEquals("50", vm.state.value!!.views)
        assertEquals("200", vm.state.value!!.likes)
        // Проверим что действительно вызывался метод репозитория
        verify(mockRepository, times(1)).getArticle() //Mockito verify
    }

    @Test
    fun `On vm refresh, increase counter`(){
        assertEquals(0, vm.refreshCounts)
        // act
        vm.refresh()
        // verify
        assertEquals(1, vm.refreshCounts)
    }

    @Test
    fun `On vm refresh, reset screen state`(){
        // act
        vm.refresh()
        // verify
        verify(mockStateObserver).onChanged(ScreenState.getEmpty())
    }

    @Test
    fun `On vm refresh after reset, request data update`(){
        // act
        vm.refresh()
        // verify
        // 2 т.к при init{} тоже есть запрос
        verify(mockRepository, times(2)).getArticle()
    }

    // my homework test is this ↓
    @Test
    fun `On vm init, accessing the repository and attempt to save the author of the article`() {
        // verify
        // Проверим что действительно  идет обращение в репозиторий c попыткой сохранения автора статьи
        verify(mockRepository, times(1)).saveArticleAuthor(testArticle)
    }
}