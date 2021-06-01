package com.agc.worrywhy.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.agc.worrywhy.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_worry.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddWorryFragment : Fragment() {
    private val viewModel: AddWorryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_worry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        check_current_worry.setOnCheckedChangeListener { _, checked ->
            button_worry_add.text = getString(if (checked) R.string.next else R.string.add)
        }

        edit_text_add_worry.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                button_worry_add.performClick()
                true
            } else false
        }

        button_worry_add.setOnClickListener {
            val currentWorry = check_current_worry.isChecked
            val worryText = edit_text_add_worry.text.toString()

            lifecycleScope.launch {
                val id = viewModel.addWorry(worryText)

                if (!currentWorry) {
                    findNavController().popBackStack()
                } else {
                    findNavController().navigate(
                        AddWorryFragmentDirections.actionAddWorryFragmentToAddOccurrenceFragment(id)
                    )
                }
            }
        }
    }
}