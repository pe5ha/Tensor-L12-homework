package ru.tensor.testingapplication.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.disposables.SerialDisposable
import ru.tensor.testingapplication.domain.ArticleRepository
import ru.tensor.testingapplication.ui.main.data.ArticleMapper
import ru.tensor.testingapplication.ui.main.data.ScreenState

class MainViewModel(
    private val repository: ArticleRepository,
    private val mapper: ArticleMapper
) : ViewModel() {

    private val _state = MutableLiveData(ScreenState.getEmpty())
    val state: LiveData<ScreenState> = _state

    private val disposables = SerialDisposable()

    // Допустим есть необходимость хранить количество обновлений
    var refreshCounts = 0
        private set

    init {
        fillScreen()
    }

    fun refresh() {
        refreshCounts++
        resetState()
        fillScreen()
    }

    private fun resetState() {
        _state.value = ScreenState.getEmpty()
    }

    private fun fillScreen() {
        disposables.set(
            repository.getArticle()
                .doOnSubscribe { _state.value = _state.value?.copy(inProgress = true) }
                .doFinally { _state.value = _state.value?.copy(inProgress = false) }
                .subscribe { article ->
                    _state.value = mapper.apply(article)
                    repository.saveArticleAuthor(article)
                }
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}

/**
 * Фабрика VM
 * Предполагается что в **фабрику** происходит инъекция зависимостей
 * В рамках текущего примера, фабрика создается по месту использования (в [MainFragment]), что не очень хорошо
 */
@Suppress("UNCHECKED_CAST")
class MainVmFactory(
    private val repository: ArticleRepository,
    private val mapper: ArticleMapper,
) : ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = MainViewModel(
        repository,
        mapper
    ) as VM
}