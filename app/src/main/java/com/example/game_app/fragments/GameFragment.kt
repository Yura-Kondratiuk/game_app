package com.example.game_app.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.game_app.R
import com.example.game_app.databinding.FragmentGameBinding
import com.example.game_app.utils.viewBinding

class GameFragment : Fragment(R.layout.fragment_game) {

    private val binding by viewBinding(FragmentGameBinding::bind)

    private var list = mutableListOf<Pair<TextView, Position>>()

    private val manager = GameManager()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list = mutableListOf(
            Pair(binding.one, Position(0, 0)),
            Pair(binding.two, Position(0, 1)),
            Pair(binding.three, Position(0, 2)),
            Pair(binding.four, Position(1, 0)),
            Pair(binding.five, Position(1, 1)),
            Pair(binding.six, Position(1, 2)),
            Pair(binding.seven, Position(2, 0)),
            Pair(binding.eight, Position(2, 1)),
            Pair(binding.nine, Position(2, 2))
        )

        list.forEach { data ->
            data.first.setOnClickListener { onBoxClicked(data.first, data.second) }
        }
        binding.startNewGameButton.setOnClickListener {
            binding.startNewGameButton.isVisible = false
            manager.reset()
            resetBoxes()
        }

        updatePoints()

    }

    private fun updatePoints() {
        binding.playerOneScore.text = "Player X Points: ${manager.player1Points}"
        binding.playerTwoScore.text = "Player O Points: ${manager.player2Points}"
    }

    private fun resetBoxes() {
        list.forEach {
            it.first.text = ""
            it.first.background = null
            it.first.isEnabled = true
        }
    }

    private fun onBoxClicked(box: TextView, position: Position) {
        if (box.text.isEmpty()) {
            box.text = manager.currentPlayerMark
            val winningLine = manager.makeMove(position)
            if (winningLine != null) {
                updatePoints()
                disableBoxes()
                binding.startNewGameButton.visibility = View.VISIBLE
                showWinner(winningLine)
            }
        }
    }

    private fun disableBoxes() {
        list.forEach {
            it.first.isEnabled = false
        }
    }

    private fun showWinner(winningLine: WinningLine) {
        with(binding) {
            val (winningBoxes, background) = when (winningLine) {
                WinningLine.ROW_0 -> Pair(listOf(one, two, three), R.drawable.horizontal_line)
                WinningLine.ROW_1 -> Pair(listOf(four, five, six), R.drawable.horizontal_line)
                WinningLine.ROW_2 -> Pair(listOf(seven, eight, nine), R.drawable.horizontal_line)
                WinningLine.COLUMN_0 -> Pair(listOf(one, four, seven), R.drawable.vertical_line)
                WinningLine.COLUMN_1 -> Pair(listOf(two, five, eight), R.drawable.vertical_line)
                WinningLine.COLUMN_2 -> Pair(listOf(three, six, nine), R.drawable.vertical_line)
                WinningLine.DIAGONAL_LEFT -> Pair(
                    listOf(one, five, nine),
                    R.drawable.left_diagonal_line
                )
                WinningLine.DIAGONAL_RIGHT -> Pair(
                    listOf(three, five, seven),
                    R.drawable.right_diagonal_line
                )
            }

            winningBoxes.forEach { box ->
                box.background = ContextCompat.getDrawable(requireContext(), background)
            }
        }
    }

    companion object {
        fun newInstance() = GameFragment()
    }
}