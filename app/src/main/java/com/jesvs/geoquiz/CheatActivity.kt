package com.jesvs.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jesvs.geoquiz.databinding.ActivityCheatBinding

const val EXTRA_ANSWER_SHOWN = "com.jesvs.geoquiz.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE = "com.jesvs.geoquiz.answer_is_true"
private const val KEY_CHEATED = "cheater"

class CheatActivity : AppCompatActivity() {

    private var isCheater = false
    private var answerIsTrue = false
    private lateinit var binding: ActivityCheatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheatBinding.inflate(layoutInflater)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        binding.apiLevelTextView.text = getString(R.string.api_level, Build.VERSION.SDK_INT)

        isCheater = savedInstanceState?.getBoolean(KEY_CHEATED, false) ?: false
        if (isCheater) {
            showAnswer()
        }

        binding.showAnswerButton.setOnClickListener {
            showAnswer()
        }

        setContentView(binding.root)
    }

    private fun showAnswer() {
        isCheater = true
        binding.showAnswerButton.isEnabled = false
        val answerText = when {
            answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }
        binding.answerTextView.setText(answerText)
        setAnswerShownResult()
    }

    private fun setAnswerShownResult() {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isCheater)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_CHEATED, isCheater)
    }
}
