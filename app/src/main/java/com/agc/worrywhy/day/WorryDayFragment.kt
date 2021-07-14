package com.agc.worrywhy.day

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.agc.worrywhy.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_day_worries.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WorryDayFragment : Fragment() {
    private val adapter by lazy {
        WorryDayAdapter(resources)
    }

    private val args: WorryDayFragmentArgs by navArgs()
    private val viewModel: WorryDayViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_day_worries, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_day.adapter = adapter

        lifecycleScope.launch {
            viewModel.worryFlow.collect {
                adapter.source = it
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.select(args.date)
    }
}