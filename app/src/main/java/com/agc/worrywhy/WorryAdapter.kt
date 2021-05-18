package com.agc.worrywhy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WorryAdapter(var worries: List<Worry> = emptyList()) : RecyclerView.Adapter<WorryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_worry,
            parent,
            false
        ).let(::ViewHolder)

    override fun getItemCount() = worries.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.content.text = worries[position].content
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content: TextView = itemView.findViewById(R.id.text_worry_content)
    }
}

