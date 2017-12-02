package com.imangazaliev.notelin.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.imangazaliev.notelin.R
import com.imangazaliev.notelin.mvp.model.Note
import com.imangazaliev.notelin.utils.formatDate

class NotesAdapter(private val notesList: List<Note>) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): NotesAdapter.ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.note_item_layout, viewGroup, false)
        return NotesAdapter.ViewHolder(v)
    }

    override
    fun onBindViewHolder(viewHolder: NotesAdapter.ViewHolder, i: Int) {
        val note = notesList[i]
        viewHolder.noteTitle.text = note.title
        viewHolder.noteDate.text = formatDate(note.changedAt)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var noteTitle: TextView = itemView.findViewById(R.id.tvItemNoteTitle) as TextView
        var noteDate: TextView = itemView.findViewById(R.id.tvItemNoteDate) as TextView

    }

}
