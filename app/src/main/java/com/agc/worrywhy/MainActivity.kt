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
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                viewModel.removeAllWorries()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

