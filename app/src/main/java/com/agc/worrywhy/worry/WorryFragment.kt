package com.agc.worrywhy.worry

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.agc.worrywhy.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_worry_single.*

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_worry_occurrences.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        recycler_worry_occurrences.adapter = adapter

        viewModel.worry.observe(viewLifecycleOwner, {
            if (it == null) return@observe
            text_worry_content.text = it.worry.content
            worry_occurrences_title.text =
                requireContext().getString(R.string.template_occurences, it.instances.size)

            adapter.instances = it.instances
            println(it)
        })
    }

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