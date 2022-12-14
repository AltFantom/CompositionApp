package com.kupriyanov.compositionapp.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kupriyanov.compositionapp.R
import com.kupriyanov.compositionapp.databinding.FragmentGameBinding
import com.kupriyanov.compositionapp.domain.entities.GameResult
import com.kupriyanov.compositionapp.domain.entities.GameSettings
import com.kupriyanov.compositionapp.domain.entities.Level

class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var level: Level
    private lateinit var textProgressAnswers: String
    private lateinit var gameSettings: GameSettings
    private var score = 0
    private var countOfQuestions = 0

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseParams()
        setupGameSettingsObserver()
        viewModel.setupGameSettings(level)
        viewModel.generateQuestion()
        viewModel.launchTimer(gameSettings.gameTimeInSeconds)
        setupObserves()
        setupClickListeners()
        addOnBackPressedCallback()
        setupSecondaryProgressOnProgressBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseParams() {
        viewModel = ViewModelProvider(requireActivity())[GameViewModel::class.java]
        textProgressAnswers = binding.tvProgressAnswers.text.toString()
    }

    private fun setupSecondaryProgressOnProgressBar() {
        binding.progressBar.secondaryProgress = gameSettings.minPercentOfRightAnswers
    }

    private fun addOnBackPressedCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.popBackStack()
                resetValues()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun setupObserves() {
        setupQuestionObserver()
        setupScoreObserver()
        setupTimerObserver()
        setupShouldLaunchGameFinishedFragmentObserver()
        setupPercentOfRightAnswersObserver()
        setupProgressBarTintObserver()
        setupIsReachThresholdOfRightResponseObserver()
        setupCountResponseObserver()
    }

    private fun setupGameSettingsObserver() {
        viewModel.gameSettings.observe(viewLifecycleOwner) {
            gameSettings = it
        }
    }

    private fun setupShouldLaunchGameFinishedFragmentObserver() {
        viewModel.shouldLaunchGameFinishedFragment.observe(viewLifecycleOwner) {
            if (it) {
                val minPercentOfRightAnswers = gameSettings.minPercentOfRightAnswers
                val minCountOfRightAnswers = gameSettings.minCountOfRightAnswers
                launchGameFinishedFragment(
                    GameResult(
                        viewModel.isWinner(minPercentOfRightAnswers, minCountOfRightAnswers),
                        score,
                        countOfQuestions,
                        gameSettings
                    )
                )
            }
        }
    }

    private fun setupIsReachThresholdOfRightResponseObserver() {
        viewModel.isReachThresholdOfRightResponse.observe(viewLifecycleOwner) {
            if (it) {
                val colorId = android.R.color.holo_green_light
                binding.tvProgressAnswers.setTextColor(resources.getColor(colorId, null))
            }
        }
    }

    private fun setupProgressBarTintObserver() {
        viewModel.progressBarTint.observe(viewLifecycleOwner) {
            binding.progressBar.progressTintList = ColorStateList.valueOf(
                resources.getColor(it, null)
            )
        }
    }

    private fun setupCountResponseObserver() {
        viewModel.countResponse.observe(viewLifecycleOwner) {
            countOfQuestions = it
        }
    }

    private fun setupPercentOfRightAnswersObserver() {
        viewModel.percentOfRightAnswers.observe(viewLifecycleOwner) {
            binding.progressBar.progress = it
        }
    }

    private fun setupTimerObserver() {
        viewModel.timer.observe(viewLifecycleOwner) {
            binding.tvTimer.text = it
        }
    }

    private fun setupScoreObserver() {
        viewModel.score.observe(viewLifecycleOwner) {
            score = it
            binding.tvProgressAnswers.text = String.format(
                textProgressAnswers,
                it,
                gameSettings.minCountOfRightAnswers
            )
        }
    }

    private fun setupQuestionObserver() {
        viewModel.question.observe(viewLifecycleOwner) {
            with(binding) {
                tvSum.text = it.sum.toString()
                tvLeftNumber.text = it.visibleNumber.toString()
                tvOption1.text = it.options[0].toString()
                tvOption2.text = it.options[1].toString()
                tvOption3.text = it.options[2].toString()
                tvOption4.text = it.options[3].toString()
                tvOption5.text = it.options[4].toString()
                tvOption6.text = it.options[5].toString()
            }
        }
    }

    private fun setupClickListeners() {
        setupOptionsClickListeners()
    }

    private fun setupOptionsClickListeners() {
        with(binding) {
            tvOption1.setOnClickListener {
                doAfterClickAnyOption(tvOption1.text.toString())
            }
            tvOption2.setOnClickListener {
                doAfterClickAnyOption(tvOption2.text.toString())
            }
            tvOption3.setOnClickListener {
                doAfterClickAnyOption(tvOption3.text.toString())
            }
            tvOption4.setOnClickListener {
                doAfterClickAnyOption(tvOption4.text.toString())
            }
            tvOption5.setOnClickListener {
                doAfterClickAnyOption(tvOption5.text.toString())
            }
            tvOption6.setOnClickListener {
                doAfterClickAnyOption(tvOption6.text.toString())
            }
        }
    }

    private fun doAfterClickAnyOption(optionText: String) {
        val minCountOfRightAnswers = gameSettings.minCountOfRightAnswers
        with(viewModel) {
            isRightAnswer(optionText, minCountOfRightAnswers)
            generateQuestion()
            countPercentOfRightAnswers(this@GameFragment.gameSettings.minPercentOfRightAnswers)
        }
    }


    private fun launchGameFinishedFragment(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
        resetValues()
    }

    private fun resetValues() {
        viewModel.resetShouldLaunchGameFinished()
        viewModel.resetScore()
        viewModel.resetTimer()
        viewModel.resetIsReachThreshold()
    }

    private fun parseArgs() {
        requireArguments().getParcelable<Level>(LEVEL_KEY)?.let {
            level = it
        }
    }

    companion object {
        const val NAME = "GameFragment"
        private const val LEVEL_KEY = "level"

        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(LEVEL_KEY, level)
                }
            }
        }
    }
}