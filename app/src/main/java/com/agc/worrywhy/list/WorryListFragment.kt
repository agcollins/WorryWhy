package com.agc.worrywhy.list

import android.os.Bundle
import android.view.*
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.agc.worrywhy.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_worry_list.*

@AndroidEntryPoint
class WorryListFragment : Fragment() {
    private val worryListViewModel: WorryListViewModel by viewModels()
    private val adapter = WorryAdapter(
        clickListener = { worry ->
            findNavController().navigate(
                WorryListFragmentDirections.actionWorryListFragmentToWorryFragment(
                    worry.uid
                )
            )
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_worry_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_worries.adapter = adapter

        worryListViewModel.worries.observe(viewLifecycleOwner, {
            text_no_worries.isVisible = it.isEmpty()
            adapter.worries = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_worry -> {
                findNavController().navigate(
                    WorryListFragmentDirections.actionWorryListFragmentToAddWorryFragment()
                )
                true
            }
            R.id.menu_remove_all -> {
                worryListViewModel.removeAllWorries()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}