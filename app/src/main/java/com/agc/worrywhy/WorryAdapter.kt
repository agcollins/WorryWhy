package com.agc.worrywhy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

internal class WorryAdapter(
    private val clickListener: (CompleteWorry) -> Unit,
    private val deleteListener: (CompleteWorry) -> Unit
) : RecyclerView.Adapter<WorryAdapter.ViewHolder>() {
    /**
     * The current list of worries tracked.
     *
     * When set, automatically updates the adapter.
     */
    var worries: List<CompleteWorry> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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
        holder.frequency.apply {
            isGone = worry.count == 0
            text = context.getString(R.string.times_template, worry.count)
        }

        // TODO -- The creation of this lambda is slow -- find another way
        holder.card.setOnClickListener {
            clickListener(worry)
        }
//
//        holder.deleteButton.setOnClickListener {
//            deleteListener(worry)
//        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content: TextView = itemView.findViewById(R.id.text_worry_content)
        val frequency: TextView = itemView.findViewById(R.id.text_worry_times)
        val card: MaterialCardView = itemView.findViewById(R.id.card_worry)
        val deleteButton: Button = itemView.findViewById(R.id.button_delete_worry)
    }
}

