package org.ieselcaminas.victor.minesweeper2019


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import org.ieselcaminas.victor.minesweeper2019.databinding.FragmentLoseBinding

/**
 * A simple [Fragment] subclass.
 */
class GameLose : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentLoseBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_lose, container, false)
        return binding.root
    }


}
