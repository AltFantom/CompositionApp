package com.kupriyanov.compositionapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.kupriyanov.compositionapp.R
import com.kupriyanov.compositionapp.databinding.FragmentGameFinishedBinding
import com.kupriyanov.compositionapp.domain.entities.GameResult

class GameFinishedFragment : Fragment() {

    private lateinit var gameResult: GameResult

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addOnBackPressedCallback()
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

    private fun addOnBackPressedCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                retryGame()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
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

    private fun parseArgs() {
        requireArguments().getParcelable<GameResult>(KEY_GAME_RESULT)?.let {
            gameResult = it
        }
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    companion object {

        private const val KEY_GAME_RESULT = "game_result"

        fun newInstance(
            gameResult: GameResult
        ): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
        }
    }
}