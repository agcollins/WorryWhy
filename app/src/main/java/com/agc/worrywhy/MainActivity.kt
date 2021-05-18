package com.agc.worrywhy

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    private val viewModel: WorryViewModel by viewModels()
    private val adapter = WorryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewModel.worries.observe(this, Observer {
            adapter.worries = it
            adapter.notifyDataSetChanged()
        })

        recycler_worries.adapter = adapter

        fab.setOnClickListener { view ->
            viewModel.addWorry("this")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                viewModel.removeAllWorries()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

