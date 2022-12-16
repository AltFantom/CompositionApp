package com.kupriyanov.compositionapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.kupriyanov.compositionapp.R
import com.kupriyanov.compositionapp.databinding.FragmentGameFinishedBinding

class GameFinishedFragment : Fragment() {

    private val args by navArgs<GameFinishedFragmentArgs>()

    private val gameResult by lazy {
        args.gameResult
    }

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonClickListener()
        bindViews()
    }

    private fun bindViews() {
        setEmoji()
        setRequiredScore()
        setScoreAnswers()
        setRequiredPercentage()
        setScorePercentage()
    }

    private fun setScorePercentage() {
        val percentage = gameResult.percentOfRightAnswers
        binding.scorePercentage.text = String.format(
            resources.getString(R.string.score_percentage),
            percentage
        )
    }

    private fun setRequiredPercentage() {
        val requiredPercentage = gameResult.gameSettings.minPercentOfRightAnswers
        binding.tvRequiredPercentage.text = String.format(
            resources.getString(R.string.required_percentage),
            requiredPercentage
        )
    }

    private fun setScoreAnswers() {
        val score = gameResult.countOfRightAnswers
        binding.tvScoreAnswers.text = String.format(
            resources.getString(R.string.score_answers),
            score
        )
    }

    private fun setRequiredScore() {
        val minCount = gameResult.gameSettings.minCountOfRightAnswers
        binding.requiredScore.text = String.format(
            resources.getString(R.string.required_score),
            minCount
        )
    }

    private fun setEmoji() {
        val emojiResId = if (gameResult.winner) {
            R.drawable.ic_smile
        } else {
            R.drawable.ic_sad
        }
        Glide.with(this).load(emojiResId).into(binding.emojiResult)
    }

    private fun setButtonClickListener() {
        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retryGame() {
        findNavController().popBackStack()
    }
}