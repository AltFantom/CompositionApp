package com.kupriyanov.compositionapp.domain.repository

import com.kupriyanov.compositionapp.domain.entities.GameSettings
import com.kupriyanov.compositionapp.domain.entities.Level
import com.kupriyanov.compositionapp.domain.entities.Question

interface GameRepository {

    fun generateQuestion(
        maxSumValue: Int,
        countOfOptions: Int
    ): Question

    fun getGameSettings(level: Level): GameSettings
}