package com.agc.worrywhy.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.agc.worrywhy.R
import com.agc.worrywhy.calendar.CalendarFragment
import com.agc.worrywhy.list.WorryListFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_worry_tabs.*

@AndroidEntryPoint
class WorryTabsFragment : Fragment(), WorryListFragment.Callbacks {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_worry_tabs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> WorryListFragment()
                    1 -> CalendarFragment()
                    else -> error("invalid position $position")
                }
            }

            override fun getItemCount() = 2
        }

        TabLayoutMediator(tabs, pager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.list)
                1 -> getString(R.string.calendar)
                else -> error("invalid position $position")
            }
        }.attach()
    }

    override fun onAddWorry() {
        findNavController().navigate(
            WorryTabsFragmentDirections.actionWorryTabsFragmentToAddWorryFragment()
        )
    }

    override fun onTapWorry(worryId: Long) {
        findNavController().navigate(
            WorryTabsFragmentDirections.actionWorryTabsFragmentToWorryFragment(
                worryId
            )
        )
    }
}