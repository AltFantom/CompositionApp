package com.kupriyanov.compositionapp.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kupriyanov.compositionapp.R
import com.kupriyanov.compositionapp.databinding.FragmentGameBinding
import com.kupriyanov.compositionapp.domain.entities.GameResult
import com.kupriyanov.compositionapp.domain.entities.Level

class GameFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(
            this
        )[GameViewModel::class.java]
    }
    private lateinit var level: Level

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
        viewModel.startGame(level)
        setClickListenersToOptions()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        setupTimerObserver()
        setupQuestionObserver()
        setupGameResultObserver()
        setupPercentOfRightAnswersObserver()
        setupMinPercentObserver()
        setupProgressAnswers()
        setupEnoughPercentObserver()
        setupEnoughCountObserver()
    }

    private fun setupEnoughCountObserver() {
        viewModel.enoughCount.observe(viewLifecycleOwner) {
            binding.tvProgressAnswers.setTextColor(getColorByState(it))
        }
    }

    private fun setupEnoughPercentObserver() {
        viewModel.enoughPercent.observe(viewLifecycleOwner) {
            binding.progressBar.progressTintList = ColorStateList.valueOf(getColorByState(it))
        }
    }

    private fun setupProgressAnswers() {
        viewModel.progressAnswers.observe(viewLifecycleOwner) {
            binding.tvProgressAnswers.text = it
        }
    }

    private fun setupMinPercentObserver() {
        viewModel.minPercent.observe(viewLifecycleOwner) {
            binding.progressBar.secondaryProgress = it
        }
    }

    private fun setupPercentOfRightAnswersObserver() {
        viewModel.percentOfRightAnswers.observe(viewLifecycleOwner) {
            binding.progressBar.setProgress(it, true)
        }
    }

    private fun setupTimerObserver() {
        viewModel.formattedTime.observe(viewLifecycleOwner) {
            binding.tvTimer.text = it
        }
    }

    private fun setupGameResultObserver() {
        viewModel.gameResult.observe(viewLifecycleOwner) {
            launchGameFinishedFragment(it)
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

    private fun setClickListenersToOptions() {
        with(binding) {
            tvOption1.setOnClickListener {
                viewModel.chooseAnswer(tvOption1.text.toString().toInt())
            }
            tvOption2.setOnClickListener {
                viewModel.chooseAnswer(tvOption2.text.toString().toInt())
            }
            tvOption3.setOnClickListener {
                viewModel.chooseAnswer(tvOption3.text.toString().toInt())
            }
            tvOption4.setOnClickListener {
                viewModel.chooseAnswer(tvOption4.text.toString().toInt())
            }
            tvOption5.setOnClickListener {
                viewModel.chooseAnswer(tvOption5.text.toString().toInt())
            }
            tvOption6.setOnClickListener {
                viewModel.chooseAnswer(tvOption6.text.toString().toInt())
            }
        }
    }

    private fun getColorByState(goodState: Boolean): Int {
        val colorResId = if (goodState) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }
        return ContextCompat.getColor(requireContext(), colorResId)
    }

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
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