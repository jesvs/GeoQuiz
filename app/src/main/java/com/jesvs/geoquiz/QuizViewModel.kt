package com.jesvs.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {

    var currentIndex = 0
    val isCheater: Boolean
    get() = cheatBank[currentIndex]

    init {
        Log.d(TAG, "QuizViewModel init() called $this")
    }

    private val questionBank = listOf(
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true),
        Question(R.string.question_australia, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_oceans, true)
    )

    val currentQuestionAnswer: Boolean
    get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
    get() = questionBank[currentIndex].textResId

    val currentResponse: Boolean?
    get() = responseBank[currentIndex]

    val testCompleted: Boolean
    get() {
        return !responseBank.contains(null)
    }

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        currentIndex -=1
        if (currentIndex < 0) {
            currentIndex = questionBank.size - 1
        }
    }

    fun storeResponse(response: Boolean) {
        responseBank[currentIndex] = response
    }

    fun cheated() {
        cheatBank[currentIndex] = true
    }

    fun cheatsRemaining(): Int {
        var r = 3
        for (c in cheatBank) {
            if (c) {
                r--
            }
            if (r <= 0) {
                return 0
            }
        }
        return r
    }

    fun getScore(): Double {
        var score = 0.0
        for ((i, s) in responseBank.withIndex()) {
            if (s == questionBank[i].answer) {
                score++
            }
        }
        return score / questionBank.size * 100
    }

    private var responseBank = MutableList<Boolean?>(questionBank.size) { null }
    private var cheatBank = MutableList<Boolean>(questionBank.size) { false }
}