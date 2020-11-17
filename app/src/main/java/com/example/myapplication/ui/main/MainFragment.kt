package com.example.myapplication.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myapplication.R
import com.example.myapplication.ui.main.state.MainIntent.GetQuestions
import com.example.myapplication.ui.main.state.UiState
import com.example.myapplication.ui.main.state.UiState.*
import com.example.myapplication.ui.questions.QuestionsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var button: Button

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button = view.findViewById(R.id.button)
        progressBar = view.findViewById(R.id.progress_bar)

        button.setOnClickListener {
            // 1. Send events
            viewModel.handleIntent(GetQuestions)
        }

        // 2. Observe state
        viewModel.uiState.observe(viewLifecycleOwner, {
            render(it)
        })

    }

    private fun render(state: UiState) {
        when (state) {
            is Complete -> {
                progressBar.visibility = View.INVISIBLE
                // navigate to next screen
                activity?.startActivity(
                    Intent(activity, QuestionsActivity::class.java)
                )
                activity?.finish()
            }
            is Loading -> {
                progressBar.visibility = View.VISIBLE
                button.isEnabled = false
            }
            is Error -> {
                progressBar.visibility = View.INVISIBLE
                button.isEnabled = true
                Toast.makeText(activity, resources.getString(R.string.main_toast_error_text), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}