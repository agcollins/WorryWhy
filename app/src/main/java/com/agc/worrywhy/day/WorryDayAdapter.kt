package com.agc.worrywhy.day

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.agc.worrywhy.R
import com.agc.worrywhy.persistence.relationship.WorryWithInstancesAndText

internal class WorryDayAdapter(private val resources: Resources) :
    RecyclerView.Adapter<WorryDayAdapter.ViewHolder>() {
    var source: List<WorryWithInstancesAndText> = emptyList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val content: TextView = view.findViewById(R.id.text_day_content)
        val summary: TextView = view.findViewById(R.id.text_day_summary)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_day_worries)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_day_worry, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val worry = source[position]
        holder.content.text = worry.worry.content
        holder.summary.text = resources.getQuantityString(
            R.plurals.times_template_calendar,
            worry.instances.size,
            worry.instances.size
        )
    }

    override fun getItemCount(): Int {
        return source.size
    }
}