package com.agc.worrywhy.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.agc.worrywhy.R
import com.agc.worrywhy.WorryAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_worry_list.*

@AndroidEntryPoint
class WorryListFragment : Fragment() {
    private val worryListViewModel: WorryListViewModel by viewModels()
    private val adapter = WorryAdapter(
        clickListener = { worry ->
            worryListViewModel.recordWorry(worry.uid)
        }, deleteListener = { worry ->
            worryListViewModel.deleteWorry(worry.uid)
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
            adapter.worries = it
        })

        fab_add_worry.setOnClickListener { view ->
            findNavController().navigate(
                WorryListFragmentDirections.actionWorryListFragmentToAddWorryFragment()
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_remove_all -> {
                worryListViewModel.removeAllWorries()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}