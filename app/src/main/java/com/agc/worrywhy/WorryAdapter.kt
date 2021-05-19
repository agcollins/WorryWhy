package com.agc.worrywhy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

internal class WorryAdapter(var worries: List<Worry> = emptyList(), private val clickListener: (Worry) -> Unit) :
    RecyclerView.Adapter<WorryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_worry,
            parent,
            false
        ).let(::ViewHolder)

    override fun getItemCount() = worries.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val worry = worries[position]
        holder.content.text = worry.content
        // TODO -- The creation of this lambda is slow -- find another way
        holder.card.setOnClickListener {
            clickListener(worry)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content: TextView = itemView.findViewById(R.id.text_worry_content)
        val card: MaterialCardView = itemView.findViewById(R.id.card_worry)
    }
}

