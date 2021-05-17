package com.agc.worrywhy

import android.os.Bundle
import android.text.Layout
import android.view.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    class WorryAdapter(private val worries: List<Worry>) :
        RecyclerView.Adapter<WorryAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            LayoutInflater.from(parent.context).let {
                it.inflate(R.layout.item_worry, parent, false)
            }.let(::ViewHolder)

        override fun getItemCount() = worries.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.content.text = worries[position].content
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val content: TextView = itemView.findViewById(R.id.text_worry_content)
        }
    }

    private val database by lazy {
        Room.databaseBuilder<AppDatabase>(this, AppDatabase::class.java, "app").build()
    }

    private val dao by lazy {
        database.worryDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            lifecycleScope.launch(Dispatchers.Default) {
                dao.addWorry(Worry("Something random #" + Random.nextInt(1000)))
                withContext(Dispatchers.Main) {
                    recycler_worries.adapter = WorryAdapter(
                        withContext(Dispatchers.Default) {
                            dao.getAll()
                        }
                    )
                }
            }
        }

        lifecycleScope.launch(Dispatchers.Default) {
            recycler_worries.adapter = WorryAdapter(dao.getAll())
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
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
