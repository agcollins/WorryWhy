package com.agc.worrywhy.worry

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.agc.worrywhy.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_worry_single.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WorryFragment : Fragment() {
    private val viewModel: WorryViewModel by viewModels()
    private val args: WorryFragmentArgs by navArgs()
    private val adapter = WorryOccurrenceAdapter {
        viewModel.deleteOccurrence(it.uid)
    }

    override fun onStart() {
        super.onStart()
        viewModel.select(args.worryId)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_worry_single, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_worry, menu)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("worryId", args.worryId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_worry_occurrences.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        recycler_worry_occurrences.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.worry.collect {
                if (it == null) return@collect
                text_worry_content.text = it.worry.content
                worry_occurrences_title.text =
                    requireContext().getString(R.string.template_occurences, it.instances.size)

                adapter.instances = it.instances
            }
        }

        image_edit_worry.setOnClickListener {
            image_edit_worry.isGone = true
            text_worry_content.isGone = true
            edit_text_worry_content.setText(text_worry_content.text)
            edit_text_worry_content.isGone = false
            edit_text_worry_content.requestFocus()
        }

        edit_text_worry_content.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                submitTitle()
                true
            } else false
        }

        edit_text_worry_content.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                submitTitle()
            }
        }
    }

    private fun submitTitle() {
        saveTitle()
        image_edit_worry.isGone = false
        text_worry_content.isGone = false
        edit_text_worry_content.isGone = true
    }

    private fun saveTitle() = viewModel.saveTitle(edit_text_worry_content.text.toString(), args.worryId)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete_worry -> {
                viewModel.deleteWorry(args.worryId)
                findNavController().popBackStack()
                true
            }
            R.id.menu_add_occurrence -> {
                findNavController().navigate(
                    WorryFragmentDirections.actionWorryFragmentToAddOccurrenceFragment(
                        args.worryId
                    )
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}