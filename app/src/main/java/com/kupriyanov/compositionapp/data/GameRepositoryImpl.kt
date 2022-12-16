package com.kupriyanov.compositionapp.data

import com.kupriyanov.compositionapp.domain.entities.GameSettings
import com.kupriyanov.compositionapp.domain.entities.Level
import com.kupriyanov.compositionapp.domain.entities.Question
import com.kupriyanov.compositionapp.domain.repository.GameRepository
import java.lang.Integer.min
import java.lang.Integer.max
import kotlin.random.Random

object GameRepositoryImpl : GameRepository {

    private const val MIN_SUM_VALUE = 2
    private const val MIN_ANSWER_VALUE = 1
    private const val SPREAD_AROUND_RIGHT_ANSWER = 15

    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
        val sum = Random.nextInt(MIN_SUM_VALUE, maxSumValue + 1)
        val visibleNumber = Random.nextInt(MIN_ANSWER_VALUE, sum)
        val options = HashSet<Int>()
        val rightAnswer = sum - visibleNumber
        options.add(rightAnswer)
        val from = max(rightAnswer - SPREAD_AROUND_RIGHT_ANSWER, MIN_ANSWER_VALUE)
        val to = min(maxSumValue, rightAnswer + SPREAD_AROUND_RIGHT_ANSWER)
        while (options.size < countOfOptions) {
            options.add(Random.nextInt(from, to))
        }
        return Question(sum, visibleNumber, options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when (level) {
            Level.EASY -> {
                GameSettings(
                    10,
                    10,
                    70,
                    60
                )
            }
            Level.NORMAL -> {
                GameSettings(
                    100,
                    10,
                    75,
                    60
                )
            }
            Level.HARD -> {
                GameSettings(
                    1000,
                    10,
                    80,
                    75
                )
            }
            Level.VERY_HARD -> {
                GameSettings(
                    10000,
                    7,
                    85,
                    90
                )
            }
        }
    }
}