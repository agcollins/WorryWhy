package com.agc.worrywhy.list

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.agc.worrywhy.R
import com.agc.worrywhy.tabs.WorryTabsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_worry_list.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WorryListFragment : Fragment() {
    private val callbacks by lazy {
        requireParentFragment() as Callbacks
    }

    interface Callbacks {
        fun onAddWorry()
        fun onTapWorry(worryId: Long)
    }

    private val worryListViewModel: WorryListViewModel by viewModels()
    private val adapter = WorryAdapter(
        clickListener = { worry ->
            callbacks.onTapWorry(
                worry.uid
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

        viewLifecycleOwner.lifecycleScope.launch {
            worryListViewModel.worries.collect {
                requireActivity().invalidateOptionsMenu()
                if (it == null) return@collect
                text_no_worries.isVisible = it.isEmpty()
                adapter.update(it)
            }
        }

        fab_add.setOnClickListener {
            goToWorries()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.menu_remove_all).isVisible =
            worryListViewModel.worries.value?.isNotEmpty() ?: false

        val searchView = (menu.findItem(R.id.menu_search_worries).actionView as SearchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                query?.let(adapter::filter)
                return true
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_worries, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_search_worries -> {
                true
            }
            R.id.menu_remove_all -> {
                worryListViewModel.removeAllWorries()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun goToWorries() = callbacks.onAddWorry()
}