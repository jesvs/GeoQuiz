package com.jesvs.geoquiz

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jesvs.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        binding.trueButton.setOnClickListener {
            checkAnswer(true)
            checkResult()
            updateAnswerButtons()
        }

        binding.falseButton.setOnClickListener {
            checkAnswer(false)
            checkResult()
            updateAnswerButtons()
        }

        binding.nextButton.setOnClickListener {
            nextQuestion()
        }

        binding.prevButton.setOnClickListener {
            prevQuestion()
        }

        binding.cheatButton.setOnClickListener { view ->
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.i(TAG, "Showing reveal animation")
                val options = ActivityOptions.makeClipRevealAnimation(view, 0, 0, view.width, view.height)
                startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
            } else {
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
            }
        }

        binding.questionTextView.setOnClickListener {
            nextQuestion()
        }

        updateQuestion()
        updateAnswerButtons()
        updateCheatsRemaining()

        setContentView(binding.root)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            val isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            if (isCheater) {
                quizViewModel.cheated()
                updateCheatsRemaining()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
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
        binding.questionTextView.setText(questionTextResId)
    }

    private fun updateCheatsRemaining() {
        binding.cheatsRemainingTextView.text = resources.getString(R.string.cheats_remaining, quizViewModel.cheatsRemaining())
        if (quizViewModel.cheatsRemaining() == 0) {
            binding.cheatButton.isEnabled = false
        }
    }

    private fun updateAnswerButtons() {
        val unanswered = quizViewModel.currentResponse == null
        binding.trueButton.isEnabled = unanswered
        binding.falseButton.isEnabled = unanswered
    }

    private fun checkAnswer(userAnswer: Boolean) {
        quizViewModel.storeResponse(userAnswer)

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == quizViewModel.currentQuestionAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
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