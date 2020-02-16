package com.jesvs.geoquiz

import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {

    var currentIndex = 0
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

    fun getScore(): Double {
        var score = 0.0
        for ((i, s) in responseBank.withIndex()) {
            if (s == questionBank[i].answer) {
                score++
            }
        }
        return score / questionBank.size * 100
    }

    private val responseBank = mutableListOf<Boolean?>(null, null, null, null, null, null)

}