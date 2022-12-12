package com.kupriyanov.compositionapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kupriyanov.compositionapp.R
import com.kupriyanov.compositionapp.data.GameRepositoryImpl
import com.kupriyanov.compositionapp.databinding.FragmentGameBinding
import com.kupriyanov.compositionapp.domain.entities.GameResult
import com.kupriyanov.compositionapp.domain.entities.GameSettings
import com.kupriyanov.compositionapp.domain.entities.Level

class GameFragment : Fragment() {

    private lateinit var gameRepositoryImpl: GameRepositoryImpl
    private lateinit var level: Level
    private lateinit var gameSettings: GameSettings

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
        binding.tvSum.setOnClickListener {
            launchGameFinishedFragment(
                GameResult(
                true,
                23,
                25,
                gameSettings
            )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
    }

    private fun parseArgs() {
        gameRepositoryImpl = GameRepositoryImpl
        requireArguments().getParcelable<Level>(LEVEL_KEY)?.let {
            level = it
        }
        gameSettings = gameRepositoryImpl.getGameSettings(level)
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