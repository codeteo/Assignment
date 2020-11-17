package com.example.myapplication.ui.questions

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.ui.questions.state.NetworkResult.*
import com.example.myapplication.ui.questions.state.QuestionIntent.*
import com.example.myapplication.ui.questions.state.QuestionsUiState
import com.example.myapplication.utils.views.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_questions.*
import kotlinx.android.synthetic.main.toolbar_questions.*

@AndroidEntryPoint
class QuestionsActivity : AppCompatActivity() {

    private lateinit var viewModel: QuestionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        viewModel = ViewModelProvider(this)
            .get(QuestionsViewModel::class.java)

        btn_previous.setOnClickListener {
            viewModel.handleIntent(GetPreviousQuestion)
        }

        btn_next.setOnClickListener {
            viewModel.handleIntent(GetNextQuestion)
        }

        btn_submit.setOnClickListener {
            // 1. Send events
            viewModel.handleIntent(PostAnswer(et_answer.text.toString().trim()))
        }

        // 2. Observe state
        viewModel.uiState.observe(this, {
            render(it)
        })

        viewModel.isEmptyTextError.observe(this, {
            Toast.makeText(this, getString(R.string.toast_empty_answer_error_text), Toast.LENGTH_SHORT)
                .show()
        })

        // 3. initialize state
        viewModel.handleIntent(GetFirstQuestion)
    }

    private fun render(state: QuestionsUiState) {
        val index = state.index
        val questionText = state.questions[index].questionText
        val questionId = state.questions[index].id

        val answer = state.answers.firstOrNull {
            it.id == questionId
        }

        if (state.isLoading) {
            progress_bar.visibility = View.VISIBLE
            btn_submit.isEnabled = false

            // when loading disable prev/next btns
            btn_previous.isEnabled = false
            btn_next.isEnabled = false

            return
        }

        progress_bar.visibility = View.INVISIBLE
        tv_toolbar_pager.text = resources.getString(R.string.tv_toolbar_questions,
            state.index.plus(1), state.questions.size)

        when (index) {
            0 -> {
                btn_previous.isEnabled = false
                btn_next.isEnabled = true
            }
            state.questions.size.minus(1) -> {
                btn_previous.isEnabled = true
                btn_next.isEnabled = false
            }
            in 1..state.questions.size.minus(1) -> {
                btn_next.isEnabled = true
                btn_previous.isEnabled = true
            }
        }

        when (state.networkResult) {
            FAILURE -> {
                snackbar(resources.getString(R.string.snackbar_title_failure)).setAction(resources.getString(R.string.snackbar_btn_action_text)) {
                    viewModel.handleIntent(RetryRequest(et_answer.text.toString().trim()))
                }.show()
                btn_submit.isEnabled = true
            }
            SUCCESS -> {
                snackbar(resources.getString(R.string.snackbar_title_success)).show()
                btn_submit.isEnabled = false
            }
            NONE -> {
                btn_submit.isEnabled = true
                et_answer.setText("")
                btn_submit.text = resources.getString(R.string.btn_submit_text)
            }
        }

        // Texts
        tv_question_text.text = questionText
        tv_counter.text = resources.getString(R.string.tv_questions_answered, state.answers.size)

        if (answer!= null && answer.answerText.isNotEmpty()) {
            et_answer.setText(answer.answerText)
            btn_submit.isEnabled = false
            btn_submit.text = getString(R.string.btn_submit_submitted_text)
        } else {
            btn_submit.isEnabled = true
        }

    }
}