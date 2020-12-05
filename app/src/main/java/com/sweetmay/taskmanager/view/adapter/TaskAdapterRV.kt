package com.sweetmay.taskmanager.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sweetmay.taskmanager.R
import com.sweetmay.taskmanager.model.Note
import com.sweetmay.taskmanager.view.callback.OnItemRVClick
import com.sweetmay.taskmanager.view.ui.OnItemRVLongClick
import kotlinx.android.synthetic.main.item_task.view.*
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapterRV(var onItemRVClick: OnItemRVClick, var onItemRVLongClick: OnItemRVLongClick) : RecyclerView.Adapter<TaskAdapterRV.ViewHolder>() {
    val smf = SimpleDateFormat("EEE, d MMM yyyy HH:mm", Locale.getDefault())
    var notes = listOf<Note>()
    set(value) {
        Collections.sort(value)
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notes[position])
        holder.itemView.setOnClickListener{
            onItemRVClick.onItemClick(notes[position])
        }
        holder.itemView.setOnLongClickListener {
            onItemRVLongClick.onItemLongClick(notes[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(note: Note) = with(itemView){
            notes_container.strokeColor = note.color
            note_title_rv.text = note.title
            note_body_rv.text = note.text
            note_date_rv.text = smf.format(note.date)
        }
    }
}