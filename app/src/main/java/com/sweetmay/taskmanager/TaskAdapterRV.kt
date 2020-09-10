package com.sweetmay.taskmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_task.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TaskAdapterRV(var onItemRVClick: OnItemRVClick) : RecyclerView.Adapter<TaskAdapterRV.ViewHolder>() {
    val smf = SimpleDateFormat("EEE, d MMM yyyy HH:mm", Locale.getDefault())
    var notes = ArrayList<Note>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    init {

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
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(note: Note) = with(itemView){
            note_title_rv.text = note.title
            note_body_rv.text = note.text
            note_date_rv.text = smf.format(note.date)
        }
    }
}