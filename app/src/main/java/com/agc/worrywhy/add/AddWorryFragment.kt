package com.agc.worrywhy.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.agc.worrywhy.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.add_worry_fragment.*

@AndroidEntryPoint
class AddWorryFragment : Fragment() {
    private val viewModel: AddWorryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_worry_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edit_text_add_worry.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                button_worry_add.performClick()
                true
            } else false
        }

        button_worry_add.setOnClickListener {
            viewModel.addWorry(edit_text_add_worry.text.toString(), check_current_worry.isChecked)
            findNavController().popBackStack()
        }
    }
}