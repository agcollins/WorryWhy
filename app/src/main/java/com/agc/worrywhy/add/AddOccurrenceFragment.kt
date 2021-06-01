package com.agc.worrywhy.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.agc.worrywhy.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_occurrence.*

@AndroidEntryPoint
class AddOccurrenceFragment : Fragment() {
    private val viewModel: AddWorryViewModel by viewModels()
    private val args: AddOccurrenceFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_occurrence, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edit_text_add_occurrence.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                button_occurrence_add.performClick()
                true
            } else false
        }

        button_occurrence_add.setOnClickListener {
            val occurrenceText =
                edit_text_add_occurrence.text.toString().trim().takeUnless { it.isEmpty() }

            viewModel.addWorryOccurrence(args.worryId, occurrenceText)
            findNavController().popBackStack()
        }
    }
}