package com.jesvs.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView

    private val questionBank = listOf<Question>(
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true),
        Question(R.string.question_australia, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_oceans, true)
    )

    private val responseBank = mutableListOf<Boolean?>(null, null, null, null, null, null)

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

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

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun nextQuestion() {
        currentIndex = (currentIndex + 1) % questionBank.size
        updateQuestion()
        updateAnswerButtons()
    }

    private fun prevQuestion() {
        currentIndex -= 1
        if (currentIndex < 0) {
            currentIndex = questionBank.size - 1
        }
        updateQuestion()
        updateAnswerButtons()
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    private fun updateAnswerButtons() {
        val unanswered = responseBank[currentIndex] == null
        trueButton.isEnabled = unanswered
        falseButton.isEnabled = unanswered
    }

    private fun checkAnswer(userAnswer: Boolean) {
        responseBank[currentIndex] = userAnswer
        val correctAnswer = questionBank[currentIndex].answer

        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        showToast(messageResId)
    }

    private fun checkResult() {
        var score = 0
        for ((i, r) in responseBank.withIndex()) {
            if (r == null) {
                return
            }
            if (questionBank[i].answer == r) {
                score++
            }
        }

        val total = questionBank.size
        val percentage = score / total.toFloat() * 100

        Toast.makeText(this, "You answered $score right out of ${total}. Your score is ${percentage} %.", Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.TOP, 0, 200)
        }.show()
    }

    private fun showToast(@StringRes resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).apply {
            setGravity(Gravity.TOP, 0, 200)
        }.show()
    }
}