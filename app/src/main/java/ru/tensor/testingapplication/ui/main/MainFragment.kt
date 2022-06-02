package ru.tensor.testingapplication.ui.main

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.tensor.testingapplication.R
import ru.tensor.testingapplication.data.MainRepository
import ru.tensor.testingapplication.ui.main.data.ArticleMapper
import ru.tensor.testingapplication.ui.main.data.ScreenState

class MainFragment : Fragment(R.layout.main_fragment) {

    private lateinit var viewModel: MainViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //Предполагается, что произошла инъекция фабрики VM
        val appContext = requireContext().applicationContext
        val prefs = appContext.getSharedPreferences("testName", Context.MODE_PRIVATE)
        val factory = MainVmFactory(MainRepository(prefs), ArticleMapper(appContext))

        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        viewModel.state.observe(viewLifecycleOwner, ::applyUiState)
    }

    private fun applyUiState(screenState: ScreenState) = view?.run {
        val progressView = findViewById<ProgressBar>(R.id.progress)
        val titleView = findViewById<TextView>(R.id.title)
        val subtitleView = findViewById<TextView>(R.id.subtitle)
        val descView = findViewById<TextView>(R.id.description)
        val likeCountView = findViewById<TextView>(R.id.like_count)
        val viewsCountView = findViewById<TextView>(R.id.view_count)
        val likeIconView = findViewById<ImageView>(R.id.ico_likes)
        val viewIconView = findViewById<ImageView>(R.id.ico_views)
        val refreshView = findViewById<ImageView>(R.id.ico_refresh)

        if (screenState.inProgress) {
            progressView?.visibility = View.VISIBLE
            likeIconView?.visibility = View.GONE
            viewIconView?.visibility = View.GONE
        } else {
            progressView?.visibility = View.GONE
            likeIconView?.visibility = View.VISIBLE
            viewIconView?.visibility = View.VISIBLE
        }
        titleView?.text = screenState.title
        subtitleView?.text = screenState.subtitle
        descView?.text = screenState.descriptions
        likeCountView?.text = screenState.likes
        viewsCountView?.text = screenState.views

        refreshView.setOnClickListener { viewModel.refresh() }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}