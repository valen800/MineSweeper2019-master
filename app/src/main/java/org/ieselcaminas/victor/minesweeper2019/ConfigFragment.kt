package org.ieselcaminas.victor.minesweeper2019


import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.fragment_config.*
import org.ieselcaminas.victor.minesweeper2019.databinding.FragmentConfigBinding
import java.lang.NumberFormatException

/**
 * A simple [Fragment] subclass.
 */
class ConfigFragment : Fragment() {
    data class ConfigData(var numRows: Int, var numCols: Int)

    var configData = ConfigData(10,10)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding: FragmentConfigBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_config,
            container, false)

        binding.config = this

        val arrayDifficulty = arrayOf( "Easy", "Normal", "Hard")
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, arrayDifficulty)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        binding.spinnerDififculty.adapter = adapter

        binding.buttonPlay.setOnClickListener() {
            //Navigation.findNavController(it).navigate(R.id.action_configFragment_to_gameFragment)
            it.findNavController().navigate(ConfigFragmentDirections.actionConfigFragmentToGameFragment(configData.numRows, configData.numCols))
        }

        binding.editTextRows.addTextChangedListener() {
            try {
                configData.numRows = it.toString().toInt()
                //binding.NumRow.setText(it.toString())

            }catch (ex: NumberFormatException) {
                configData.numCols = 0
                //binding.NumRow.setText(0)
            }
            binding.invalidateAll()
        }

        binding.editTextCols.addTextChangedListener() {
            try {
                configData.numCols = it.toString().toInt()
                //binding.NumCol.setText(it.toString())
            }catch (ex: NumberFormatException) {
                configData.numCols = 0
                //binding.NumCol.setText(0)
            }
            binding.invalidateAll()
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }

    /*private fun getShareIntent(): Intent {
        //var args = ConfigFragment.fromBundle(arguments!!)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(
            Intent.EXTRA_TEXT, getString(R.string.share_success_text))

        return shareIntent
    }

    private fun shareSuccess() {
        startActivity(getShareIntent())
    }

    private fun throwAboutFragment() {
        val intent = Intent(activity, AboutFragment::class.java)
        startActivity(intent)
    }*/

}
