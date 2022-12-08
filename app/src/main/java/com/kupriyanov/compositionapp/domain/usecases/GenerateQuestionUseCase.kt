package com.kupriyanov.compositionapp.domain.usecases

import com.kupriyanov.compositionapp.domain.entities.Question
import com.kupriyanov.compositionapp.domain.repository.GameRepository

class GenerateQuestionUseCase(private val gameRepository: GameRepository) {

    operator fun invoke(maxSumValue: Int): Question {
        return gameRepository.generateQuestion(maxSumValue, COUNT_OF_OPTIONS)
    }

    private companion object {
        private const val COUNT_OF_OPTIONS = 6
    }
}