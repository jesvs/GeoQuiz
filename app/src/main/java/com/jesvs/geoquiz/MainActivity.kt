package com.jesvs.geoquiz

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener {
            checkAnswer(true)
            checkResult()
            updateAnswerButtons()
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
            checkResult()
            updateAnswerButtons()
        }

        nextButton.setOnClickListener {
            nextQuestion()
        }

        prevButton.setOnClickListener {
            prevQuestion()
        }

        questionTextView.setOnClickListener {
            nextQuestion()
        }

        updateQuestion()
        updateAnswerButtons()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun nextQuestion() {
        quizViewModel.moveToNext()
        updateQuestion()
        updateAnswerButtons()
    }

    private fun prevQuestion() {
        quizViewModel.moveToPrev()
        updateQuestion()
        updateAnswerButtons()
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun updateAnswerButtons() {
        val unanswered = quizViewModel.currentResponse == null
        trueButton.isEnabled = unanswered
        falseButton.isEnabled = unanswered
    }

    private fun checkAnswer(userAnswer: Boolean) {
        quizViewModel.storeResponse(userAnswer)

        val messageResId = if (userAnswer == quizViewModel.currentQuestionAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        showToast(messageResId)
    }

    private fun checkResult() {
        if (!quizViewModel.testCompleted) {
            return
        }
        val score = quizViewModel.getScore()
        Toast.makeText(this, "Your score is $score %.", Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.TOP, 0, 200)
        }.show()
    }

    private fun showToast(@StringRes resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).apply {
            setGravity(Gravity.TOP, 0, 200)
        }.show()
    }
}