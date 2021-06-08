package com.agc.worrywhy.worry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.agc.worrywhy.R
import com.agc.worrywhy.persistence.entity.WorryInstance
import com.agc.worrywhy.persistence.relationship.WorryTextInstance
import java.text.SimpleDateFormat
import java.util.*

internal class WorryOccurrenceAdapter(
    private val deleteListener: (WorryInstance) -> Unit,
) : RecyclerView.Adapter<WorryOccurrenceAdapter.ViewHolder>() {
    private val dateFormat = SimpleDateFormat("MM/dd/yyyy h:mm a", Locale.getDefault())
    var instances: List<WorryTextInstance> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_worry_occurrence, parent, false)
            .let(::ViewHolder)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val instance = instances[position].instance
        holder.occurrenceDatetime.text = dateFormat.format(instance.date)
        holder.deleteButton.setOnClickListener {
            deleteListener(instance)
        }
        val content = instances[position].text.firstOrNull()?.content
        holder.occurrenceText.apply {
            if (text == null) {
                isGone = true
            } else {
                isGone = false
                text = content
            }
        }
    }

    override fun getItemCount() = instances.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val occurrenceDatetime: TextView = itemView.findViewById(R.id.text_occurrence_datetime)
        val occurrenceText: TextView = itemView.findViewById(R.id.text_occurrence_content)
        val deleteButton: ImageView = itemView.findViewById(R.id.image_remove_occurrence)
    }

}