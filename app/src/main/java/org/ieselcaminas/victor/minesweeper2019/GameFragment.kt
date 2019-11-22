package org.ieselcaminas.victor.minesweeper2019


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_game.*
import org.ieselcaminas.victor.minesweeper2019.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    lateinit var binding: FragmentGameBinding
    lateinit var board: Array<Array<MineButton>>
    lateinit var bombMatrix: BombMatrix
    var numRows: Int = 0
    var numCols: Int = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_game, container, false)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)

        binding.buttonWin.setOnClickListener() {
            it.findNavController().navigate(GameFragmentDirections.actionGameFragmentToWon())
        }

        binding.buttonLose.setOnClickListener() {
            it.findNavController().navigate(GameFragmentDirections.actionGameFragmentToLose())
        }

        binding.buttonRestart.setOnClickListener() {
            clearBoard()
        }

        var args = GameFragmentArgs.fromBundle(arguments!!)
        numRows = args.numRows
        numCols = args.numCols
        bombMatrix = BombMatrix(numRows, numCols, (numRows * numCols) / 6)



        createButtons(bombMatrix)

        return binding.root

    }

    private fun clearBoard(){
        GridLayoutButtons.removeAllViews()
        restartGame()
    }

    private fun restartGame() {

        var args = GameFragmentArgs.fromBundle(arguments!!)
        numRows = args.numRows
        numCols = args.numCols
        bombMatrix = BombMatrix(numRows, numCols, (numRows * numCols) / 6)

        binding.LinearGlobal.setBackgroundColor(Color.WHITE)
        binding.textLoose.visibility = View.INVISIBLE

        createButtons(bombMatrix)
    }

    private fun createButtons(boardMatrix: BombMatrix) {
        board = Array(numRows) { row ->
            Array(numCols) { col ->
                MineButton(context!!, row, col)
            }
        }

        binding.GridLayoutButtons.columnCount = numCols
        binding.GridLayoutButtons.rowCount = numRows

        for (line in board) {
            for (button in line) {
                var FrameLayout = FrameLayout(context!!)
                val backgroundImageView = ImageView(context!!)
                val numBack = TextView(context!!)
                val backgroundImageBomb = ImageView(context!!)
                var layoutParams = LinearLayout.LayoutParams(SIZE, SIZE)
                var layoutParamsBombs = LinearLayout.LayoutParams(SIZE - 10, SIZE - 10)

                backgroundImageView.setImageResource(R.drawable.back)

                printAndFormatNumbersAndBomb(boardMatrix, button, backgroundImageBomb, backgroundImageView, numBack, layoutParamsBombs)

                adjustFrameLayout(backgroundImageView, layoutParams,numBack, backgroundImageView, button, FrameLayout)

                binding.GridLayoutButtons.addView(FrameLayout)
                button.setOnClickListener() {
                    gameOver(boardMatrix, button)
                    if (button.state == StateType.CLOSED) {
                        open(button.row, button.col)

                        toastPrint(button)
                        button.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    private fun toastPrint(button: MineButton) {
        Toast.makeText(
            context,
            "" + (button.row + 1) + " X " + (button.col + 1),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun toasPrint(row: Int, col: Int) {
        Toast.makeText(
            context, "Rows = ${row} Cols = ${col}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun adjustFrameLayout(backgroundImageView: ImageView, layoutParams: LinearLayout.LayoutParams,
                                  numBack: TextView, backgroundImageBomb: ImageView, button: MineButton,
                                  FrameLayout: FrameLayout) {
        backgroundImageView.setPadding(0, 0, 0, 0)
        backgroundImageView.scaleType = ImageView.ScaleType.CENTER
        backgroundImageView.adjustViewBounds = true
        backgroundImageView.layoutParams = layoutParams

        FrameLayout.addView(backgroundImageView)
        FrameLayout.addView(numBack)
        FrameLayout.addView(backgroundImageBomb)
        FrameLayout.addView(board[button.row][button.col])
    }

    private fun open(row: Int, col: Int) {
        if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
            return
        }
        board[row][col].state = StateType.OPEN
        board[row][col].visibility = View.INVISIBLE

        if (bombMatrix.value(row, col) == 0) {
            for (i in row - 1..row + 1) {
                for (j in col - 1..col + 1) {
                    if (i >= 0 && i < numRows && j >= 0 && j < numCols) {
                        if (1 != row && j != col && board[i][j].state == StateType.CLOSED) {
                            open(i, j)
                        }
                    }
                }
            }
        }
    }

    private fun gameOver(boardMatrix: BombMatrix, button: MineButton) {
        if (boardMatrix.value(button.row, button.col) == BOMB_INT) {
            binding.LinearGlobal.setBackgroundColor(Color.RED)
            binding.textLoose.visibility = View.VISIBLE

            for (row in 0..board.size-1) {
                for (col in 0..board.size-1) {
                    board[row][col].state = StateType.OPEN
                    board[row][col].visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun printAndFormatNumbersAndBomb(
        boardMatrix: BombMatrix,
        button: MineButton,
        backgroundImageBomb: ImageView,
        backgroundImage: ImageView,
        numBack: TextView,
        layoutParamsBombs: LinearLayout.LayoutParams
    ) {

        if (boardMatrix.value(button.row, button.col) == BOMB_INT) {
            setBombsOnBoard(backgroundImageBomb, backgroundImage, layoutParamsBombs)


        } else if (!(boardMatrix.value(button.row, button.col) == 0)) {
            colorNumber(numBack, boardMatrix, button)
        }
    }

    private fun setBombsOnBoard(backgroundImageBomb: ImageView, backgroundImage: ImageView,
                                layoutParamsBombs: LinearLayout.LayoutParams) {
        backgroundImageBomb.setImageResource(R.drawable.bomb)
        backgroundImage.setColorFilter(Color.RED)
        backgroundImageBomb.layoutParams = layoutParamsBombs
    }

    private fun colorNumber(numBack: TextView, boardMatrix: BombMatrix, button: MineButton) {
        numBack.text = boardMatrix.value(button.row, button.col).toString()
        when (boardMatrix.value(button.row, button.col)) {
            1 -> numBack.setTextColor(Color.BLUE)
            2 -> numBack.setTextColor(Color.BLACK)
            3 -> numBack.setTextColor(Color.GREEN)
            4 -> numBack.setTextColor(Color.GRAY)
            5 -> numBack.setTextColor(Color.CYAN)
            6 -> numBack.setTextColor(Color.MAGENTA)
            7 -> numBack.setTextColor(Color.RED)
            8 -> numBack.setTextColor(Color.LTGRAY)
        }
    }
}
