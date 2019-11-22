package org.ieselcaminas.victor.minesweeper2019

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.databinding.adapters.ViewBindingAdapter.setOnLongClickListener
const val SIZE = 50

class MineButton constructor(context: Context, var row: Int, var col: Int): ImageButton(context) {
    public var state: StateType = StateType.CLOSED

    init {


        layoutParams = LinearLayout.LayoutParams(SIZE, SIZE)
        setBackground(getDrawable(context, R.drawable.boton))

        setPadding(0,0,0,0)
        scaleType = ImageView.ScaleType.CENTER
        adjustViewBounds = true

        setOnTouchListener() { view: View, motionEvent: MotionEvent ->
            val button: MineButton = view as MineButton
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                button.background = getDrawable(context, R.drawable.boton_pressed)
            } else {
                if (motionEvent.action == MotionEvent.ACTION_UP || motionEvent.action == MotionEvent.ACTION_CANCEL) {
                    button.background = getDrawable(context, R.drawable.boton)

                }
            }
            false
        }

        setOnLongClickListener() {
            var button: MineButton = it as MineButton
            button.setImageDrawable(getDrawable(context, R.drawable.flag))


            when (button.state) {
                StateType.CLOSED -> {button.setImageDrawable(
                    getDrawable(context, R.drawable.flag))
                    button.state = StateType.FLAG }

                StateType.FLAG -> {button.setImageDrawable(
                    getDrawable(context, R.drawable.question))
                    button.state = StateType.QUESTION }

                StateType.QUESTION -> {button.setImageDrawable(null)
                    button.state = StateType.CLOSED}
            }



            true
        }
    }
}