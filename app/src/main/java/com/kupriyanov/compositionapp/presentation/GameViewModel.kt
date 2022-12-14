package com.kupriyanov.compositionapp.presentation

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kupriyanov.compositionapp.data.GameRepositoryImpl
import com.kupriyanov.compositionapp.domain.entities.GameSettings
import com.kupriyanov.compositionapp.domain.entities.Level
import com.kupriyanov.compositionapp.domain.entities.Question
import com.kupriyanov.compositionapp.domain.usecases.GenerateQuestionUseCase
import com.kupriyanov.compositionapp.domain.usecases.GetGameSettingsUseCase

class GameViewModel : ViewModel() {
    private val repository = GameRepositoryImpl

    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)

    private val _gameSettings = MutableLiveData<GameSettings>()
    val gameSettings: LiveData<GameSettings>
        get() = _gameSettings

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private var _timer = MutableLiveData<String>()
    val timer: LiveData<String>
        get() = _timer

    private var _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    private var _progressBarTint = MutableLiveData<Int>()
    val progressBarTint: LiveData<Int>
        get() = _progressBarTint

    private var _isReachThresholdOfRightResponse = MutableLiveData<Boolean>(false)
    val isReachThresholdOfRightResponse: LiveData<Boolean>
        get() = _isReachThresholdOfRightResponse

    private var launchTimer: CountDownTimer? = null
    private var countResponse = 0

    private val _shouldLaunchGameFinishedFragment = MutableLiveData(
        false
    )
    val shouldLaunchGameFinishedFragment: LiveData<Boolean>
        get() = _shouldLaunchGameFinishedFragment

    fun setupGameSettings(level: Level): GameSettings {
        _gameSettings.value = getGameSettingsUseCase(level)
        return getGameSettingsUseCase(level)
    }

    fun generateQuestion() {
        _question.value = gameSettings.value?.let {
            generateQuestionUseCase(it.maxSumValue)
        }
    }

    fun isRightAnswer(answer: String, minCountOfRightAnswers: Int) {
        question.value?.let {
            if ((it.sum - it.visibleNumber) == answer.toInt()) {
                _score.value = _score.value?.inc()
            }
        }
        countResponse++
        _score.value?.let {
            if (it >= minCountOfRightAnswers) {
                _isReachThresholdOfRightResponse.value = true
            }
        }
    }

    fun launchTimer(timeInSeconds: Int) {
        val time = (timeInSeconds * 1000).toLong()
        launchTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(milliseconds: Long) {
                val s: Long = milliseconds % 60000 / 1000
                val m: Long = milliseconds / 60000
                _timer.value = String.format("%02d:%02d", m, s)
            }

            override fun onFinish() {
                launchTimer = null
                _shouldLaunchGameFinishedFragment.value = true
            }
        }.start()
    }

    fun countPercentOfRightAnswers(minPercentOfRightAnswers: Int) {
        _percentOfRightAnswers.value = ((_score.value?.times(100))?.div(countResponse))
        _percentOfRightAnswers.value?.let {
            if (it >= minPercentOfRightAnswers) {
                _progressBarTint.value = android.R.color.holo_green_light
            } else {
                _progressBarTint.value = android.R.color.holo_red_light
            }
        }

    }

    fun resetShouldLaunchGameFinished() {
        _shouldLaunchGameFinishedFragment.value = false
    }

    fun resetScore() {
        _score.value = 0
        countResponse = 0
        _percentOfRightAnswers.value = 0
    }

    fun resetTimer() {
        launchTimer?.cancel()
    }

    fun resetIsReachThreshold() {
        _isReachThresholdOfRightResponse.value = false
    }
}

